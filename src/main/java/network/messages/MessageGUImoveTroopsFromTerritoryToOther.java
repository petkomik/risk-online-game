package network.messages;

import game.models.CountryName;
import gameState.GameState;

public class MessageGUImoveTroopsFromTerritoryToOther extends Message {

	private static final long serialVersionUID = 1L;
	private GameState gameState;
	private CountryName from;
	private CountryName to;
	private int numberFrom;
	private int numberTo;

	public MessageGUImoveTroopsFromTerritoryToOther(GameState gameState, 
			CountryName from, CountryName to, int numberFrom, int numberTo) {
		super(MessageType.MessageGUImoveTroopsFromTerritoryToOther);
		this.gameState = gameState;
		this.from = from;
		this.to = to;
		this.numberFrom = numberFrom;
		this.numberTo = numberTo;
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

}
