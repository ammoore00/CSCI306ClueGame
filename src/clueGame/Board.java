package clueGame;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Board {
	private static Board instance = new Board();

	private String legendFile;
	private String boardFile;
	private String playerFile;
	private String weaponFile;

	private Map<Character, String> legend = new HashMap<>();
	private ArrayList<String> roomList = new ArrayList<>();
	private ArrayList<Card> weaponList = new ArrayList<>();
	
	private ArrayList<Player> playerList = new ArrayList<>();
	private ArrayList<PlayerComputer> computerList = new ArrayList<>();
	private PlayerHuman human;
	
	private ArrayList<Card> deck = new ArrayList<>();
	private ArrayList<Card> deckWithoutSolution = new ArrayList<>();
	
	private Solution solution;

	private Map<BoardCell, Set<BoardCell>> adjacencyList = new HashMap<>();
	private Set<BoardCell> targets;
	private BoardCell[][] board;
	
	private int turn;

	private int numRows;
	private int numColumns;

	private Board() {}

	public void initialize() {
		try {
			loadRoomConfig();
			loadBoardConfig();
			loadPersonConfig();
			loadWeaponConfig();
		} catch (FileNotFoundException | BadConfigFormatException e) {
			e.printStackTrace();
		}

		calcAdjacencies();
		setupDeck();
		solution = new Solution();
		deal();
	}

	public void setConfigFiles(String boardFile, String legendFile, String playerFile, String weaponFile) {
		this.boardFile = boardFile;
		this.legendFile = legendFile;
		this.playerFile = playerFile;
		this.weaponFile = weaponFile;
	}

	//Loads cards into memory
	public void loadPersonConfig() throws BadConfigFormatException, FileNotFoundException {
		String filepath = playerFile;
		
		//open new input stream to read in personConfigFile
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		String line;

		playerList = new ArrayList<>();
		computerList = new ArrayList<>();

		try {
			//Used for error tracking
			int count = 0;

			//Reads person config
			while ((line = reader.readLine()) != null) {
				String[] lineSplit = line.split(", ");
				String name = lineSplit[0];
				Color color = stringToColor(lineSplit[1]);

				//Assigns player type
				if (lineSplit[2].trim().equals("Human")) {
					human = new PlayerHuman(name, color);
					playerList.add(human);
					human.setCard(new Card(name, EnumCardType.PERSON));
				} else if (lineSplit[2].trim().equals("Computer")) {
					PlayerComputer computer = new PlayerComputer(name, color);
					playerList.add(computer);
					computerList.add(computer);
					computer.setCard(new Card(name, EnumCardType.PERSON));
				} else {
					reader.close();
					throw new BadConfigFormatException("Person entry has bad format on line " + count + ". 3rd entry must be \"Human\" or \"Computer\"");
				}

				count++;
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadPlayerCardAssignmentException e) {
			//If for some reason a player was assigned a card of the wrong type
			e.printStackTrace();
		}
	}

	//Loads cards into memory
	public void loadWeaponConfig() throws BadConfigFormatException, FileNotFoundException {
		String filepath = weaponFile;
		
		//open new input stream to read in weaponConfigFile
		BufferedReader reader = new BufferedReader(new FileReader(filepath));
		String name;

		weaponList = new ArrayList<>();

		try {
			//Reads weapon config
			while ((name = reader.readLine()) != null) {
				weaponList.add(new Card(name, EnumCardType.WEAPON));
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public void setupDeck() {
		for (String s : roomList) {
			deck.add(new Card(s, EnumCardType.ROOM));
		}
		
		for (Player p : playerList) {
			deck.add(p.getCard());
		}
		
		for (Card c : weaponList) {
			deck.add(c);
		}
	}
	
	public void deal() {
		ArrayList<Card> deckShuffle = shuffleDeck();
		
		//Makes solution
		for (int i = 0; i < deckShuffle.size(); i++) {
			if (deckShuffle.get(i).getType() == EnumCardType.PERSON && solution.getPerson() == null) {
				solution.setPerson(deckShuffle.get(i));
				deckShuffle.remove(i);
			}
			if (deckShuffle.get(i).getType() == EnumCardType.WEAPON && solution.getWeapon() == null) {
				solution.setWeapon(deckShuffle.get(i));
				deckShuffle.remove(i);
			}
			if (deckShuffle.get(i).getType() == EnumCardType.ROOM && solution.getRoom() == null) {
				solution.setRoom(deckShuffle.get(i));
				deckShuffle.remove(i);
			}
		}
		
		//Deals to players
		for (Player p : playerList) {
			p.addCard(deckShuffle.remove(0));
			p.addCard(deckShuffle.remove(0));
			p.addCard(deckShuffle.remove(0));
		}
	}
	
	public ArrayList<Card> shuffleDeck() {
		ArrayList<Card> deckShuffle = new ArrayList<>();
		ArrayList<Card> deckCopy = new ArrayList<>();
		Random rand = new Random();
		
		for (Card c : deck) {
			deckCopy.add(c);
		}
		
		//Shuffles deck by randomly removing cards from the deck copy and placing them into the shuffled deck
		while (!deckCopy.isEmpty()) {
			int index = rand.nextInt(deckCopy.size());
			deckShuffle.add(deckCopy.get(index));
			deckCopy.remove(index);
		}
		
		return deckShuffle;
	}
	
	//Calculates deck without solution
	//Precomputed to avoid unnecessary recalculation
	public void findDeckWithoutSolution() {
		deckWithoutSolution = new ArrayList<>();
		
		for (Card c : deck)
			if (!solution.contains(c))
				deckWithoutSolution.add(c);
	}
	
	/*
	 * Methods to assist with tests
	 */
	//Returns the deck minus the solution
	public ArrayList<Card> getDeckNoSolution() {
		return deckWithoutSolution;
	}
	
	//Returns the deck minus a specific solution
	public ArrayList<Card> getDeckNoSolution(Solution s) {
		ArrayList<Card> tempDeck = new ArrayList<>();
		
		for (Card c : deck)
			if (!s.contains(c))
				tempDeck.add(c);
		
		return tempDeck;
	}
	
	//Returns a random solution with no correct elements
	public Solution findIncorrectSolution() {
		Solution incorrect = new Solution();
		
		boolean solutionHasPerson = false;
		boolean solutionHasWeapon = false;
		boolean solutionHasRoom = false;

		Random rand = new Random();

		ArrayList<Card> tempDeck = new ArrayList<>();

		for (Card c : deckWithoutSolution) {
			tempDeck.add(c);
		}

		while (tempDeck.size() > 0) {
			int index = rand.nextInt(tempDeck.size());

			Card nextCard = tempDeck.get(index);

			if (!solutionHasPerson && nextCard.getType() == EnumCardType.PERSON) {
				incorrect.setPerson(nextCard);
				solutionHasPerson = true;
			} else if (!solutionHasWeapon && nextCard.getType() == EnumCardType.WEAPON) {
				incorrect.setWeapon(nextCard);
				solutionHasWeapon = true;
			} else if (!solutionHasRoom && nextCard.getType() == EnumCardType.ROOM) {
				incorrect.setRoom(nextCard);
				solutionHasRoom = true;
			} else if (incorrect.isComplete()) {
				break;
			}

			tempDeck.remove(index);
		}
		
		return incorrect;
	}
	
	//Returns the card from the first person able to disprove
	public Card handleSuggestion(Player suggester, Solution suggestion) {
		int index = turn + 1;
		
		while (index != turn) {
			Player player = playerList.get(index);
			//Returns card from first person able to disprove, unless only the suggester is left, where it returns null
			//Also prioritizes computer players over human by only returning the player's disprove if the loop gets back to the suggester without a computer disproving, and the human was not the suggester
			if (player.canDisprove(suggestion)) {
				if (!player.equals(suggester) && !(player instanceof PlayerHuman)) 
					return player.disproveSuggestion(suggestion);
			}
			
			index++;
			
			if (index >= playerList.size())
				index = 0;
		}
		
		//If nobody could disprove
		return null;
	}
	
	public boolean testAccusation(Solution accusation) {
		boolean isSamePerson = accusation.getPerson() == solution.getPerson();
		boolean isSameWeapon = accusation.getWeapon() == solution.getWeapon();
		boolean isSameRoom = accusation.getRoom() == solution.getRoom();

		return isSamePerson && isSameWeapon && isSameRoom;
	}
	
	public void calcAdjacencies() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				Set<BoardCell> list = new HashSet<>();

				//If it is a room then no adjacencies
				if (board[i][j].isDoorway() || board[i][j].isWalkway()) {
					//Bounds check and checks to only leave doorway in its direction
					if (i > 0 && (!board[i][j].isDoorway() || board[i][j].getDoorDirection() == DoorDirection.UP)) {
						BoardCell cell = board[i - 1][j];
						if (cell.isWalkway())
							list.add(cell);
						//Only enter a doorway in its direction
						else if (cell.isDoorway() && cell.getDoorDirection() == DoorDirection.DOWN)
							list.add(cell);
					}

					if (j > 0 && (!board[i][j].isDoorway() || board[i][j].getDoorDirection() == DoorDirection.LEFT)) {
						BoardCell cell = board[i][j - 1];
						if (cell.isWalkway())
							list.add(cell);
						else if (cell.isDoorway() && cell.getDoorDirection() == DoorDirection.RIGHT)
							list.add(cell);
					}

					if (i < board.length - 1 && (!board[i][j].isDoorway() || board[i][j].getDoorDirection() == DoorDirection.DOWN)) {
						BoardCell cell = board[i + 1][j];
						if (cell.isWalkway())
							list.add(cell);
						else if (cell.isDoorway() && cell.getDoorDirection() == DoorDirection.UP)
							list.add(cell);
					}

					if (j < board[0].length - 1 && (!board[i][j].isDoorway() || board[i][j].getDoorDirection() == DoorDirection.RIGHT)) {
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

		if (!(board[i][j].getInitial() == 'W' || board[i][j].isDoorway()))
			return;

		calcTargets_do(board[i][j], pathLength);
	}

	public void calcTargets(BoardCell startCell, int pathLength) {
		targets = new HashSet<>();

		if (!(startCell.getInitial() == 'W' || startCell.isDoorway()))
			return;

		calcTargets_do(startCell, pathLength);
	}

	//Recursive call
	public void calcTargets_do(BoardCell startCell, int pathLength) {
		for (BoardCell cell : adjacencyList.get(startCell)) {
			if (cell.getInitial() == 'W' || cell.isDoorway()) {
				if (pathLength == 1 || cell.isDoorway()) {
					targets.add(cell);
				}
				else {
					calcTargets_do(cell, pathLength - 1);
				}
			}
		}
	}

	//Converts string into Color object
	//Uses java.awt.Color constants, throws error if invalid color
	public Color stringToColor(String colorName) throws BadConfigFormatException {
		Color color;

		switch (colorName) {
		case "BLACK":
			color = Color.black;
			break;
		case "BLUE":
			color = Color.blue;
			break;
		case "CYAN":
			color = Color.cyan;
			break;
		case "DARK_GRAY":
			color = Color.darkGray;
			break;
		case "GRAY":
			color = Color.gray;
			break;
		case "GREEN":
			color = Color.green;
			break;
		case "LIGHT_GRAY":
			color = Color.lightGray;
			break;
		case "MAGENTA":
			color = Color.magenta;
			break;
		case "ORANGE":
			color = Color.orange;
			break;
		case "PINK":
			color = Color.pink;
			break;
		case "RED":
			color = Color.red;
			break;
		case "WHITE":
			color = Color.white;
			break;
		case "YELLOW":
			color = Color.yellow;
			break;
		default:
			throw new BadConfigFormatException("Invalid color \"" + colorName + "\"");
		}

		return color;
	}

	public Card getCardByName(String name) {
		for (Card c : deck)
			if (c.getName().equals(name))
				return c;
		return null;
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
	
	public ArrayList<Player> getPlayers() {
		return playerList;
	}
	
	public ArrayList<Card> getWeaponList() {
		return weaponList;
	}
	
	public ArrayList<Card> getDeck() {
		return deck;
	}

	public Solution getSolution() {
		return solution;
	}
	
	public Player getPlayer(String name) {
		for (Player p : playerList)
			if (p.getName().contentEquals(name))
				return p;
		return null;
	}
	
	public Player getPlayer(Card card) {
		for (Player p : playerList)
			if (p.getName().contentEquals(card.getName()))
				return p;
		return null;
	}
	
	public void setTurn(int turn) {
		this.turn = turn;
	}
}
