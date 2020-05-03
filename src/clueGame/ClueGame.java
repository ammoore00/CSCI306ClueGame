package clueGame;

import java.util.Set;

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

	public static void handleSuggestionButton() {
		
	}

	public static void handleNextPlayerButton() {
		PlayerHuman human = board.getHuman();
		
		//If current turn is the human
		if (board.isHumanTurn()) {
			//If the human has moved, move on, else do nothing
			if (human.getHasMoved()) {
				human.setHasMoved(false);
				board.nextTurn();
			}
		}
		else {
			board.nextTurn();
		}
	}
	
	public static void handleDisprove(Set<Card> hand) {
		renderer.initializeDisproveDialog(hand);
	}
	
	public static void handleDisproveReturn(Card disproveCard) {
		PlayerHuman human = board.getHuman();
		
		human.disproveSuggestionReturn(disproveCard);
	}

	public static void handleBoardClick(BoardCell clickedCell) {
		if (clickedCell != null && clickedCell.isPlayerTarget) {
			PlayerHuman human = board.getHuman();
			human.setLocation(clickedCell.getRow(), clickedCell.getColumn());
			board.resetTargetCells();
			human.setHasMoved(true);
			renderer.refreshBoard();
			
			if (clickedCell.isRoom() || clickedCell.isDoorway()) {
				//Gets which room the player is now in
				String roomName = board.getLegend().get(clickedCell.getInitial());
				Card room = board.getCardByName(roomName);
				renderer.initializeMakeSuggestionDialog(room);
			}
		}
	}
}
