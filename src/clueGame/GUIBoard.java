/*
 * Team Members:
 * Rebecca Cowgill
 * Abigail Moore
 * 
 */

package clueGame;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

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

	@Override
	public void mousePressed(MouseEvent event) {
	}

	/*
	 * Unused mouse listener methods
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
