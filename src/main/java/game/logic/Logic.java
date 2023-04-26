package game.logic;

import java.util.HashMap;

import game.exceptions.WrongCountryException;
import game.exceptions.WrongPhaseException;
import game.exceptions.WrongTroopsCountException;
import game.models.CountryName;
import game.models.Player;
import game.models.Territory;
import gameState.GameState;
import gameState.Period;
import general.AppController;

public class Logic {
	// playerDiceThrown back to GameHandler, to Gui/ Messanger
	public static int setInitialTroopsSize(GameState gamestate) {
		int troopsSize = 0;
		/** set available troopsize */
		switch (gamestate.getPlayers().size()) {
		case 2:
			troopsSize = 40;
			break;
		case 3:
			troopsSize = 35;
			break;
		case 4:
			troopsSize = 30;
			break;
		case 5:
			troopsSize = 25;
			break;
		case 6:
			troopsSize = 20;
			break;
		default:
			troopsSize = -1;
			break;
		}

		return troopsSize;
	}
	
	public static HashMap<Player, Integer> diceThrowToDetermineTheBeginner(GameState gameState) {
		HashMap<Player, Integer> playersDiceThrown = new HashMap<Player, Integer>();
		for (Player p : gameState.getPlayers().values()) {
			int diceNumber = getRandomDiceNumber();
			playersDiceThrown.put(p, diceNumber);
		}
		return playersDiceThrown;
	}

	public static Player getFirstPlayer(GameState gameState) {
		HashMap<Player, Integer> playerDice = gameState.getPlayersDiceThrown();
		Player firstPlayer = null;
		int maxDice = 0;
		for (Player p : playerDice.keySet()) {
			if (playerDice.get(p) > maxDice) {
				firstPlayer = p;
				maxDice = playerDice.get(p);
			}
		}
		return firstPlayer;
	}

	public static int getRandomDiceNumber() {
		return (int) (Math.random() * 6) + 1;
	}

	public static boolean claimTerritory(Player player, GameState gameState, CountryName territory) {

		if (gameState.getCurrentPlayer().equals(player)) {
			if (gameState.getCurrentGamePeriod().equals(Period.COUNTRYPOSESSION)) {
				if (gameState.getTerritories().get(territory).getOwnedByPlayer() == null) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean allTerritoriesClaimed(GameState gameState) {

		for (Territory territory : gameState.getTerritories().values()) {
			if (territory.getOwnedByPlayer() == null) {
				return false;
			}
		}
		return true;
	}
	// click on Country , check if u can deploy troops , if yes, return number of troops, if not allowed -1
	public static int canDeployTroopsToTerritory(GameState gameState, Player player, CountryName territory) {
		if (gameState.getCurrentPlayer().equals(player)) {
			if (gameState.getCurrentGamePeriod().equals(Period.INITIALREINFORCEMENT)) {
				if (gameState.getTerritories().get(territory).getOwnedByPlayer().equals(player)) {
					if (gameState.getPlayerTroopsLeft().get(player) >= 1) {
						return gameState.getPlayerTroopsLeft().get(player);
					}
				}
			}
		}
		return -1;
	}
	
	

	public static boolean isGameOver(GameState gameState) {
		return gameState.getAlivePlayers().size() == 1;
	}

	public static boolean canAttack(GameState gameState, Territory attacker, Territory defender) {
		if (gameState.getTerritories().get(attacker.getCountryName()).getNeighboringTerritories().contains(defender)) {
			if (!gameState.getTerritories().get(attacker.getCountryName()).getOwnedByPlayer()
					.equals(gameState.getTerritories().get(defender.getCountryName()).getOwnedByPlayer())) {
				if (gameState.getTerritories().get(attacker.getCountryName()).getNumberOfTroops() > 1) {
					return true;
				}
			}
		}

		return false;
	}
}
