package tests;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class AdjacencyAndTargetTests {
	private Board board;
	
	@Before
	public void initialize() {
		board = Board.getInstance();
		board.setConfigFiles("Layout.csv", "Rooms.txt", "Players.txt", "Weapons.txt");
		board.initialize();
	}
	
	@Test
	public void adjwalkwayTest() {
		Set<BoardCell> adjList = board.getAdjList(7, 8);
		assertEquals(4, adjList.size());
		assert(adjList.contains(board.getCellAt(6, 8)));
		assert(adjList.contains(board.getCellAt(7, 7)));
		assert(adjList.contains(board.getCellAt(8, 8)));
		assert(adjList.contains(board.getCellAt(7, 9)));
	}
	
	@Test
	public void adjroomsTest() {
		Set<BoardCell> adjList = board.getAdjList(0, 0);
		assert(adjList.isEmpty());
	}
	
	@Test
	public void adjedgeTest() {
		Set<BoardCell> adjList = board.getAdjList(6, 0);
		assertEquals(1, adjList.size());
		assert(adjList.contains(board.getCellAt(6, 1)));
	}
	
	@Test
	public void adjnextToDoorwayTest() {
		Set<BoardCell> adjList = board.getAdjList(14, 17);
		assertEquals(3, adjList.size());
		assert(adjList.contains(board.getCellAt(13, 17)));
		assert(adjList.contains(board.getCellAt(14, 16)));
		assert(adjList.contains(board.getCellAt(14, 18)));
	}
	
	@Test
	public void adjinDoorTest() {
		Set<BoardCell> adjList = board.getAdjList(18, 8);
		assertEquals(1, adjList.size());
		assert(adjList.contains(board.getCellAt(17, 8)));
	}
	
	@Test
	public void targetTests() {
		//1 step
		board.calcTargets(7, 8, 1);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(4, targets.size());
		assert(targets.contains(board.getCellAt(6, 8)));
		assert(targets.contains(board.getCellAt(7, 7)));
		assert(targets.contains(board.getCellAt(8, 8)));
		assert(targets.contains(board.getCellAt(7, 9)));
		
		//2 steps
		board.calcTargets(7, 8, 2);
		targets = board.getTargets();
		assertEquals(6, targets.size());
		assert(targets.contains(board.getCellAt(7, 8)));
		assert(targets.contains(board.getCellAt(7, 6)));
		assert(targets.contains(board.getCellAt(6, 7)));
		assert(targets.contains(board.getCellAt(8, 7)));
		assert(targets.contains(board.getCellAt(6, 9)));
		assert(targets.contains(board.getCellAt(8, 9)));
		
		//4 steps
		board.calcTargets(6, 0, 4);
		targets = board.getTargets();
		assertEquals(3, targets.size());
		assert(targets.contains(board.getCellAt(6, 0)));
		assert(targets.contains(board.getCellAt(6, 2)));
		assert(targets.contains(board.getCellAt(6, 4)));
	}
	
	@Test
	public void targetRoomTests() {
		//From room
		board.calcTargets(9, 5, 2);
		Set<BoardCell> targets = board.getTargets();
		assertEquals(3, targets.size());
		assert(targets.contains(board.getCellAt(9, 5)));
		assert(targets.contains(board.getCellAt(7, 5)));
		assert(targets.contains(board.getCellAt(8, 6)));
		
		//Into room
		board.calcTargets(8, 5, 1);
		targets = board.getTargets();
		assertEquals(3, targets.size());
		assert(targets.contains(board.getCellAt(9, 5)));
		assert(targets.contains(board.getCellAt(7, 5)));
		assert(targets.contains(board.getCellAt(8, 6)));
	}
}
