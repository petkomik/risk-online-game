package gameState;

import game.Lobby;

public class SinglePlayerHandler {
	
	GameHandler handler;
	Lobby lobby;

	public SinglePlayerHandler(Lobby lobby) {
		handler = new GameHandler(lobby);
		this.lobby = lobby;
	
	}

	public Lobby getLobby() {
		return lobby;
	}
}
