/*
 * Team Members:
 * Rebecca Cowgill
 * Abigail Moore
 * 
 */

package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class Renderer {
	private static Renderer instance = new Renderer();
	
	public static final int DEFAULT_X_RESOLUTION = 1000;
	public static final int DEFAULT_Y_RESOLUTION = 1000;
	
	private JFrame frame;
	private JMenuBar menuBar;
	private Board board = Board.getInstance();

	private GUIBoard guiBoard;
	private GUIControlPanel guiControlPanel;
	
	//Singleton pattern
	private Renderer() {
		
	}
	
	public static Renderer getInstance() {
		return instance;
	}
	
	public void initialize() {
		//Create new frame to hold panels
		frame = new JFrame();
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Clue Game");
		frame.setResizable(false);
		frame.setSize(DEFAULT_X_RESOLUTION, DEFAULT_Y_RESOLUTION);
		
		//add menuBar to JFrame
		initializeMenuBar();
		initializeControlGUI();
		initializeBoardGUI();
		initializeCardsGUI();
		
		//create new welcome message
		WelcomeMessage message = new WelcomeMessage();
	}
	
	public void updateTurn(String turn) {
		guiControlPanel.updateTurn(turn);
	}
	
	public void updateRoll(int roll) {
		guiControlPanel.updateRoll(roll);
	}
	
	public void updateGuess(String guess) {
		guiControlPanel.updateGuess(guess);
	}
	
	private void initializeMenuBar() {
		//create menu object
		GUIMenu menu = new GUIMenu();
		//create menuBar
		menuBar = new JMenuBar();
		//add menu object to menuBar
		menuBar.add(menu.createFileMenu());
		frame.setJMenuBar(menuBar);
	}
	
	private void initializeControlGUI() {
		guiControlPanel = new GUIControlPanel();
		frame.add(guiControlPanel, BorderLayout.PAGE_END);
	}
	
	private void initializeBoardGUI() {
		guiBoard = new GUIBoard();
		guiBoard.addMouseListener(guiBoard);
		frame.add(guiBoard);
	}
	
	private void initializeCardsGUI() {
		GUICards gui = new GUICards();
		frame.add(gui, BorderLayout.EAST);
	}
	
	public void initializeMakeSuggestionDialog(Card room) {
		MakeSuggestionDialog dialog = new MakeSuggestionDialog(room);
		dialog.setVisible(true);
	}
	
	public void initializeDisproveDialog(Set<Card> hand) {
		DisproveSuggestionDialog dialog = new DisproveSuggestionDialog(hand);
		dialog.setVisible(true);
	}
	
	public void returnPlayerDisprove(Card disproveCard) {
		ClueGame.handleDisproveReturn(disproveCard);
	}
	
	public JFrame getFrame() {
		return frame;
	}
	
	public void refreshBoard() {
		guiBoard.repaint();
	}
	
	public class WelcomeMessage extends JDialog implements ActionListener {
		PlayerHuman player;
		Board board = Board.getInstance();
		
		public WelcomeMessage () {
			setTitle("Welcome to Clue");
			setSize(400, 200);
			setLayout(new GridLayout(2,1));
			
			player = board.getHuman();
			
			JLabel announcePlayer = new JLabel("You are " + player.getName() + " , press Next Player to begin play");
			JPanel announcement = new JPanel();
			announcement.add(announcePlayer);
			
			JButton ok = new JButton("OK");
			ok.addActionListener(this);
			JPanel okPanel = new JPanel();
			okPanel.add(ok);
			
			this.add(announcement);
			this.add(okPanel);
			
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			
			this.setVisible(true);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			//Makes frame containing the game show up when the welcome message is dismissed
			frame.setVisible(true);
			this.dispose();
		}
	}
}
