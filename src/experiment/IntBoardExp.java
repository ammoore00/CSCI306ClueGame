package experiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

public class IntBoardExp {
	private Map<BoardCellExp, Set<BoardCellExp>> adjacencyList = new HashMap<>();
	private Set<BoardCellExp> targets;
	private BoardCellExp[][] board;
	
	public IntBoardExp(int size) {
		board = new BoardCellExp[size][size];
		
		//Initializes board
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = new BoardCellExp(i, j);
			}
		}
		
		calcAdjacencies();
	}
	
	public void calcAdjacencies() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				Set<BoardCellExp> list = new HashSet<>();
				
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
	
	public void calcTargets(BoardCellExp startCell, int pathLength) {
		
	}
	
	public BoardCellExp getCell(int x, int y) {
		return board[x][y];
	}
	
	public Map<BoardCellExp, Set<BoardCellExp>> getAdjacencyList() {
		return adjacencyList;
	}
	
	public Set<BoardCellExp> getTargets() {
		return targets;
	}
}
