package network.messages;

import game.models.CountryName;
import gameState.GameState;

public class MessageGUIsetTroopsOnTerritory extends Message {

	private static final long serialVersionUID = 1L;
	private GameState gameState;
	private CountryName countryName; 
	private int numTroopsOfCountry;
	
	
	public MessageGUIsetTroopsOnTerritory(GameState gameState, 
			CountryName countryName, int numTroopsOfCountry) {
		super(MessageType.MessageGUIsetTroopsOnTerritory);
		this.gameState = gameState;
		this.countryName = countryName;
		this.numTroopsOfCountry = numTroopsOfCountry;
	
	}

	public GameState getGameState() {
		return gameState;
	}


	public CountryName getCountryName() {
		return countryName;
	}


	public int getNumTroopsOfCountry() {
		return numTroopsOfCountry;
	}

	
}
