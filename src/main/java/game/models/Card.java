package game.models;
/**
 * class for game card representation
 * @author srogalsk
 *
 */
public class Card {
	private int cardValue; // e.g. infantry = 1, cavalry = 5, artillery = 10 or joker = -1
	private Player ownedBy;
	private String name;
	
	public Card(String name, int cardValue) {
		this.name = name;
		this.cardValue = cardValue;
	}
	
	public int getCardSymbol() {
		return cardValue;
	}
	public void setCardSymbol(int cardSymbol) {
		this.cardValue = cardSymbol;
	}
	public Player getOwnedBy() {
		return ownedBy;
	}
	public void setOwnedBy(Player ownedBy) {
		this.ownedBy = ownedBy;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
