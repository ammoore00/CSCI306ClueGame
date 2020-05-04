/*
 * Team Members:
 * Rebecca Cowgill
 * Abigail Moore
 * 
 */

package clueGame;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GUIControlPanel extends JPanel {
	//the following are all the text fields that will be used
	private JTextField whoseTurn;
	private JTextField dieRoll;
	private JTextArea guess;
	private JTextField response;
	
	JPanel whoseTurnPanel;
	JPanel rollDiePanel;
	JPanel guessPanel;
	JPanel guessResultPanel;
	JPanel accusationPanel;
	
	public GUIControlPanel() {
		setLayout(new GridLayout(2, 3));
		
		//Creates all panels and adds them to the larger panel
		whoseTurnPanel = whoseTurnPanel();
		add(whoseTurnPanel);
		
		JPanel panel = nextPlayerButtonPanel();
		add(panel);
		
		accusationPanel = makeAccusationButtonPanel();
		add(accusationPanel);
		
		rollDiePanel = rollDiePanel();
		add(rollDiePanel);
		
		guessPanel = guessPanel();
		add(guessPanel);
		
		guessResultPanel = guessResultPanel();
		add(guessResultPanel);
	}
	
	public void updateTurn(String turn) {
		Component[] components = whoseTurnPanel.getComponents();
		for (Component c : components) {
			if (c instanceof JTextField) {
				((JTextField) c).setText(turn);
			}
		}
	}
	
	public void updateRoll(int roll) {
		Component[] components = rollDiePanel.getComponents();
		for (Component c : components) {
			if (c instanceof JTextField) {
				((JTextField) c).setText(Integer.toString(roll));
			}
		}
	}
	
	public void updateGuess(String guess) {
		Component[] components = guessPanel.getComponents();
		for (Component c : components) {
			if (c instanceof JTextArea) {
				((JTextArea) c).setText(guess);
			}
		}
	}
	
	public void updateDisprove(Card result) {
		Component[] components = guessResultPanel.getComponents();
		for (Component c : components) {
			if (c instanceof JTextField) {
				((JTextField) c).setText(result == null ? "No new clues" : result.getName());
			}
		}
	}
	
	private JPanel whoseTurnPanel() {
		JPanel panel = new JPanel();
		JLabel whoseTurnLabel = new JLabel("Whose Turn?");
		whoseTurn = new JTextField(15);
		//user shouldn't actually be able to edit this field
			//changed with next player button
		whoseTurn.setEditable(false);
		panel.add(whoseTurnLabel);
		panel.add(whoseTurn);
		return panel;
	}
	
	private JPanel nextPlayerButtonPanel() {
		JButton nextPlayer = new JButton("Next player");
		JPanel panel = new JPanel();
		
		//Adds listener to the button
		//Logic is held within ClueGame class to follow Single Principle philosophy
		nextPlayer.addActionListener(new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		        ClueGame.handleNextPlayerButton();
		    }
		});
		
		//setting the gridlayout here is just to make the button fit the entire cell, serves no actual function
		panel.setLayout(new GridLayout(1,1));
		panel.add(nextPlayer);
		return panel;
	}
	
	private JPanel makeAccusationButtonPanel() {
		JButton makeAccusation = new JButton("Make an accusation");
		JPanel panel = new JPanel();
		
		//Adds listener to the button
		//Logic is held within ClueGame class to follow Single Principle philosophy
		makeAccusation.addActionListener(new ActionListener()
		{
		    @Override
		    public void actionPerformed(ActionEvent e)
		    {
		        ClueGame.handleAccusationButton();
		    }
		});
		
		//setting the gridlayout here is just to make the button fit the entire cell, serves no actual function
		panel.setLayout(new GridLayout(1,1));
		panel.add(makeAccusation);
		return panel;
	}
	
	private JPanel rollDiePanel() {
		JPanel panel = new JPanel();
		JLabel rollLabel = new JLabel("Roll");
		dieRoll = new JTextField(5);
		dieRoll.setEditable(false);
		panel.add(rollLabel);
		panel.add(dieRoll);
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Die"));
		return panel;
	}
	
	private JPanel guessPanel() {
		JPanel panel = new JPanel();
		JLabel guessLabel = new JLabel("Guess");
		guess = new JTextArea(3, 20);
		guess.setEditable(false);
		panel.add(guessLabel);
		panel.add(guess);
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Guess"));
		return panel;
	}
	
	private JPanel guessResultPanel() {
		JPanel panel = new JPanel();
		JLabel responseLabel = new JLabel("Response");
		response = new JTextField(10);
		response.setEditable(false);
		panel.add(responseLabel);
		panel.add(response);
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Guess Result"));
		return panel;
	}
}
