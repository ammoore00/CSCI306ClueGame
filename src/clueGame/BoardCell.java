package clueGame;

public class BoardCell {
	private int x;
	private int y;
	private DoorDirection doorDir;
	private char initial;
	
	public BoardCell(int x, int y, char initial) {
		this.x = x;
		this.y = y;
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
}