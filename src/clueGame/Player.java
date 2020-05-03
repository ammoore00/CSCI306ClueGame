package clueGame;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Set;

public abstract class Player {
	private String name;
	private Color color;
	private Card card;
	protected Set<Card> hand = new HashSet<>();
	
	private int row;
	private int column;
	
	protected Board board = Board.getInstance();
	
	public Player(String name, Color color) {
		this.name = name;
		this.color = color;
	}
	
	public abstract Card disproveSuggestion(Solution suggestion);
	
	public abstract void makeMove(int roll);
	
	/*public void draw(Graphics g, int drawX, int drawY) {
		int size = GUIBoard.GRID_SIZE - GUIBoard.BORDER_SIZE;
		
		g.setColor(this.color);
		g.fillOval(drawX + GUIBoard.BORDER_SIZE, drawY + GUIBoard.BORDER_SIZE, size, size);
	}*/
	
	public boolean canDisprove(Solution suggestion) {
		Set<Card> solutionSet = suggestion.getAllCardsAsSet();
		
		for (Card c : hand) {
			if (solutionSet.contains(c))
				return true;
		}
		
		return false;
	}
	
	public void addCard(Card c) {
		hand.add(c);
	}
	
	public String getName() {
		return name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public Set<Card> getHand() {
		return hand;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	
	public void setCard(Card card) throws BadPlayerCardAssignmentException {
		if (card.getName() != this.getName())
			throw new BadPlayerCardAssignmentException("Card type must be PERSON");
		else
			this.card = card;
	}
	
	public Card getCard() {
		return card;
	}
	
	public void setLocation(int row, int column) {
		this.row = row;
		this.column = column;
	}
}
