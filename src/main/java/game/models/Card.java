package game.models;

import java.io.Serializable;

/**
 * class for game card representation
 * @author srogalsk
 *
 */
public class Card implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int cardValue; // e.g. infantry = 1, cavalry = 5, artillery = 10 or joker = -1
	private Player ownedBy;
	private CountryName name;
	
	public Card(CountryName name, int cardValue) {
		this.name = name;
		this.cardValue = cardValue;
	}
	
	/**constructor for defensive copying*/
	public Card(Card card, Player player) {
		this.name = card.getName();
		this.cardValue = card.getCardSymbol();
		this.ownedBy = (player);
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
	public CountryName getName() {
		return name;
	}
	public void setName(CountryName name) {
		this.name = name;
	}

}
