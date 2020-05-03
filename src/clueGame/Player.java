package clueGame;

import java.awt.Color;
import java.util.ArrayList;

public abstract class Player {
	private String name;
	private Color color;
	private ArrayList<Card> hand;
	private Card card;
	
	public Player(String name, Color color) {
		this.name = name;
		this.color = color;
		this.hand = new ArrayList<>();
	}
	
	public void addToHand(Card c) {
		hand.add(c);
	}
	
	public void setCard(Card card) throws BadPlayerCardAssignmentException {
		if (card.getType() != EnumCardType.PERSON)
			throw new BadPlayerCardAssignmentException("Player cards must be of type PERSON");
		this.card = card;
	}
	
	public Card getCard() {
		return card;
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public ArrayList<Card> getHand() {
		return hand;
	}
}
