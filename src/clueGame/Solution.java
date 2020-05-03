package clueGame;

import java.util.HashSet;
import java.util.Set;

public class Solution {
	private Card person;
	private Card weapon;
	private Card room;
	
	public boolean contains(Card card) {
		return card != null && card == person || card == weapon || card == room;
	}
	
	public void setPerson(Card person) {
		this.person = person;
	}

	public void setWeapon(Card weapon) {
		this.weapon = weapon;
	}

	public void setRoom(Card room) {
		this.room = room;
	}

	public void setCardWithoutType(Card c) {
		switch (c.getType()) {
		case PERSON:
			setPerson(c);
			break;
		case WEAPON:
			setWeapon(c);
			break;
		case ROOM :
			setRoom(c);
			break;
		default:
			break;
		}
	}

	public Card getPerson() {
		return person;
	}

	public Card getWeapon() {
		return weapon;
	}

	public Card getRoom() {
		return room;
	}
	
	public Set<Card> getAllCardsAsSet() {
		Set<Card> solutionSet = new HashSet<>();
		
		solutionSet.add(person);
		solutionSet.add(weapon);
		solutionSet.add(room);
		
		return solutionSet;
	}
	
	public String toString() {
		String personName = person.getName();
		String weaponName = weapon.getName();
		String roomName = room.getName();
		
		return "Person: " + personName + ", Weapon: " + weaponName + ", Room: " + roomName;
	}
	
	public boolean isComplete() {
		return person != null && weapon != null && room != null;
	}
}
