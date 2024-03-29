package clueGame;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

public class PlayerHuman extends Player {
	private Set<BoardCell> targets;
	private Renderer renderer = Renderer.getInstance();
	
	private boolean hasMoved = false;

	public PlayerHuman(String name, Color color) {
		super(name, color);
	}

	//Dummy method - will allow for player choice once GUI is added
	@Override
	public Card disproveSuggestion(Solution suggestion) {
		Set<Card> solutionSet = suggestion.getAllCardsAsSet();
		System.out.println(suggestion);
		
		for (Card c : hand) {
			System.out.println(c.getName());
			if (solutionSet.contains(c)) {
				System.out.println("Disprove");
				return c;
			}
		}
		
		return null;
	}
	
	public void disproveSuggestionChoices(Solution suggestion) {
		//Check if player can disprove
		boolean canDisprove = false;
		Set<Card> solutionSet = suggestion.getAllCardsAsSet();
		Set<Card> canDisproveSet = new HashSet<Card>();
		
		for (Card c : hand) {
			if (solutionSet.contains(c)) {
				canDisprove = true;
				canDisproveSet.add(c);
			}
		}
		
		if(canDisprove) ClueGame.handleDisprove(canDisproveSet);
	}
	
	public void disproveSuggestionReturn(Card disproveCard) {
		
	}

	@Override
	public void makeMove(int roll) {
		//Gets and draws possible targets on the board
		board.calcTargets(getRow(), getColumn(), roll);
		targets = board.getTargets();
		renderer.refreshBoard();
	}

	public boolean getHasMoved() {
		return hasMoved;
	}

	public void setHasMoved(boolean hasMoved) {
		this.hasMoved = hasMoved;
	}
}
