package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.PlayerComputer;
import clueGame.PlayerHuman;
import clueGame.Solution;
import clueGame.Card;
import clueGame.Player;

public class GameActionTests {
	private static Board board;
	
	@BeforeClass
	public static void setUp() {
		board = Board.getInstance();
		board.setConfigFiles("Layout.csv", "Rooms.txt", "Players.txt", "Weapons.txt");
		board.initialize();
		board.setTurn(0);
	}

	@Test
	public void targetsTest() {
		PlayerComputer computer = (PlayerComputer) board.getPlayers().get(1);
		//no possible rooms to enter
		BoardCell cell = computer.chooseTarget(board.getCellAt(7, 8), 1);
		Set<BoardCell> validTargets = new HashSet<>();
		validTargets.add(board.getCellAt(7, 7));
		validTargets.add(board.getCellAt(8, 8));
		validTargets.add(board.getCellAt(7, 9));
		validTargets.add(board.getCellAt(6, 8));
		assertTrue(validTargets.contains(cell));
		
		//must enter room
		cell = computer.chooseTarget(board.getCellAt(17, 8), 2);
		assertTrue(cell == board.getCellAt(18, 8));
		
		//just left room
		computer.setLastRoomVisited("Bridge");
		cell = computer.chooseTarget(board.getCellAt(17, 8), 1);
		validTargets.clear();
		validTargets.add(board.getCellAt(18, 8));
		validTargets.add(board.getCellAt(16, 8));
		validTargets.add(board.getCellAt(17, 7));
		validTargets.add(board.getCellAt(17, 9));
		assertTrue(validTargets.contains(cell));
	}
	
	@Test
	public void testAccusation() {
		Solution solution = board.getSolution();
		ArrayList<Card> deck = board.getDeck();
		Card wrongPerson = null;
		Card wrongWeapon = null;
		Card wrongRoom = null;
		
		//this switch statement gets cards for person/room/weapon that are not in solution
		for (Card c : deck) {
			switch (c.getType()) {
			case PERSON:
				if (solution.getPerson() != c && wrongPerson == null) {
					wrongPerson = c;
				}
				break;
			case WEAPON:
				if (solution.getWeapon() != c && wrongWeapon == null) {
					wrongWeapon = c;
				}
				break;
			case ROOM:
				if (solution.getRoom() != c && wrongRoom == null) {
					wrongRoom = c;
				}
				break;
			}
		}
		
		
		
		//testing solution is true
		assertTrue(board.testAccusation(solution));
		
		//wrong person
		Solution accusation = new Solution();
		accusation.setPerson(wrongPerson);
		accusation.setWeapon(solution.getWeapon());
		accusation.setRoom(solution.getRoom());
		assertFalse(board.testAccusation(accusation));
		
		//wrong weapon
		accusation = new Solution();
		accusation.setPerson(solution.getPerson());
		accusation.setWeapon(wrongWeapon);
		accusation.setRoom(solution.getRoom());
		assertFalse(board.testAccusation(accusation));
		
		//wrong room
		accusation = new Solution();
		accusation.setPerson(solution.getPerson());
		accusation.setWeapon(solution.getWeapon());
		accusation.setRoom(wrongRoom);
		assertFalse(board.testAccusation(accusation));
	}
	
	@Test
	public void testCreateSuggestion() {
		PlayerComputer computer = (PlayerComputer) board.getPlayers().get(1);
		
		//Makes sure it selects the room it is in
		computer.setCurrentRoom("Bridge");
		Solution suggestion = computer.createSuggestion();
		assertEquals(suggestion.getRoom().getName(), "Bridge");
		Solution solution = board.getSolution();
		ArrayList<Card> deck = board.getDeck();

		//this switch statement gets cards for person/room/weapon that are not in solution - ensures only one person/weapon left
		for (Card c : deck) {
			switch (c.getType()) {
			case PERSON:
				if (solution.getPerson() != c) {
					computer.getSeenCards().add(c);
				}
				break;
			case WEAPON:
				if (solution.getWeapon() != c) {
					computer.getSeenCards().add(c);
				}
				break;
			default:
				break;
			}
		}
		
		//Makes sure that it selects the only weapon and person that are available
		suggestion = computer.createSuggestion();
		assertEquals(suggestion.getPerson(), solution.getPerson());
		assertEquals(suggestion.getWeapon(), solution.getWeapon());
		
		boolean gotPerson = false;
		Card person = null;
		boolean gotWeapon = false;
		Card weapon = null;
		
		computer.getSeenCards().clear();
		
		//this switch statement gets cards for person/room/weapon that are not in solution
		for (Card c : deck) {
			switch (c.getType()) {
			case PERSON:
				if (solution.getPerson() != c && !gotPerson) {
					computer.getSeenCards().add(c);
					gotPerson = true;
					person = c;
				}
				break;
			case WEAPON:
				if (solution.getWeapon() != c && !gotWeapon) {
					computer.getSeenCards().add(c);
					gotWeapon = true;
					weapon = c;
				}
				break;
			default:
				break;
			}
		}
		
		suggestion = computer.createSuggestion();
		assertFalse(suggestion.getPerson() == person);
		assertFalse(suggestion.getWeapon() == weapon);
	}
	
	@Test
	public void testDisproveSuggestion() {
		Solution suggestion = board.findIncorrectSolution();
		Set<Card> cardSet = suggestion.getAllCardsAsSet();
		
		PlayerComputer computer = new PlayerComputer("X", Color.black);
		
		for (Card c : cardSet) {
			computer.getHand().add(c);
		}
		
		Card disproveCard = computer.disproveSuggestion(suggestion);
		assertTrue(cardSet.contains(disproveCard));
	}
	
	@Test
	public void testHandleSuggestion() {
		Solution solution = board.getSolution();
		PlayerHuman human = (PlayerHuman) board.getPlayers().get(0);
		PlayerComputer computer = (PlayerComputer) board.getPlayers().get(1);
		
		//Passes solution since no one can disprove
		assertEquals(null, board.handleSuggestion(computer, solution));
		
		//Tests suggestion only computer accuser can disprove by getting first card in their hand then assigning the others to the solution's cards
		Solution computerDisproveSuggestion = new Solution();
		Iterator<Card> iter = computer.getHand().iterator();
		Card disproveCard = iter.next();
		
		switch (disproveCard.getType()) {
		case PERSON:
			computerDisproveSuggestion.setPerson(disproveCard);
			computerDisproveSuggestion.setWeapon(solution.getWeapon());
			computerDisproveSuggestion.setRoom(solution.getRoom());
			break;
		case WEAPON:
			computerDisproveSuggestion.setPerson(solution.getPerson());
			computerDisproveSuggestion.setWeapon(disproveCard);
			computerDisproveSuggestion.setRoom(solution.getRoom());
			break;
		case ROOM:
			computerDisproveSuggestion.setPerson(solution.getPerson());
			computerDisproveSuggestion.setWeapon(solution.getWeapon());
			computerDisproveSuggestion.setRoom(disproveCard);
			break;
		default:
			break;
		}
		
		board.setTurn(1);
		assertEquals(null, board.handleSuggestion(computer, computerDisproveSuggestion));
		
		//Does the same but with the human
		Solution humanDisproveSuggestion = new Solution();
		iter = human.getHand().iterator();
		disproveCard = iter.next();
		
		switch (disproveCard.getType()) {
		case PERSON:
			humanDisproveSuggestion.setPerson(disproveCard);
			humanDisproveSuggestion.setWeapon(solution.getWeapon());
			humanDisproveSuggestion.setRoom(solution.getRoom());
			break;
		case WEAPON:
			humanDisproveSuggestion.setPerson(solution.getPerson());
			humanDisproveSuggestion.setWeapon(disproveCard);
			humanDisproveSuggestion.setRoom(solution.getRoom());
			break;
		case ROOM:
			humanDisproveSuggestion.setPerson(solution.getPerson());
			humanDisproveSuggestion.setWeapon(solution.getWeapon());
			humanDisproveSuggestion.setRoom(disproveCard);
			break;
		default:
			break;
		}
		
		board.setTurn(0);
		assertEquals(null, board.handleSuggestion(human, humanDisproveSuggestion));
		
		//Computer is accuser but human is the only one who can disprove
		//Uses same suggestion since human is the only one able to disprove again, only changes suggester
		board.setTurn(1);
		assertEquals(null, board.handleSuggestion(computer, humanDisproveSuggestion));
		
		//Three players can disprove, ensures the correct order
		//Done using three instead of two (per assignment guidelines) to make suggestion assembly easier
		//All computers
		Player firstDisprove = computer;
		ArrayList<Card> cards = new ArrayList<>();
		
		int playerCounter = 1;
		
		computerDisproveSuggestion = new Solution();
		
		//Makes sure to choose cards of different types from different players
		while (cards.size() < 3) {
			PlayerComputer comp = (PlayerComputer) board.getPlayers().get(playerCounter);
			int size = cards.size();
			
			if (size == 0) {
				iter = comp.getHand().iterator();
				cards.add(iter.next());
				firstDisprove = comp;
			}
			else if (size == 1) {
				for (Card c : comp.getHand()) {
					if (c.getType() != cards.get(0).getType()) {
						cards.add(c);
						break;
					}
				}
			}
			else {
				for (Card c : comp.getHand()) {
					if (c.getType() != cards.get(0).getType() && c.getType() != cards.get(1).getType()) {
						cards.add(c);
						break;
					}
				}
			}
			
			playerCounter++;
		}
		
		//Adds cards to suggestion
		for (Card c : cards) {
			computerDisproveSuggestion.setCardWithoutType(c);
		}
		
		//Makes sure that the first player able to disprove is the one to disprove
		board.setTurn(0);
		disproveCard = board.handleSuggestion(human, computerDisproveSuggestion);
		assertTrue(firstDisprove.getHand().contains(disproveCard));
		
		//Same as above but human is able to disprove
		firstDisprove = human;
		cards.clear();
		
		//Start at 2nd computer because 1st is accusing
		playerCounter = 2;
		
		//Makes sure to choose cards of different types from different players
		//Unlucky rng may lead to null pointer, rerun test if so
		while (cards.size() < 3) {
			Player player = board.getPlayers().get(playerCounter);
			int size = cards.size();
			
			if (size == 0) {
				iter = player.getHand().iterator();
				cards.add(iter.next());
				firstDisprove = player;
			}
			else if (size == 1) {
				for (Card c : player.getHand()) {
					if (c.getType() != cards.get(0).getType()) {
						cards.add(c);
						if (firstDisprove instanceof PlayerHuman)
							firstDisprove = player;
					}
				}
			}
			else {
				for (Card c : player.getHand()) {
					if (c.getType() != cards.get(0).getType() && c.getType() != cards.get(1).getType())
						cards.add(c);
				}
			}
			
			//Skips first computer player since that is the one making the accusation in this case
			playerCounter++;
		}
		
		//Adds cards to suggestion
		for (Card c : cards) {
			computerDisproveSuggestion.setCardWithoutType(c);
		}
		
		//Makes sure that the first non-human player able to disprove is the one to disprove
		board.setTurn(1);
		assertTrue(firstDisprove.getHand().contains(board.handleSuggestion(computer, computerDisproveSuggestion)));
	}
}