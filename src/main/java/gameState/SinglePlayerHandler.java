package gameState;

import game.Lobby;

public class SinglePlayerHandler {
	
	GameHandler handler;
	
	public SinglePlayerHandler(Lobby lobby) {
		handler = new GameHandler(lobby);
		
	}

}
