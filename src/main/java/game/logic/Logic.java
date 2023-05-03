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
	
	public static int setInitialTroopsSize(GameState gamestate) {
		int troopsSize = 0;
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
	
	public static HashMap<Integer, Integer> diceThrowToDetermineTheBeginner(GameState gameState) {
		HashMap<Integer, Integer> playersDiceThrown = new HashMap<Integer, Integer>();
		for (int idPlayer : gameState.getPlayers().keySet()) {
			int diceNumber = getRandomDiceNumber();
			playersDiceThrown.put(idPlayer, diceNumber);
		}
		return playersDiceThrown;
	}

	public static Integer getFirstPlayer(GameState gameState) {
		HashMap<Integer, Integer> playerDice = gameState.getPlayersDiceThrown();
		Integer firstPlayer = null;
		int maxDice = 0;
		for (Integer playerId : playerDice.keySet()) {
			if (playerDice.get(playerId) > maxDice) {
				firstPlayer = playerId;
				maxDice = playerDice.get(playerId);
			}
		}
		return firstPlayer;
	}

	public static boolean canThrowInitialDice(int idOfPlayer, GameState gamestate) {
		if(gamestate.getCurrentPlayer().getID() == idOfPlayer) {
			if(gamestate.getCurrentGamePeriod().equals(Period.DICETHROW)) {
				return true;
			}
		}
		return false;
	}

	public static int getRandomDiceNumber() {
		return (int) (Math.random() * 6) + 1;
	}

	public static boolean claimTerritory(Player player, GameState gameState, CountryName territory) {
		if (gameState.getCurrentPlayer().equals(player)) {
			if (gameState.getCurrentGamePeriod().equals(Period.COUNTRYPOSESSION)) {
				if(gameState.getTerritories().get(territory).getOwnedByPlayer() == null) {
					if(gameState.getAlivePlayers().get((gameState.getAlivePlayers().indexOf(player) + 
							gameState.getAlivePlayers().size() + 1) % gameState.getAlivePlayers().size()).getID()
							== Logic.getFirstPlayer(gameState)) {
						if(gameState.getPlayerTroopsLeft().get(player.getID()) > 
								gameState.getPlayerTroopsLeft().get(gameState.getAlivePlayers().get(
										(gameState.getAlivePlayers().indexOf(player) + 
												gameState.getAlivePlayers().size() + 1) % gameState.getAlivePlayers().size()).getID()
							)) {
							return true;
						}
					} else {
						if(gameState.getPlayerTroopsLeft().get(player.getID()) ==
								gameState.getPlayerTroopsLeft().get(gameState.getAlivePlayers().get(
										(gameState.getAlivePlayers().indexOf(player) + 
												gameState.getAlivePlayers().size() + 1) % gameState.getAlivePlayers().size()).getID()
							)) {
							return true;
						}
					}		
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
		if (gameState.getCurrentPlayer().getID() == player.getID()) {
			if (gameState.getCurrentGamePeriod().equals(Period.INITIALDEPLOY)) {
				if (gameState.getTerritories().get(territory).getOwnedByPlayer().getID() == player.getID()) {
					if (gameState.getPlayerTroopsLeft().get(player.getID()) >= 1) {
						if(gameState.getAlivePlayers().get((gameState.getAlivePlayers().indexOf(player) + 
								gameState.getAlivePlayers().size() + 1) % gameState.getAlivePlayers().size()).getID()
								== Logic.getFirstPlayer(gameState)) {
							if(gameState.getPlayerTroopsLeft().get(player.getID()) > 
									gameState.getPlayerTroopsLeft().get(gameState.getAlivePlayers().get(
											(gameState.getAlivePlayers().indexOf(player) + 
													gameState.getAlivePlayers().size() + 1) % gameState.getAlivePlayers().size()).getID()
								)) {
								return true;
							}
						} else {
							if(gameState.getPlayerTroopsLeft().get(player.getID()) ==
									gameState.getPlayerTroopsLeft().get(gameState.getAlivePlayers().get(
											(gameState.getAlivePlayers().indexOf(player) + 
													gameState.getAlivePlayers().size() + 1) % gameState.getAlivePlayers().size()).getID()
								)) {
								return true;
							}
						}		
					}
				}
			}
		}
		return false;
	}
	
	public static boolean canReinforceTroopsToTerritory(GameState gameState, Player player, CountryName territory) {
		if (gameState.getCurrentPlayer().equals(player)) {
			if (gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
				if (gameState.getTerritories().get(territory).getOwnedByPlayer().getID() == player.getID()) {
					if(gameState.getPlayerTroopsLeft().get(player.getID()) > 0) {
						if(gameState.getCurrentTurnPhase().equals(Phase.REINFORCE)) {
							return true;							
						}
					}
				}
			}
		}
		return false;
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
		} else if (!gameState.getRiskCardsInPlayers().get(player.getID()).containsAll(cards)) {
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

	public static HashMap<Integer, Integer> getTroopsReinforce(GameState gameState) {
		HashMap<Integer, Integer> troops = gameState.getPlayerTroopsLeft();
		
		for(Territory territory : gameState.getTerritories().values()) {
			troops.put(territory.getOwnedByPlayer().getID(), troops.get(territory.getOwnedByPlayer().getID()) + 1);
		}
		
		for(Integer idPlayer : troops.keySet()) {
			troops.replace(idPlayer, Math.max((troops.get(idPlayer) - troops.get(idPlayer) % 3) / 3, 3));
		}
		
		for(Continent continent : gameState.getContinents().keySet()) {
			int continentOwner = gameState.getTerritories()
					.get(gameState.getContinents().get(continent).get(0)).getOwnedByPlayer().getID();
			boolean success = true;
			for(CountryName countryName : gameState.getContinents().get(continent)) {
				Territory territory = gameState.getTerritories().get(countryName);
				if(continentOwner != territory.getOwnedByPlayer().getID()) {
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
							return true;
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
		gameState.getTerritories().get(x).getOwnedByPlayer().equals(gameState.getPlayers().get(idOfPlayer)) ||
		gameState.getTerritories().get(x).getNeighboringTerritories().contains(gameState.getTerritories().get(x)));		
		return unreachableCountries;
	}

	public static boolean playerFortifyingPosition(CountryName country, int idOfPlayer, GameState gameState) {
		if(gameState.getCurrentPlayer().equals(gameState.getPlayers().get(idOfPlayer))) {
			if(gameState.getAlivePlayers().contains(gameState.getPlayers().get(idOfPlayer))) {
				if(gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
					if(gameState.getCurrentTurnPhase().equals(Phase.FORTIFY)) {
						if(gameState.getTerritories().get(country).getOwnedByPlayer()
								.equals(gameState.getCurrentPlayer())) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}


	public static boolean playerEndsPhase(Phase phase, int idOfPlayer, GameState gameState) {
		if(gameState.getCurrentPlayer().equals(gameState.getPlayers().get(idOfPlayer))) {
			if(gameState.getAlivePlayers().contains(gameState.getPlayers().get(idOfPlayer))) {
				if(gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
					if(gameState.getCurrentTurnPhase().equals(phase)) {
						if(phase.equals(Phase.REINFORCE)) {
							return gameState.getPlayerTroopsLeft().get(idOfPlayer) == 0;
						}
						return true;	
					}
				}
			}
		}
		
		return false;
	}

	public static boolean playerEndsTurn(Period period, int idOfPlayer, GameState gameState) {
		if(gameState.getCurrentPlayer().equals(gameState.getPlayers().get(idOfPlayer))) {
			if(gameState.getAlivePlayers().contains(gameState.getPlayers().get(idOfPlayer))) {
				if(!gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD)) {
					return true;		
				}
			}
		}
		
		return false;
	}

	public static boolean isDiceThrowPeriodOver(GameState gameState, int idOfPlayer) {
		if(gameState.getCurrentPlayer().equals(gameState.getPlayers().get(idOfPlayer))) {
			if(gameState.getAlivePlayers().contains(gameState.getPlayers().get(idOfPlayer))) {
				if(gameState.getCurrentGamePeriod().equals(Period.DICETHROW)) {
					if(gameState.getAlivePlayers().get(gameState.getAlivePlayers().size() - 1).getID() == idOfPlayer) {
						return true;		

					}
				}
			}
		}
				
		return false;
	}
	
	public static boolean playerForitfyConfirmedIsOk(GameState gameState, Player player, 
			CountryName from, CountryName to, int troopsNumber) {
		if(gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD) ) {
			if(gameState.getCurrentTurnPhase().equals(Phase.FORTIFY)) {
				if(gameState.getCurrentPlayer().getID() == player.getID()) {
					if(gameState.getTerritories().get(from).getOwnedByPlayer()
							.equals(gameState.getCurrentPlayer()) ) {
						if(gameState.getTerritories().get(to).getOwnedByPlayer()
								.equals(gameState.getCurrentPlayer())) {
							if(gameState.getTerritories().get(from).getNumberOfTroops() >= troopsNumber) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public static boolean playerAttackAttackConfirmedIsOK(GameState gameState, int idOfPLayer, 
			CountryName attacking, CountryName attacked, int numTroops) {
		if(gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD) ) {
			if(gameState.getCurrentTurnPhase().equals(Phase.ATTACK)) {
				if(gameState.getCurrentPlayer().getID() == idOfPLayer) {
					if(gameState.getTerritories().get(attacking).getOwnedByPlayer()
							.equals(gameState.getCurrentPlayer()) ) {
						if(!gameState.getTerritories().get(attacked).getOwnedByPlayer()
								.equals(gameState.getCurrentPlayer())) {
							if(gameState.getTerritories().get(attacking).getNumberOfTroops() >= numTroops) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public static boolean playerAttackColoniseConfirmedIsOK(GameState gameState, int idOfPLayer, 
			CountryName attacking, CountryName attacked, int numTroops) {
		if(gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD) ) {
			if(gameState.getCurrentTurnPhase().equals(Phase.ATTACK)) {
				if(gameState.getCurrentPlayer().getID() == idOfPLayer) {
					if(gameState.getTerritories().get(attacking).getOwnedByPlayer()
							.equals(gameState.getCurrentPlayer()) ) {
						if(gameState.getTerritories().get(attacked).getOwnedByPlayer()
								.equals(gameState.getCurrentPlayer())) {
							if(gameState.getTerritories().get(attacking).getNumberOfTroops() >= numTroops) {
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public static boolean playerReinforceConfirmedIsOk(GameState gameState, int idPlayer, 
			CountryName country, int numTroops) {
		if(gameState.getCurrentGamePeriod().equals(Period.MAINPERIOD) ) {
			if(gameState.getCurrentTurnPhase().equals(Phase.REINFORCE)) {
				if(gameState.getCurrentPlayer().getID() == idPlayer) {
					if(gameState.getTerritories().get(country).getOwnedByPlayer()
							.equals(gameState.getCurrentPlayer()) ) {
						if(gameState.getPlayerTroopsLeft().get(idPlayer) >= numTroops) {
							return true;
						}
					}
				}
			}
		}	
		return false;
	}
	

	public static boolean isGameOver(GameState gameState) {
		return gameState.getAlivePlayers().size() == 1;
	}
}
