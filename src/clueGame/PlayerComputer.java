package clueGame;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

public class PlayerComputer extends Player {
	private String lastRoomVisited;
	private String currentRoom;
	private Set<Card> seenCards;
	private Set<Card> shownCards;
	
	Renderer renderer = Renderer.getInstance();
	
	private boolean readyForAccusation = false;
	private Solution accusation;

	public PlayerComputer(String name, Color color) {
		super(name, color);
		seenCards = new HashSet<Card>();
		shownCards = new HashSet<Card>();
	}
	
	@Override
	public void addCard(Card c) {
		super.addCard(c);
		seenCards.add(c);
	}

	@Override
	public void makeMove(int pathlength) {
		//Make an accusation at the start of its turn
		if (readyForAccusation) {
			//Tests accusation then passes to ClueGame for handling
			boolean result = board.testAccusation(accusation);
			ClueGame.handleComputerAccusation(this, result);
			readyForAccusation = false;
		}
		
		BoardCell target = chooseTarget(board.getCellAt(getRow(), getColumn()), pathlength);
		setLocation(target.getRow(), target.getColumn());
		//Reset before redraw to avoid drawing the targets for computers
		board.resetTargetCells();
		renderer.refreshBoard();
		
		//Sets the current room
		if (board.getCellAt(this.getRow(), this.getColumn()).isDoorway()) {
			currentRoom = board.getLegend().get(board.getCellAt(this.getRow(), this.getColumn()).getInitial());
		}
		else {
			currentRoom = null;
		}
		
		//Makes a suggestion if it is in a room
		if (currentRoom != null) {
			Solution suggestion = createSuggestion();
			renderer.updateGuess(suggestion.toString());
			
			//Gets player in the suggestion and moves them to the room
			Player suggestedPlayer = board.getPlayer(suggestion.getPerson());
			suggestedPlayer.setLocation(this.getRow(), this.getColumn());
			
			Card result = board.handleSuggestion(this, suggestion);
			renderer.updateDisprove(result);
			
			//If no one disproved then it is ready for accusation and remembers that suggestion (Computer cannot make suggestion with card in its hand)
			if (result == null) {
				readyForAccusation = true;
				accusation = suggestion;
			}
			else {
				seenCards.add(result);
			}
		}
	}
	
	public BoardCell chooseTarget(BoardCell currLocation, int length) {
		BoardCell target = null;
		boolean mustChooseDoor = false;
		
		board.calcTargets(currLocation.getRow(), currLocation.getColumn(), length);
		Set<BoardCell> targets = board.getTargets();
		
		//Finds doorways in target list
		for (BoardCell b : targets) {
			if (b.isDoorway()) {
				char c = b.getInitial();
				String room = board.getLegend().get(c);
				
				//Must go into a room if it was not just there
				if (!room.equals(lastRoomVisited)) {
					mustChooseDoor = true;
					target = b;
				}
			}
		}
		
		//Selects a random target if it does not need to go into a room
		if (!mustChooseDoor) {
			Random rand = new Random();
			int index = rand.nextInt(targets.size());
			Iterator<BoardCell> iter = targets.iterator();
			for (int i = 0; i < index; i++) {
			    iter.next();
			}
			target = iter.next();
		}
		
		return target;
	}
	
	public Solution createSuggestion() {
		Solution solution = new Solution();
		ArrayList<Card> deck = board.getDeck();
		
		Random rand = new Random();
		
		boolean solutionHasPerson = false;
		boolean solutionHasWeapon = false;
		
		//While solution incomplete
		while ((solution.getPerson() == null || solution.getWeapon() == null) && deck.size() > 0) {
			int index = rand.nextInt(deck.size());
			
			//Randomly chooses a card to add for person or weapon
			Card nextCard = deck.get(index);
			
			if (seenCards.contains(nextCard)) {
				continue;
			}
			
			if (!solutionHasPerson && nextCard.getType() == EnumCardType.PERSON) {
				solution.setPerson(nextCard);
				solutionHasPerson = true;
			} else if (!solutionHasWeapon && nextCard.getType() == EnumCardType.WEAPON) {
				solution.setWeapon(nextCard);
				solutionHasWeapon = true;
			}
		}
		
		//Ensures it can only choose its current room
		solution.setRoom(board.getCardByName(currentRoom));
		
		return solution;
	}
	
	@Override
	public Card disproveSuggestion(Solution suggestion) {
		ArrayList<Card> suggestionCards = new ArrayList<>();
		suggestionCards.add(suggestion.getPerson());
		suggestionCards.add(suggestion.getWeapon());
		suggestionCards.add(suggestion.getRoom());
		
		//Checks if any of the cards have been shown before. If so, the computer will try to keep showing cards it has already shown
		for (Card c : suggestionCards) {
			if (shownCards.contains(c))
				return c;
		}
		
		//If no valid cards have been shown before it will pick the first card that is valid
		for (Card c : suggestionCards) {
			if (hand.contains(c)) {
				shownCards.add(c);
				return c;
			}
		}
		
		return null;
	}

	public String getLastRoomVisited() {
		return lastRoomVisited;
	}

	public void setLastRoomVisited(String lastRoomVisited) {
		this.lastRoomVisited = lastRoomVisited;
	}

	public Set<Card> getSeenCards() {
		return seenCards;
	}

	public void setSeenCards(Set<Card> seenCards) {
		this.seenCards = seenCards;
	}

	public String getCurrentRoom() {
		return currentRoom;
	}

	public void setCurrentRoom(String currentRoom) {
		this.currentRoom = currentRoom;
	}
}
