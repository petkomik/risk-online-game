package network.messages;

import game.models.CountryName;
import game.models.Lobby;
import gameState.GameState;

public class MessageGUImoveTroopsFromTerritoryToOther extends Message {

	private static final long serialVersionUID = 1L;
	private GameState gameState;
	private CountryName from;
	private CountryName to;
	private int numberFrom;
	private int numberTo;
	private Lobby lobby;

	public MessageGUImoveTroopsFromTerritoryToOther(GameState gameState, 
			CountryName from, CountryName to, int numberFrom, int numberTo, Lobby clientsLobby) {
		super(MessageType.MessageGUImoveTroopsFromTerritoryToOther);
		this.gameState = gameState;
		this.from = from;
		this.to = to;
		this.numberFrom = numberFrom;
		this.numberTo = numberTo;
		this.lobby = clientsLobby;
	}

	public GameState getGameState() {
		return gameState;
	}

	public CountryName getFrom() {
		return from;
	}

	public CountryName getTo() {
		return to;
	}

	public int getNumberFrom() {
		return numberFrom;
	}

	public int getNumberTo() {
		return numberTo;
	}

	public Lobby getLobby() {
		return lobby;
	}

}
