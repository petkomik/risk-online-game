package network.messages;

import game.models.CountryName;
import game.models.Player;

public class MessageAttack extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Player player;
	private CountryName countryFrom;
	private CountryName countryTo;
	private int troops;
	private int playerId;

	
	public MessageAttack(Player player, CountryName countryFrom, CountryName cuntryTo, int troops) {
		super(MessageType.MessageAttack);
		this.countryFrom = countryFrom;
		this.countryTo = cuntryTo;
		this.player = player;
		this.troops = troops;
	}
	public MessageAttack(int playerID, CountryName countryFrom, CountryName cuntryTo, int troops) {
		super(MessageType.MessageAttack);
		this.countryFrom = countryFrom;
		this.countryTo = cuntryTo;
		this.playerId = playerID;
		this.troops = troops;
	}

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
