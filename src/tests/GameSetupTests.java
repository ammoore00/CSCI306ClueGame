package tests;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.EnumCardType;
import clueGame.Player;
import clueGame.PlayerComputer;
import clueGame.PlayerHuman;

public class GameSetupTests {
	private Board board;
	
	@Before
	public void initialize() {
		board = Board.getInstance();
		board.setConfigFiles("Layout.csv", "Rooms.txt", "Players.txt", "Weapons.txt");
		board.initialize();
	}
	
	@Test
	public void playerTest() {
		//Makes sure players load correctly
		ArrayList<Player> players = board.getPlayers();
		assertEquals(6, players.size());
		//Checks names
		assert(players.get(0).getName().equals("Captain Holmes"));
		assert(players.get(1).getName().equals("Doctor Burrows"));
		assert(players.get(2).getName().equals("XO Jameson"));
		assert(players.get(3).getName().equals("Engineer Roberts"));
		assert(players.get(4).getName().equals("Officer Smith"));
		assert(players.get(5).getName().equals("Commander Ulitsky"));
		//Checks colors
		assert(players.get(0).getColor() == Color.BLUE);
		assert(players.get(1).getColor() == Color.GREEN);
		assert(players.get(2).getColor() == Color.YELLOW);
		assert(players.get(3).getColor() == Color.ORANGE);
		assert(players.get(4).getColor() == Color.RED);
		assert(players.get(5).getColor() == Color.MAGENTA);
		//Checks type
		assert(players.get(0) instanceof PlayerHuman);
		assert(players.get(1) instanceof PlayerComputer);
		assert(players.get(2) instanceof PlayerComputer);
		assert(players.get(3) instanceof PlayerComputer);
		assert(players.get(4) instanceof PlayerComputer);
		assert(players.get(5) instanceof PlayerComputer);
	}
	
	@Test
	public void weaponTest() {
		//Makes sure weapons loaded correctly
		ArrayList<Card> weapons = board.getWeaponList();
		assertEquals(6, weapons.size());
		assert(weapons.get(0).getName().equals("Laser Pistol"));
		assert(weapons.get(1).getName().equals("Knife"));
		assert(weapons.get(2).getName().equals("Lead Pipe"));
		assert(weapons.get(3).getName().equals("Mech Suit"));
		assert(weapons.get(4).getName().equals("Airlock"));
		assert(weapons.get(5).getName().equals("Teslacoil"));
	}
	
	@Test
	public void cardTest() {
		//Makes sure cards loaded correctly
		ArrayList<Card> deck = board.getDeck();
		assertEquals(21, deck.size());
		//Checks for cards of each type
		assert(deck.get(0).getType() == EnumCardType.ROOM);
		assert(deck.get(9).getType() == EnumCardType.PERSON);
		assert(deck.get(15).getType() == EnumCardType.WEAPON);
		
		//Makes all players have the correct number of cards
		ArrayList<Player> players = board.getPlayers();
		for (Player p : players) {
			assertEquals(3, p.getHand().size());
		}
	}
}
