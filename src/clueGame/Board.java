package clueGame;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Board {
	private static Board instance = new Board();

	private String legendFile;
	private String boardFile;

	private Map<Character, String> legend = new HashMap<>();
	private ArrayList<String> roomList = new ArrayList<>();

	private Map<BoardCell, Set<BoardCell>> adjacencyList = new HashMap<>();
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private BoardCell[][] board;

	int numRows;
	int numColumns;

	private Board() {}

	public void initialize() {
		try {
			loadRoomConfig();
			loadBoardConfig();
			calcAdjacencies();
		} catch (FileNotFoundException | BadConfigFormatException e) {
			e.printStackTrace();
		}
	}

	public void setConfigFiles(String boardFile, String legendFile) {
		this.boardFile = boardFile;
		this.legendFile = legendFile;
	}

	//Loads legend into memory
	public void loadRoomConfig() throws BadConfigFormatException, FileNotFoundException {
		String filepath = legendFile;

		//open new input stream to read in legendConfigFile
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		String line;

		legend = new HashMap<Character, String>();
		roomList = new ArrayList<>();

		try {
			//Used for error tracking
			int count = 0;

			//Reads legend
			while ((line = reader.readLine()) != null) {
				String[] lineSplit = line.split(", ");
				legend.put(lineSplit[0].charAt(0), lineSplit[1]);

				//Check that each entry in legend is either a "Card" or "Other"
				//if not, throw a BadConfigFormatException
				if (!lineSplit[2].trim().equals("Card") && !lineSplit[2].trim().equals("Other")) {
					reader.close();
					throw new BadConfigFormatException("Legend entry is not listed as either Card or Other on line " + count);
				}

				if (lineSplit[2].trim().equals("Card"))
					getRoomList().add(lineSplit[1]);

				count++;
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	//Loads board into memory
	public void loadBoardConfig() throws BadConfigFormatException, FileNotFoundException {
		String filepath = boardFile;

		//open input stream to read boardConfigFile
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		String line;

		ArrayList<String[]> boardChars = new ArrayList<>();

		try {
			//Reads csv file for rooms into 2D array
			while ((line = reader.readLine()) != null) {
				if (line.charAt(0) == 'ï')
					line = line.substring(3);

				String[] lineSplit = line.split(",");
				boardChars.add(lineSplit);
			}

			reader.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

		numRows = boardChars.size();
		numColumns = boardChars.get(0).length;

		//check that number of columns is the same for all rows
		//if not, throw a BadConfigFormatException
		for (int i = 0; i < numRows; i++) {
			if (boardChars.get(i).length != numColumns) {
				throw new BadConfigFormatException("The number of columns is not the same for all rows.");
			}
		}

		board = new BoardCell[numRows][numColumns];

		//Parses array into board cells
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				String cellStr = boardChars.get(i)[j];
				board[i][j] = new BoardCell(i ,j, cellStr.charAt(0));

				if (cellStr.length() > 1) {
					char secondChar = cellStr.charAt(1);

					if (secondChar == 'N') {
						//board[i][j].setShouldDisplayName(true);
					} else {
						char doorDir = secondChar;
						DoorDirection direction;

						switch (doorDir) {
						case 'U':
							direction = DoorDirection.UP;
							break;
						case 'D':
							direction = DoorDirection.DOWN;
							break;
						case 'R':
							direction = DoorDirection.RIGHT;
							break;
						case 'L':
							direction = DoorDirection.LEFT;
							break;
						default:
							//Code gets here if there is an unrecogized secondary character
							throw new BadConfigFormatException("Invalid secondary character");
						}

						board[i][j].setDoorDirection(direction);
					}
				}
			}
		}

		//check that all the cells in the board are represented in the legend
		//if not, throw a BadConfigFormatException
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numColumns; j++) {
				if (!legend.containsKey(board[i][j].getInitial())) {
					throw new BadConfigFormatException("The board contains a room that is not present in the legend at location " + i + ", " + j + " with initial " + board[i][j].getInitial());
				}
			}
		}
	}

	public void calcAdjacencies() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				Set<BoardCell> list = new HashSet<>();

				if (board[i][j].isDoorway() || board[i][j].isWalkway()) {
					if (i > 0) {
						BoardCell cell = board[i - 1][j];
						if (cell.isWalkway())
							list.add(cell);
						else if (cell.isDoorway() && cell.getDoorDirection() == DoorDirection.DOWN)
							list.add(cell);
					}

					if (j > 0) {
						BoardCell cell = board[i][j - 1];
						if (cell.isWalkway())
							list.add(cell);
						else if (cell.isDoorway() && cell.getDoorDirection() == DoorDirection.RIGHT)
							list.add(cell);
					}

					if (i < board.length - 1) {
						BoardCell cell = board[i + 1][j];
						if (cell.isWalkway())
							list.add(cell);
						else if (cell.isDoorway() && cell.getDoorDirection() == DoorDirection.UP)
							list.add(cell);
					}

					if (j < board[0].length - 1) {
						BoardCell cell = board[i][j + 1];
						if (cell.isWalkway())
							list.add(cell);
						else if (cell.isDoorway() && cell.getDoorDirection() == DoorDirection.LEFT)
							list.add(cell);
					}
				}

				adjacencyList.put(board[i][j], list);
			}
		}
	}

	public void calcTargets(int i, int j, int pathLength) {
		targets = new HashSet<>();
		visited = new HashSet<>();

		if (!(board[i][j].getInitial() == 'W' || board[i][j].isDoorway()))
			return;

		calcTargets_do(board[i][j], pathLength);
	}

	public void calcTargets(BoardCell startCell, int pathLength) {
		targets = new HashSet<>();
		visited = new HashSet<>();

		if (!(startCell.getInitial() == 'W' || startCell.isDoorway()))
			return;

		calcTargets_do(startCell, pathLength);
	}

	//Recursive call
	public void calcTargets_do(BoardCell startCell, int pathLength) {
		for (BoardCell cell : adjacencyList.get(startCell)) {
			if (cell.getInitial() == 'W' || cell.isDoorway()) {
				//visited.add(cell);

				if (pathLength == 1) {
					targets.add(cell);
				}
				else {
					calcTargets_do(cell, pathLength - 1);
				}
			}
		}
	}

	public Map<BoardCell, Set<BoardCell>> getAdjacencyList() {
		return adjacencyList;
	}

	public Set<BoardCell> getAdjList(int i, int j) {
		return adjacencyList.get(board[i][j]);
	}

	public Set<BoardCell> getTargets() {
		return targets;
	}

	public static Board getInstance() {
		return instance;
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumColumns() {
		return numColumns;
	}

	public BoardCell getCellAt(int i, int j) {
		return board[i][j];
	}

	public Map<Character, String> getLegend() {
		return legend;
	}

	public ArrayList<String> getRoomList() {
		return roomList;
	}
}
