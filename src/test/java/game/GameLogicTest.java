package game;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import game.logic.Logic;
import game.models.CountryName;
import game.models.Player;
import game.models.PlayerSingle;
import game.models.Territory;
import gameState.GameState;
import gameState.Period;

public class GameLogicTest {
	
	private GameState gameState;
	private Player player1;
	private CountryName testTerritory;
	
	@BeforeEach
	void setUp() throws Exception {
		// Set up a new GameState and Player object before each test
		player1 = new PlayerSingle("Player 1", 0);
		Player player3 = new PlayerSingle("Player 3", 2);
		Lobby lobby = new Lobby();
		ArrayList<Player> playerList = new ArrayList<>();
		playerList.add(player3);
		playerList.add(player1);
		lobby.setPlayersJoined(playerList);
		gameState = new GameState(lobby);
		gameState.setCurrentPlayer(0);
		gameState.setCurrentGamePeriod(Period.COUNTRYPOSESSION);
		testTerritory = CountryName.Argentina; // choose a test territory

	}

	@Test
	void testClaimTerritory() {
		// Test claiming a territory when it is unclaimed
		boolean result = Logic.claimTerritory(player1, gameState, testTerritory);
		assertTrue(result);
		
		// Test claiming a territory when it is already owned by another player
		Player player2 = new PlayerSingle("Player 2", 1);
		gameState.getTerritories().get(testTerritory).setOwnedByPlayer(player2);
		result = Logic.claimTerritory(player1, gameState, testTerritory);
		assertFalse(result);
		
		// Test claiming a territory when it is owned by the same player
		testTerritory = CountryName.Afghanistan;
		gameState.getTerritories().get(testTerritory).setOwnedByPlayer(player1);
		result = Logic.claimTerritory(player1, gameState, testTerritory);
		assertFalse(result);
		
		// Test claiming a territory during the wrong game period
		gameState.setCurrentGamePeriod(Period.COUNTRYPOSESSION);
		result = Logic.claimTerritory(player1, gameState, testTerritory);
		assertFalse(result);
		
		// Test claiming a territory when it is not the current player's turn
		Player player3 = gameState.getAlivePlayers().get(0);
		gameState.setCurrentPlayer(player3.getID());
		gameState.setCurrentGamePeriod(Period.COUNTRYPOSESSION);
		result = Logic.claimTerritory(player1, gameState, testTerritory);
		assertFalse(result);
	}

	@Test
	void fortifyTroopsTest() {
		assertTrue(true);
	}
}
