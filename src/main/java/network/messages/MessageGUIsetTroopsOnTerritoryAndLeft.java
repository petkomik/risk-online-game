package network.messages;

import game.models.CountryName;
import game.models.Lobby;
import gameState.GameState;

public class MessageGUIsetTroopsOnTerritoryAndLeft extends Message {

	private static final long serialVersionUID = 1L;
	private GameState gameState;
	private CountryName countryName;
	private int numTroopsOfCountry;
	private int numTroopsOfPlayer;
	private Lobby lobby;

	public MessageGUIsetTroopsOnTerritoryAndLeft(GameState gameState,
			CountryName countryName, int numTroopsOfCountry, int numTroopsOfPlayer, Lobby clientsLobby) {
		super(MessageType.MessageGUIsetTroopsOnTerritoryAndLeft);
		this.gameState = gameState;
		this.countryName = countryName;
		this.numTroopsOfCountry = numTroopsOfCountry;
		this.numTroopsOfPlayer = numTroopsOfPlayer;
		this.lobby = clientsLobby;
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
	
	public int getNumTroopsOfPlayer() {
		return numTroopsOfPlayer;
	}

	public Lobby getLobby() {
		return lobby;
	}

}
