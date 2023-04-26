package gameState;

import game.Lobby;
import game.logic.Logic;

public class GameHandler {
	// Gamelogic Ausführung der Methoden
	// Kommunikation zu SingleHandler und ClientHandler
	//Übergabe an GameState
	// GamePane 
	
	private GameState gameState;
	private Lobby lobby;
	
	public GameHandler (Lobby lobby) {
		this.gameState = new GameState(lobby);
		this.lobby = lobby;
	}
	
	public void determineInitialDice() {
		gameState.setPlayersDiceThrown(
				Logic.diceThrowToDetermineTheBeginner(gameState));
	}

}
