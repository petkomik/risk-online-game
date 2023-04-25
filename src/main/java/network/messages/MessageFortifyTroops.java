package network.messages;

import game.models.CountryName;
import game.models.Player;

public class MessageFortifyTroops extends Message {
	private static final long serialVersionUID = 1L;
	private Player player;
	private CountryName countryFrom;
	private CountryName countryTo;
	private int troops;
	private int playerId;

	public MessageFortifyTroops(int playerId, CountryName countryFrom, CountryName countryTo, int troops) {
		super(MessageType.MessageFortifyTroops);
		this.countryFrom = countryFrom;
		this.countryTo = countryTo;
		this.playerId = playerId;
		this.troops = troops;
	}

	/**
	 * 
	 */



	public Player getPlayer() {
		return player;
	}

	public CountryName getCountryFrom() {
		return countryFrom;
	}

	public CountryName getCountryTo() {
		return countryTo;
	}

	public int getTroops() {
		return troops;
	}

	public int getPlayerId() {
		return playerId;
	}

}
