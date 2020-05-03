package clueGame;

public class ClueGame {
	public static Renderer renderer;
	public static Board board;

	public static void main(String[] args) {
		//Singleton pattern
		board = Board.getInstance();
		board.setConfigFiles("Layout.csv", "Rooms.txt", "Players.txt", "Weapons.txt");
		board.initialize();

		//Singleton pattern
		renderer = Renderer.getInstance();
		renderer.initialize();
		
		board.nextTurn();
	}
}
