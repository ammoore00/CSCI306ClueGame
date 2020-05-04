/*
 * Team Members:
 * Rebecca Cowgill
 * Abigail Moore
 * 
 */

package clueGame;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUIBoard extends JPanel implements MouseListener{
	public static final int GRID_SIZE = 35;
	public static final int BORDER_SIZE = 1;
	public static final int DOOR_THICKNESS = 5;
	public static final int OFFSET = 50;
	
	private Board board;
	
	private ArrayList<BoardCell> cellsFlaggedForSecondPass = new ArrayList<>();;
	
	//private Graphics graphics = new Graphics2D();

	public GUIBoard() {
		board = Board.getInstance();
	}

	public void draw() {
		revalidate();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		cellsFlaggedForSecondPass.clear();
		
		//Draws the whole grid
		for (int i = 0; i < board.getNumRows(); i++) {
			for (int j = 0; j < board.getNumColumns(); j++) {
				BoardCell cell = board.getCellAt(j, i);
				
				int x = i * GRID_SIZE + OFFSET;
				int y = j * GRID_SIZE + OFFSET;
				
				cell.draw(x, y, GRID_SIZE, BORDER_SIZE, DOOR_THICKNESS, g);
				
				//Flags cells for a second pass to draw the name
				//Ensures the name does not get drawn over
				if (cell.getShouldDisplayName())
					cellsFlaggedForSecondPass.add(cell);
			}
		}
		
		for (BoardCell cell : cellsFlaggedForSecondPass) {
			cell.drawName(g);
		}
		
		ArrayList<Player> players = board.getPlayers();
		 for (Player p : players) {
			 int x = p.getColumn() * GRID_SIZE + OFFSET;
			 int y = p.getRow() * GRID_SIZE + OFFSET;
			 
			 p.draw(g, x, y);
		 }
	}
	
	//Converts display coordinates into board coordinates and returns the appropriate cell
	private BoardCell getCoordsAsBoardLoc(int x, int y) {
		int cellRow = (y - OFFSET) / GRID_SIZE;
		int cellColumn = (x - OFFSET) / GRID_SIZE;
		
		//Board panel is technically larger than the board displayed
		//This checks to make sure the click was actually on a cell
		if (cellRow < board.getNumRows() && cellColumn < board.getNumColumns() && cellRow > 0 && cellColumn > 0)
			return board.getCellAt(cellRow, cellColumn);
		return null;
	}

	@Override
	public void mousePressed(MouseEvent event) {
		int x  = event.getX();
		int y = event.getY();
		
		BoardCell clickedCell = getCoordsAsBoardLoc(x, y);
		
		ClueGame.handleBoardClick(clickedCell);
	}

	/*
	 * Unused mouse listener methods - here because of required override
	 */
	@Override
	public void mouseReleased(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}
}
