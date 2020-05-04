package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
		else {
			MoveError error = new MoveError();
		}
	}
	
	public static class MoveError extends JDialog implements ActionListener {
		PlayerHuman player;
		Board board = Board.getInstance();
		
		public MoveError () {
			setTitle("Error");
			setSize(400, 200);
			setLayout(new GridLayout(2,1));
			
			player = board.getHuman();
			
			JLabel message = new JLabel("Invalid move location");
			JPanel error = new JPanel();
			error.add(message);
			
			JButton ok = new JButton("OK");
			ok.addActionListener(this);
			JPanel okPanel = new JPanel();
			okPanel.add(ok);
			
			this.add(error);
			this.add(okPanel);
			
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			this.setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			this.dispose();
		}
	}
}
