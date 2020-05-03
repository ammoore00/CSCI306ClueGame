package clueGame;

import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class DetectiveNotesDialog extends JDialog {
	Board board = Board.getInstance();
	
	private ArrayList<Player> players = board.getPlayers();
	private ArrayList<Card> weapons = board.getWeaponList();
	private ArrayList<String> rooms = board.getRoomList();
	
	public DetectiveNotesDialog() {
		setTitle("Detective Notes");
		setSize(800, 600);
		setLayout(new GridLayout(3, 2));
		
		add(peoplePanel());
		add(personGuessPanel());
		add(roomsPanel());
		add(roomGuessPanel());
		add(weaponsPanel());
		add(weaponGuessPanel());
	}
	
	private JPanel peoplePanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder (new EtchedBorder(), "People"));
		
		for (Player p : players) {
			JCheckBox checkBox = new JCheckBox(p.getName());
			panel.add(checkBox);
		}
		
		return panel;
	}
	
	private JPanel roomsPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		
		for (String r : rooms) {
			JCheckBox checkBox = new JCheckBox(r);
			panel.add(checkBox);
		}
		
		return panel;
	}
	
	private JPanel weaponsPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));
		
		for (Card w : weapons) {
			JCheckBox checkBox = new JCheckBox(w.getName());
			panel.add(checkBox);
		}
		
		return panel;
	}
	
	private JPanel personGuessPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Person Guess"));
		panel.setLayout(new GridLayout(1, 1));
		
		JComboBox<String> comboBox = new JComboBox<String>();
		
		for (Player p : players) {
			comboBox.addItem(p.getName());
		}
		
		panel.add(comboBox);
		
		return panel;
	}
	
	private JPanel roomGuessPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Room Guess"));
		panel.setLayout(new GridLayout(1, 1));
		
		JComboBox<String> comboBox = new JComboBox<String>();
		
		for (String r : rooms) {
			comboBox.addItem(r);
		}
		
		panel.add(comboBox);
		
		return panel;
	}
	
	private JPanel weaponGuessPanel() {
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder (new EtchedBorder(), "Weapon Guess"));
		panel.setLayout(new GridLayout(1, 1));
		
		JComboBox<String> comboBox = new JComboBox<String>();
		
		for (Card w : weapons) {
			comboBox.addItem(w.getName());
		}
		
		panel.add(comboBox);
		
		return panel;
	}
}
