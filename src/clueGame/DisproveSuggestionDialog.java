package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class DisproveSuggestionDialog extends JDialog {
	Renderer renderer = Renderer.getInstance();
	Board board = Board.getInstance();
	JComboBox<String> comboBox;
	
	public DisproveSuggestionDialog(Set<Card> hand) {
		setTitle("Disprove Suggestion");
		setSize(500, 500);
		setLayout(new GridLayout(2, 1));
		
		add(chooseCard(hand));
		
	}
	
	private JPanel chooseCard(Set<Card> hand) {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Select Card"));
		panel.setLayout(new GridLayout(1, 1));
		
		comboBox = new JComboBox<String>();
		
		for (Card c : hand) {
			comboBox.addItem(c.getName());
		}
		
		panel.add(comboBox);
		
		return panel;
	}
	
	private JPanel submitButton() {
		JButton submit = new JButton("Submit");
		JPanel panel = new JPanel();
		
		//Adds listener to the button
		//Logic is held within ClueGame class to follow Single Principle philosophy
		submit.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	Card disproveCard = board.getCardByName((String) comboBox.getSelectedItem());
		        renderer.returnPlayerDisprove(disproveCard);
		    }
		});
		
		//setting the gridlayout here is just to make the button fit the entire cell, serves no actual function
		panel.setLayout(new GridLayout(1,1));
		panel.add(submit);
		return panel;
	}
}
