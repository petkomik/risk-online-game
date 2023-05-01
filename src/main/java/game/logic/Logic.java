package game.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import game.exceptions.WrongCardsException;
import game.exceptions.WrongCardsSetException;
import game.exceptions.WrongCountryException;
import game.exceptions.WrongPeriodException;
import game.exceptions.WrongPhaseException;
import game.exceptions.WrongTroopsCountException;
import game.models.Card;
import game.models.Continent;
import game.models.CountryName;
import game.models.Player;
import game.models.Territory;
import gameState.GameState;
import gameState.Period;
import gameState.Phase;
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
	public static boolean canInitialDeployTroopsToTerritory(GameState gameState, Player player, CountryName territory) {
		if (gameState.getCurrentPlayer().equals(player)) {
			if (gameState.getCurrentGamePeriod().equals(Period.INITIALDEPLOY)) {
				if (gameState.getTerritories().get(territory).getOwnedByPlayer().getID() == player.getID()) {
					if (gameState.getPlayerTroopsLeft().get(player) >= 1) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static int canReinforceTroopsToTerritory(GameState gameState, Player player, CountryName territory) {
		if (gameState.getCurrentPlayer().equals(player)) {
			if (gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
				if (gameState.getTerritories().get(territory).getOwnedByPlayer().getID() == player.getID()) {
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
	
	public static boolean turnInRiskCards(List<Card> cards, Player player, GameState gameState) 
		throws WrongCountryException, WrongTroopsCountException, WrongPhaseException, WrongCardsException,
		WrongCardsSetException, WrongPeriodException {
		if (player == null || gameState.getCurrentPlayer().equals(player)) {
			throw new WrongPhaseException("It is not your turn");
		} else if (cards == null || cards.size() != 3) {
			throw new WrongCardsException("You have to choose 3 cards");
		} else if (gameState.getCurrentTurnPhase().equals(Phase.REINFORCE)) {
			throw new WrongPhaseException("You are not in Deploy Phase. Can't turn in cards in the moment");
		} else if (gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
			throw new WrongPeriodException("You are not in Main Period.");
		} else if (!gameState.getRiskCardsInPlayers().get(player).containsAll(cards)) {
			throw new WrongCardsException("You do not own all the cards that you tried to turn in.");
		} else if ((!cards.stream().allMatch(o -> o.getCardSymbol() == cards.get(0).getCardSymbol())) || (!cards
				.stream().map(Card::getCardSymbol).distinct().collect(Collectors.toSet()).equals(Set.of(1, 5, 10)))) {
			throw new WrongCardsSetException("The set of cards you turn in should either have three of "
					+ "the same troop type or one from each.");
		}
		
		return true;
	}

	public static boolean isDeployPeriodOver(GameState gameState) {
		for(int leftOver : gameState.getPlayerTroopsLeft().values()) {
			if(leftOver != 0) {
				return false;
			}
		}
		return true;
	}

	public static HashMap<Player, Integer> getTroopsReinforce(GameState gameState) {
		HashMap<Player, Integer> troops = gameState.getPlayerTroopsLeft();
		
		for(Territory territory : gameState.getTerritories().values()) {
			troops.put(territory.getOwnedByPlayer(), troops.get(territory.getOwnedByPlayer()) + 1);
		}
		
		for(Player player : troops.keySet()) {
			troops.replace(player, Math.max((troops.get(player) - troops.get(player) % 3) / 3, 3));
		}
		
		for(Continent continent : gameState.getContinents().keySet()) {
			Player continentOwner = gameState.getTerritories()
					.get(gameState.getContinents().get(continent).get(0)).getOwnedByPlayer();
			boolean success = true;
			for(CountryName countryName : gameState.getContinents().get(continent)) {
				Territory territory = gameState.getTerritories().get(countryName);
				if(!continentOwner.equals(territory.getOwnedByPlayer())) {
					success = false;
					break;
				}
			}
			if(success) {
				switch (continent) {
				case Africa:
					troops.replace(continentOwner, troops.get(continentOwner) + 3);
					break;
				case Asia:
					troops.replace(continentOwner, troops.get(continentOwner) + 7);
					break;
				case Australia:
					troops.replace(continentOwner, troops.get(continentOwner) + 2);
					break;
				case Europe:
					troops.replace(continentOwner, troops.get(continentOwner) + 5);
					break;
				case NorthAmerica:
					troops.replace(continentOwner, troops.get(continentOwner) + 5);
					break;
				case SouthAmerica:
					troops.replace(continentOwner, troops.get(continentOwner) + 2);
					break;
				}
			}
		}
		return troops;
	}

	public static boolean playerEndsPhase(Phase phase, int idOfPlayer, GameState gameState) {
		if(gameState.getCurrentPlayer().equals(gameState.getPlayers().get(idOfPlayer))) {
			if(gameState.getAlivePlayers().contains(gameState.getPlayers().get(idOfPlayer))) {
				if(gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
					if(gameState.getCurrentTurnPhase().equals(phase)) {
						return true;	
					}
				}
			}
		}
		
		return false;
	}

	public static boolean playerAttackingFromCountry(CountryName country, int idOfPlayer, GameState gameState) {
		if(gameState.getCurrentPlayer().equals(gameState.getPlayers().get(idOfPlayer))) {
			if(gameState.getAlivePlayers().contains(gameState.getPlayers().get(idOfPlayer))) {
				if(gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
					if(gameState.getCurrentTurnPhase().equals(Phase.ATTACK)) {
						if(gameState.getTerritories().get(country).getOwnedByPlayer()
								.equals(gameState.getCurrentPlayer())) {
							if(gameState.getTerritories().get(country).getNumberOfTroops() > 1) {
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}

	public static boolean playerAttackingCountry(CountryName country, int idOfPlayer, GameState gameState) {
		if(gameState.getCurrentPlayer().equals(gameState.getPlayers().get(idOfPlayer))) {
			if(gameState.getAlivePlayers().contains(gameState.getPlayers().get(idOfPlayer))) {
				if(gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
					if(gameState.getCurrentTurnPhase().equals(Phase.ATTACK)) {
						if(!gameState.getTerritories().get(country).getOwnedByPlayer()
								.equals(gameState.getCurrentPlayer())) {
							
						}
					}
				}
			}
		}
		
		return false;
	}

	public static ArrayList<CountryName> getUnreachableTerritories(CountryName country, int idOfPlayer,
			GameState gameState) {
		ArrayList<CountryName> unreachableCountries = new ArrayList<CountryName>();
		unreachableCountries.addAll(gameState.getTerritories().keySet());
		unreachableCountries.removeIf(x -> 
		gameState.getTerritories().get(x).getOwnedByPlayer()
		.equals(gameState.getPlayers().get(idOfPlayer)) ||
		gameState.getTerritories().get(x).getNeighboringTerritories()
		.contains(gameState.getTerritories().get(x)));		
		return unreachableCountries;
	}
}
