package network.messages;

import game.models.Lobby;
import gameState.GameState;

public class MessageGUIRollInitalDice extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GameState gameState;

	private int Id;
	private int value;
	private Lobby lobby;
	
	public MessageGUIRollInitalDice(GameState gameState, int Id, int value,Lobby lobby ) {
		super(MessageType.MessageGUIRollInitalDice);
		this.gameState = gameState;
		this.Id = Id;
		this.value = value;
		this.lobby = lobby;
	}

	public int getId() {
		return Id;
	}
	public GameState getGameState() {
		return gameState;
	}

	public int getValue() {
		return value;
	}

	public Lobby getLobby() {
		return lobby;
	}

}
