package network.messages;

import game.Lobby;
import gameState.GameState;

public class MessageGUIendBattle extends Message {

	private static final long serialVersionUID = 1L;
	
	private GameState gameState;
	private Lobby lobby;
	
	public MessageGUIendBattle(GameState gameState, Lobby clientsLobby) {
		super(MessageType.MessageGUIendBattle);
		this.gameState = gameState;
		this.lobby = clientsLobby;
	}

	public GameState getGameState() {
		return gameState;
	}

	public Lobby getLobby() {
		return lobby;
	}
	

}
