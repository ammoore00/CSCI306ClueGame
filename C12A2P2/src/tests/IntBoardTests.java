package tests;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import experiment.BoardCellExp;
import experiment.IntBoardExp;

public class IntBoardTests {
	IntBoardExp board;
	
	@Before
	public void beforeAll() {
		board = new IntBoardExp(4);
	}
	
	@Test
	public void testAdjacencies() {
		//Top Left
		BoardCellExp cell = board.getCell(0, 0);
		Map<BoardCellExp, Set<BoardCellExp>> adjacencyList = board.getAdjacencyList();
		assertTrue(adjacencyList.get(cell).size() == 2);
		assertTrue(adjacencyList.get(cell).contains(board.getCell(0, 1)));
		assertTrue(adjacencyList.get(cell).contains(board.getCell(1, 0)));
		
		//Bottom Right
		cell = board.getCell(3, 3);
		adjacencyList = board.getAdjacencyList();
		assertTrue(adjacencyList.get(cell).size() == 2);
		assertTrue(adjacencyList.get(cell).contains(board.getCell(2, 3)));
		assertTrue(adjacencyList.get(cell).contains(board.getCell(3, 2)));
		
		//Right edge
		cell = board.getCell(3, 1);
		adjacencyList = board.getAdjacencyList();
		assertTrue(adjacencyList.get(cell).size() == 3);
		assertTrue(adjacencyList.get(cell).contains(board.getCell(3, 0)));
		assertTrue(adjacencyList.get(cell).contains(board.getCell(2, 1)));
		assertTrue(adjacencyList.get(cell).contains(board.getCell(3, 2)));

		//Left edge
		cell = board.getCell(0, 2);
		adjacencyList = board.getAdjacencyList();
		assertTrue(adjacencyList.get(cell).size() == 3);
		assertTrue(adjacencyList.get(cell).contains(board.getCell(0, 3)));
		assertTrue(adjacencyList.get(cell).contains(board.getCell(1, 2)));
		assertTrue(adjacencyList.get(cell).contains(board.getCell(0, 1)));
		
		//Center
		cell = board.getCell(2, 2);
		adjacencyList = board.getAdjacencyList();
		assertTrue(adjacencyList.get(cell).size() == 4);
		assertTrue(adjacencyList.get(cell).contains(board.getCell(2, 1)));
		assertTrue(adjacencyList.get(cell).contains(board.getCell(1, 2)));
		assertTrue(adjacencyList.get(cell).contains(board.getCell(2, 3)));
		assertTrue(adjacencyList.get(cell).contains(board.getCell(3, 2)));
	}
	
	@Test
	public void testTargets() {
		//Top Left - 1 step
		BoardCellExp cell = board.getCell(0,  0);
		board.calcTargets(cell, 1);
		Set<BoardCellExp> targets = board.getTargets();
		assertTrue(targets.size() == 2);
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(0, 1)));
		
		//Bottom Right - 3 steps
		cell = board.getCell(3,  3);
		board.calcTargets(cell, 3);
		targets = board.getTargets();
		assertTrue(targets.size() == 6);
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertTrue(targets.contains(board.getCell(3, 2)));
	}
}
