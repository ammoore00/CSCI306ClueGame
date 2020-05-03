package clueGame;

import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MakeSuggestionDialog extends JDialog {
	public MakeSuggestionDialog(Card room) {
		setTitle("Make a Guess");
		setSize(200, 400);
		setLayout(new GridLayout(1, 2));
		
		JPanel labels = new JPanel();
		labels.add(roomLabel());
		labels.add(personLabel());
		labels.add(weaponLabel());
		add(labels);
		
		JPanel selections = new JPanel();
		selections.add(roomPanel(room));
		add(selections);
	}
	
	private JPanel roomLabel() {
		JPanel roomPanel = new JPanel();
		JLabel label = new JLabel("Room");
		roomPanel.add(label);
		return roomPanel;
	}
	
	private JPanel personLabel() {
		JPanel personPanel = new JPanel();
		JLabel label = new JLabel("Room");
		personPanel.add(label);
		return personPanel;
	}
	
	private JPanel weaponLabel() {
		JPanel weaponPanel = new JPanel();
		JLabel label = new JLabel("Room");
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
}
