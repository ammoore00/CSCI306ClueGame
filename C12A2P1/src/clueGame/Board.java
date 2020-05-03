package clueGame;

import java.util.Map;

public class Board {
	private static Board instance = new Board();
	
	private Board() {
		
	}

	public static Board getInstance() {
		return instance;
	}

	public void setConfigFiles(String string, String string2) {
		// TODO Auto-generated method stub
	}

	public void initialize() {
		// TODO Auto-generated method stub
		
	}

	public int getNumRows() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getNumColumns() {
		// TODO Auto-generated method stub
		return 0;
	}

	public BoardCell getCellAt(int i, int j) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Character, String> getLegend() {
		// TODO Auto-generated method stub
		return null;
	}

}
