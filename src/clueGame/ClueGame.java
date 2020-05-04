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

		//Board handles updating control panel info
		board.nextTurn();
	}

	public static void handleSuggestionButton(Solution suggestion) {
		renderer.updateGuess(suggestion.toString());
		Card result = board.handleSuggestion(board.getHuman(), suggestion);
		renderer.updateDisprove(result);
	}

	public static void handleAccusationButton() {
		PlayerHuman human = board.getHuman();
		if (board.isHumanTurn()) {
			//If the human has moved, move on, else do nothing
			if (!human.getHasMoved()) {
				new MakeAccusationDialog();
			}
			else {
				new ErrorMessage("Accusations can only be made at the beginning of your turn");
			}
		}
		else {
			new ErrorMessage("Accusations can only be made at the beginning of your turn");
		}
	}

	public static void handleAccusationMade(Solution accusation) {
		boolean result = board.testAccusation(accusation);
		new AccusationResult(result);
	}
	
	public static void handleComputerAccusation(PlayerComputer computer, boolean result) {
		new ComputerAccusationResult(computer, result);
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
			ErrorMessage error = new ErrorMessage("Invalid move location");
		}
	}

	public static class ErrorMessage extends JDialog implements ActionListener {
		PlayerHuman player;
		Board board = Board.getInstance();

		public ErrorMessage(String errorText) {
			setTitle("Error");
			setSize(400, 200);
			setLayout(new GridLayout(2,1));

			player = board.getHuman();

			JLabel message = new JLabel(errorText);
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

	public static class AccusationResult extends JDialog implements ActionListener {
		PlayerHuman player;
		Board board = Board.getInstance();
		boolean win;

		public AccusationResult(boolean win) {
			setTitle("Accusation Result");
			setSize(400, 200);
			setLayout(new GridLayout(2,1));

			player = board.getHuman();
			this.win = win;

			JLabel message= new JLabel();

			if (win)
				message.setText("You won!");
			else
				message.setText("You lost!");
			JPanel accusation = new JPanel();
			accusation.add(message);

			JButton ok = new JButton("OK");
			ok.addActionListener(this);
			JPanel okPanel = new JPanel();
			okPanel.add(ok);

			this.add(accusation);
			this.add(okPanel);

			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			this.setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (win)
				renderer.getFrame().dispose();
			this.dispose();
		}
	}

	public static class ComputerAccusationResult extends JDialog implements ActionListener {
		Board board = Board.getInstance();
		boolean win;

		public ComputerAccusationResult(PlayerComputer computer, boolean win) {
			setTitle("Accusation Result");
			setSize(400, 200);
			setLayout(new GridLayout(2,1));

			JLabel message= new JLabel();

			if (win)
				message.setText(computer.getName() + " won!");
			else
				message.setText(computer.getName() + " lost!");
			JPanel accusation = new JPanel();
			accusation.add(message);

			JButton ok = new JButton("OK");
			ok.addActionListener(this);
			JPanel okPanel = new JPanel();
			okPanel.add(ok);

			this.add(accusation);
			this.add(okPanel);

			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

			this.setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (win)
				renderer.getFrame().dispose();
			this.dispose();
		}
	}
}
