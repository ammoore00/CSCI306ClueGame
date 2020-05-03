package clueGame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Board {
	private static Board instance = new Board();
	
	private Board() {
		
	}private Map<BoardCell, Set<BoardCell>> adjacencyList = new HashMap<>();
	private Set<BoardCell> targets;
	private Set<BoardCell> visited;
	private BoardCell[][] board;
	
	public void calcAdjacencies() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				Set<BoardCell> list = new HashSet<>();
				
				if (i > 0)
					list.add(board[i - 1][j]);
				if (j > 0)
					list.add(board[i][j - 1]);
				if (i < board.length - 1)
					list.add(board[i + 1][j]);
				if (j < board[0].length - 1)
					list.add(board[i][j + 1]);
				
				adjacencyList.put(board[i][j], list);
			}
		}
	}
	
	public void calcTargets(BoardCell startCell, int pathLength) {
		targets = new HashSet<>();
		visited = new HashSet<>();
		
		calcTargets_do(startCell, pathLength);
	}
	
	//Recursive call
	public void calcTargets_do(BoardCell startCell, int pathLength) {
		for (BoardCell cell : adjacencyList.get(startCell)) {
			if (!visited.contains(cell)) {
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
	
	public Set<BoardCell> getTargets() {
		return targets;
	}

	public static Board getInstance() {
		return instance;
	}

	public void setConfigFiles(String string, String string2) {
		// TODO Auto-generated method stub
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	public int getNumRows() {
		return 0;
	}

	public int getNumColumns() {
		// TODO Auto-generated method stub
		return 0;
	}

	public BoardCell getCellAt(int x, int y) {
		return board[x][y];
	}

	public Map<Character, String> getLegend() {
		// TODO Auto-generated method stub
		return null;
	}

}
