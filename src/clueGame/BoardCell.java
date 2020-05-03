package clueGame;

public class BoardCell {
	private int x;
	private int y;
	
	private char initial;
	
	public BoardCell(int x, int y, char initial) {
		this.x = x;
		this.y = y;
		this.initial = initial;
	}

	public boolean isDoorway() {
		return false;
	}

	public Object[] getDoorDirection() {
		return null;
	}

	public char getInitial() {
		return initial;
	}
}