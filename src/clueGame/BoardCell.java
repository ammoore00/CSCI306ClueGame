package clueGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class BoardCell {
	private int row;
	private int column;
	private DoorDirection doorDir;
	private char initial;
	
	private int drawX;
	private int drawY;
	
	private boolean shouldDisplayName;
	boolean isPlayerTarget;
	
	public BoardCell(int x, int y, char initial) {
		this.row = x;
		this.column = y;
		this.initial = initial;
		doorDir = DoorDirection.NONE;
	}

	public void draw(int x, int y, int gridSize, int borderSize, int doorThickness, Graphics g) {
		drawX = x;
		drawY = y;
		
		g.setColor(Color.GRAY);
		g.drawRect(x, y, gridSize, gridSize);
		
		g.setColor(getGridColorByInitial(getInitial()));
		//No border if it is a room
		int sizeForBorder = !isWalkway() ? 0 : borderSize;
		g.fillRect(x + sizeForBorder, y + sizeForBorder, gridSize - 2 * sizeForBorder, gridSize - 2 * sizeForBorder);
		
		//Special case for drawing door direction
		if (isDoorway()) {
			g.setColor(Color.BLACK);
			
			switch (getDoorDirection()) {
			case DOWN:
				g.fillRect(x, y + gridSize - doorThickness, gridSize, doorThickness);
				break;
			case LEFT:
				g.fillRect(x, y, doorThickness, gridSize);
				break;
			case RIGHT:
				g.fillRect(x + gridSize - doorThickness, y, doorThickness, gridSize);
				break;
			case UP:
				g.fillRect(x, y, gridSize, doorThickness);
				break;
			default:
				break;
			}
		}
	}
	
	private Color getGridColorByInitial(char initial) {
		if (isPlayerTarget) {
			return Color.CYAN;
		}
		
		switch (initial) {
		case 'W':
			return Color.WHITE;
		case 'T':
			return Color.RED;
		default:
			return Color.GRAY;
		}
	}

	public void drawName(Graphics g) {
		String name = Board.getInstance().getLegend().get(initial);
		
		g.setColor(Color.WHITE);
		g.setFont(g.getFont().deriveFont(Font.BOLD));
		g.drawString(name, drawX + GUIBoard.GRID_SIZE / 4, drawY + GUIBoard.GRID_SIZE / 3 * 2);
	}

	public boolean isDoorway() {
		return doorDir != null && doorDir != DoorDirection.NONE;
	}
	
	public boolean isWalkway() {
		return initial == 'W';
	}
	
	public void setDoorDirection(DoorDirection dir) {
		this.doorDir = dir;
	}

	public DoorDirection getDoorDirection() {
		return doorDir;
	}

	public char getInitial() {
		return initial;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public boolean isRoom() {
		return !isDoorway() && !isWalkway();
	}
	
	public boolean getShouldDisplayName() {
		return shouldDisplayName;
	}

	public void setShouldDisplayName(boolean shouldDisplayName) {
		this.shouldDisplayName = shouldDisplayName;
	}

	public void setIsPlayerTarget(boolean b) {
		isPlayerTarget = b;
	}
}