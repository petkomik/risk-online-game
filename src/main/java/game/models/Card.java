package game.models;
/**
 * class for game card representation
 * @author srogalsk
 *
 */
public class Card implements Cloneable{
	private int cardValue; // e.g. infantry = 1, cavalry = 5, artillery = 10 or joker = -1
	private Player ownedBy;
	private CountryName name;
	
	public Card(CountryName name, int cardValue) {
		this.name = name;
		this.cardValue = cardValue;
	}
	
	/**constructor for defensive copying*/
	public Card(Card card) {
		this.name = card.getName();
		this.cardValue = card.getCardSymbol();
		this.ownedBy = card.getOwnedBy() != null ? new Player(card.getOwnedBy()) : null;
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
	
	@Override
    public Card clone() {
        return new Card(this);
    }
}
