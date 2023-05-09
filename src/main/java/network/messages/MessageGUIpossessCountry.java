package network.messages;

import game.models.CountryName;
import gameState.GameState;

public class MessageGUIpossessCountry extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private GameState gameState; 
private CountryName country;
private int id;
private int troopsLeft;

	public GameState getGameState() {
	return gameState;
}

public CountryName getCountry() {
	return country;
}

public int getId() {
	return id;
}

public int getTroopsLeft() {
	return troopsLeft;
}

	public MessageGUIpossessCountry(GameState gameState,CountryName country, int id, int troopsLeft) {
		super(MessageType.MessageGUIpossessCountry);
		this.country = country;
		this.id = id;
		this.troopsLeft = troopsLeft;
		this.gameState = gameState;
		
		
	}

}
