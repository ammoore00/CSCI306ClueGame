package tests;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class AdjacencyTests {
	private Board board;
	
	@Before
	public void initialize() {
		board = Board.getInstance();
		board.setConfigFiles("Layout.csv", "Rooms.txt");
		board.initialize();
	}
	
	@Test
	public void walkwayTest() {
		Set<BoardCell> adjList = board.getAdjList(7, 8);
		assertEquals(4, adjList.size());
		assert(adjList.contains(board.getCellAt(6, 8)));
		assert(adjList.contains(board.getCellAt(7, 7)));
		assert(adjList.contains(board.getCellAt(8, 8)));
		assert(adjList.contains(board.getCellAt(7, 9)));
	}
	
	@Test
	public void roomsTest() {
		Set<BoardCell> adjList = board.getAdjList(0, 0);
		assert(adjList.isEmpty());
	}
	
	@Test
	public void edgeTest() {
		Set<BoardCell> adjList = board.getAdjList(6, 0);
		assertEquals(1, adjList.size());
		assert(adjList.contains(board.getCellAt(6, 1)));
	}
	
	@Test
	public void nextToDoorwayTest() {
		Set<BoardCell> adjList = board.getAdjList(14, 17);
		assertEquals(3, adjList.size());
		assert(adjList.contains(board.getCellAt(13, 17)));
		assert(adjList.contains(board.getCellAt(14, 16)));
		assert(adjList.contains(board.getCellAt(14, 18)));
	}
	
	@Test
	public void inDoorTest() {
		Set<BoardCell> adjList = board.getAdjList(18, 8);
		assertEquals(1, adjList.size());
		assert(adjList.contains(board.getCellAt(17, 8)));
	}
}
