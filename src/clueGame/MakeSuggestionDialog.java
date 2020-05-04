package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MakeSuggestionDialog extends JDialog implements ActionListener {
	Board board = Board.getInstance();

	private ArrayList<Player> players = board.getPlayers();
	private ArrayList<Card> weapons = board.getWeaponList();
	
	private JComboBox personSelection;
	private JComboBox weaponSelection;
	private Card room;

	public MakeSuggestionDialog(Card room) {
		setTitle("Make a Guess");
		setSize(400, 600);
		setLayout(new GridLayout(4, 2));

		this.room = room;
		
		add(roomLabel());
		add(roomPanel(room));
		add(personLabel());
		add(personGuessPanel());
		add(weaponLabel());
		add(weaponGuessPanel());
		add(submitGuessPanel());
		add(cancelPanel());
	}

	private JPanel roomLabel() {
		JPanel roomPanel = new JPanel();
		JLabel label = new JLabel("Room");
		roomPanel.add(label);
		return roomPanel;
	}

	private JPanel personLabel() {
		JPanel personPanel = new JPanel();
		JLabel label = new JLabel("Person");
		personPanel.add(label);
		return personPanel;
	}

	private JPanel weaponLabel() {
		JPanel weaponPanel = new JPanel();
		JLabel label = new JLabel("Weapon");
		weaponPanel.add(label);
		return weaponPanel;
	}

	private JPanel roomPanel(Card room) {
		JPanel roomPanel = new JPanel();
		JTextField roomName = new JTextField(room.getName());
		roomName.setEditable(false);
		roomPanel.add(roomName);
		return roomPanel;
	}

	private JPanel personGuessPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Person Guess"));
		panel.setLayout(new GridLayout(1, 1));

		personSelection = new JComboBox<String>();

		for (Player p : players) {
			personSelection.addItem(p.getName());
		}

		panel.add(personSelection);

		return panel;
	}

	private JPanel weaponGuessPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Weapon Guess"));
		panel.setLayout(new GridLayout(1, 1));

		weaponSelection = new JComboBox<String>();

		for (Card w : weapons) {
			weaponSelection.addItem(w.getName());
		}

		panel.add(weaponSelection);

		return panel;
	}

	private JPanel submitGuessPanel() {
		JButton submitButton = new JButton("Submit");
		JPanel panel = new JPanel();

		//Adds listener to the button
		//Logic is held within ClueGame class to follow Single Principle philosophy
		submitButton.addActionListener(this);

		//setting the gridlayout here is just to make the button fit the entire cell, serves no actual function
		panel.setLayout(new GridLayout(1,1));
		panel.add(submitButton);
		return panel;
	}

	private JPanel cancelPanel() {
		JButton cancel = new JButton("Cancel");
		JPanel panel = new JPanel();

		//Adds listener to the button
		//Logic is held within ClueGame class to follow Single Principle philosophy
		cancel.addActionListener(this);

		//setting the gridlayout here is just to make the button fit the entire cell, serves no actual function
		panel.setLayout(new GridLayout(1,1));
		panel.add(cancel);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getActionCommand().equals("Submit")) {
			Solution suggestion = getSuggestionFromSelection();
			System.out.println(suggestion);
			if (suggestion.isComplete()) {
				ClueGame.handleSuggestionButton(suggestion);
				this.dispose();
			}
		}
		else
			this.dispose();
	}
	
	public Solution getSuggestionFromSelection() {
		Solution suggestion = new Solution();
		
		suggestion.setRoom(room);
		suggestion.setPerson(board.getCardByName((String) personSelection.getSelectedItem()));
		suggestion.setWeapon(board.getCardByName((String) weaponSelection.getSelectedItem()));
		
		return suggestion;
	}
}
