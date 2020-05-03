package clueGame;

public class BoardCell {
	private int row;
	private int column;
	private DoorDirection doorDir;
	private char initial;
	
	public BoardCell(int x, int y, char initial) {
		this.row = x;
		this.column = y;
		this.initial = initial;
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
}