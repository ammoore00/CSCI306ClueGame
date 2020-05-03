package clueGame;

public class Card {
	private EnumCardType type;
	private String name;
	
	public Card(String name, EnumCardType type) {
		this.name = name;
		this.type = type;
	}
	
	public EnumCardType getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}
}
