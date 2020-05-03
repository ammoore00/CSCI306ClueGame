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
		board.loadBoardConfig();
		board.loadRoomConfig();
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
		assert(board.getCellAt(4, 5).isDoorway());
		assertEquals(board.getCellAt(4, 5).getDoorDirection(), DoorDirection.DOWN);
		assert(board.getCellAt(10, 14).isDoorway());
		assertEquals(board.getCellAt(10, 14).getDoorDirection(), DoorDirection.LEFT);
		assert(board.getCellAt(8, 18).isDoorway());
		assertEquals(board.getCellAt(8, 18).getDoorDirection(), DoorDirection.UP);
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
		
		assertEquals(numDoors, 11);
	}
	
	@Test
	public void testCellInitials() {
		assert(board.getCellAt(0, 20).getInitial() == 'O');
		assert(board.getCellAt(8, 11).getInitial() == 'L');
		assert(board.getCellAt(19, 12).getInitial() == 'S');
	}
}