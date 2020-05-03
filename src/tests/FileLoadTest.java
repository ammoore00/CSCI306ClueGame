package tests;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;

import org.junit.Before;
import org.junit.Test;

import clueGame.BadConfigFormatException;
import clueGame.Board;
import clueGame.DoorDirection;

public class FileLoadTest {
	Board board;
	
	@Before
	public void initialize() throws FileNotFoundException, BadConfigFormatException {
		board = Board.getInstance();
		board.setConfigFiles("Layout.csv", "Rooms.txt");
		board.loadRoomConfig();
		board.loadBoardConfig();
	}
	
	@Test
	public void testLoad() {
		//9 rooms + storage + walkway = 11
		assertEquals(board.getLegend().size(), 11);
		assertEquals(board.getNumRows(), 21);
		assertEquals(board.getNumColumns(), 21);
	}
	
	@Test
	public void testDoors() {
		//Doors of each direction
		assert(board.getCellAt(5, 4).isDoorway());
		assertEquals(board.getCellAt(5, 4).getDoorDirection(), DoorDirection.DOWN);
		assert(board.getCellAt(14, 10).isDoorway());
		assertEquals(board.getCellAt(14, 10).getDoorDirection(), DoorDirection.LEFT);
		assert(board.getCellAt(18, 8).isDoorway());
		assertEquals(board.getCellAt(18, 8).getDoorDirection(), DoorDirection.UP);
		assert(board.getCellAt(15, 15).isDoorway());
		assertEquals(board.getCellAt(15, 15).getDoorDirection(), DoorDirection.RIGHT);
		
		//Number of doors
		int rows = board.getNumRows();
		int cols = board.getNumColumns();
		
		int numDoors = 0;
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				if (board.getCellAt(i,  j).isDoorway()) {
					numDoors++;
				}
			}
		}
		
		assertEquals(numDoors, 12);
	}
	
	@Test
	public void testCellInitials() {
		assert(board.getCellAt(20, 0).getInitial() == 'O');
		assert(board.getCellAt(11, 8).getInitial() == 'L');
		assert(board.getCellAt(12, 19).getInitial() == 'S');
	}
}