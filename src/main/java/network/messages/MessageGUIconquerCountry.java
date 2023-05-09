package network.messages;

import game.models.CountryName;
import gameState.GameState;

public class MessageGUIconquerCountry extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public GameState getGameState() {
		return gameState;
	}

	public CountryName getCountry() {
		return country;
	}

	public int getId() {
		return id;
	}

	public int getTroops() {
		return troops;
	}

	private	GameState gameState;
	private CountryName country;
	private int id;
	private int troops; 
	
	public MessageGUIconquerCountry(GameState gameState, CountryName country, int id, int troops) {
		super(MessageType.MessageGUIconquerCountry);
		this.country = country;
		this.gameState = gameState;
		this.id = id;
		this.troops = troops;
	}

}
