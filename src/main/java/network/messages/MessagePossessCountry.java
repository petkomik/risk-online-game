package network.messages;

import game.models.CountryName;

public class MessagePossessCountry extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int playerId;
	private CountryName coutry;
	public MessagePossessCountry(int playerId, CountryName country) {
		super(MessageType.MessagePossessCountry);
		this.coutry = country;
		this.playerId = playerId;
		
	}
	public int getPlayerId() {
		return playerId;
	}
	public CountryName getCoutry() {
		return coutry;
	}

}
