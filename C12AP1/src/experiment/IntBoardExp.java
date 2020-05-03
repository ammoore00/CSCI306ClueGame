package experiment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;

public class IntBoardExp {
	private Map<BoardCellExp, ArrayList<BoardCellExp>> adjacencyList = new HashMap<>();
	private Set<BoardCellExp> targets;
	private BoardCellExp[][] board;
	
	public IntBoardExp(int size) {
		board = new BoardCellExp[size][size];
		calcAdjacencies();
	}
	
	public void calcAdjacencies() {
		
	}
	
	public void calcTargets(BoardCellExp startCell, int pathLength) {
		
	}
	
	public BoardCellExp getCell(int x, int y) {
		return board[y][x];
	}
	
	public Map<BoardCellExp, ArrayList<BoardCellExp>> getAdjacencyList() {
		return adjacencyList;
	}
	
	public Set<BoardCellExp> getTargets() {
		return targets;
	}
}
