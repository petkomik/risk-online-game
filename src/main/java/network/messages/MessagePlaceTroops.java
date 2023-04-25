package network.messages;

import game.models.CountryName;

public class MessagePlaceTroops extends Message{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private CountryName countryName;
	private int numberOfTroops;
	private int payerID;
	
	public MessagePlaceTroops(int playerID, CountryName countryName, int numberOfTroops) {
		super(MessageType.MessagePlaceTroops);
		this.countryName = countryName;
		this.numberOfTroops = numberOfTroops;
		this.payerID = playerID;
		
	}

	public CountryName getCountryName() {
		return countryName;
	}

	public int getNumberOfTroops() {
		return numberOfTroops;
	}

	
	
	
}
