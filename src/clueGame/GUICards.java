/*
 * Team Members:
 * Rebecca Cowgill
 * Abigail Moore
 * 
 */

package clueGame;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class GUICards extends JPanel {
	private Board board = Board.getInstance();
	private Player player;
	
	public static final int CARD_WIDTH = 115; 
	public static final int CARD_HEIGHT = 130; 
	
	public GUICards() {
		player = board.getHuman();
		
		this.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT * 3));
		
		//Gets player's hand
		Set<Card> hand = player.getHand();
		int numberOfPeople = 0;
		int numberOfWeapons = 0;
		int numberOfRooms = 0;
		
		//Counts number of cards in player's hand
		for (Card c : hand) {
			switch (c.getType()) {
			case PERSON:
				numberOfPeople++;
				break;
			case WEAPON:
				numberOfWeapons++;
				break;
			case ROOM:
				numberOfRooms++;
				break;
			default:
				break;
			}
		}
		
		setLayout(new FlowLayout());
		
		if (numberOfPeople > 0) {
			add(cardPanel(EnumCardType.PERSON, "People"));
		}
		
		if (numberOfWeapons > 0) {
			add(cardPanel(EnumCardType.WEAPON, "Weapons"));
		}
		
		if (numberOfRooms > 0) {
			add(cardPanel(EnumCardType.ROOM, "Rooms"));
		}
		
	}
	
	public JPanel cardPanel(EnumCardType type, String title) {
		JPanel cards = new JPanel();
		
		ArrayList<JTextField> cardsTextFields = new ArrayList<JTextField>();
		for (Card c : player.getHand()) {
			if (c.getType() == type) {
				JTextField card = new JTextField(c.getName());
				card.setEditable(false);
				cardsTextFields.add(card);
			}
		}
		
		for (JTextField t : cardsTextFields) {
			cards.add(t);
		}
		
		cards.setPreferredSize(new Dimension(CARD_WIDTH, CARD_HEIGHT * cardsTextFields.size()));
		cards.setLayout(new GridLayout(cardsTextFields.size(), 1));
		cards.setBorder(new TitledBorder (new EtchedBorder(), title));
		
		return cards;
	}
}
