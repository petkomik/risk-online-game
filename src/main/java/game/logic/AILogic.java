package game.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import game.models.CountryName;
import game.models.PlayerAI;
import game.models.Territory;
import gameState.GameState;
import javafx.util.Pair;
/**
 * AILogic class to simulate a player
 *
 * @author jorohr
 */

public class AILogic {

	public static CountryName chooseTerritoryToInitialClaim(GameState gameState, PlayerAI player) {
				
		switch(player.getLevel()) {
			case EASY:
				return getRandomFreeCountryName(gameState);
			default:
				return null;
		}
	}
	
	public static CountryName chooseTerritoryToInitialReinforce(GameState gameState, PlayerAI player) {
		
		switch(player.getLevel()) {
			case EASY:
				return getRandomOwnedCountryName(gameState, player);
			case CASUAL:
				return null;
			case HARD:
				return null;
			default:
				return null;
		}
	}
	
	public static Pair<CountryName, Integer> chooseTerritoryToReinforce(GameState gameState, PlayerAI player) {
		
		switch(player.getLevel()) {
			case EASY:
				int randNumbTroops = (int) (Math.random()*gameState.getPlayerTroopsLeft().get(player));
				return new Pair(mostInnerCountry(gameState, player), randNumbTroops);
			case CASUAL:
				return null;
			case HARD:
				return null;
			default:
				return null;
		}
	}
	
	public static CountryName getRandomFreeCountryName(GameState gameState) {
		HashMap<CountryName, Territory> territories = gameState.getTerritories();

		for(Entry<CountryName, Territory> set : territories.entrySet()) {
			if(set.getValue().getOwnedByPlayer() == null) {
				return set.getKey();
			}
		}
		return null;
	}
	
	public static CountryName getRandomOwnedCountryName(GameState gameState, PlayerAI player) {
		HashMap<CountryName, Territory> territories = gameState.getTerritories();

		for(Entry<CountryName, Territory> set : territories.entrySet()) {
			if(set.getValue().getOwnedByPlayer().getID() == player.getID()) {
				return set.getKey();
			}
		}
		return null;
	}
	
	//owned territory with the fewest owned neighbours -> most outer country
	public static Territory mostOuterCountry(GameState gameState, PlayerAI player) {
		
			int min = Integer.MAX_VALUE;
			int count = 0;
			Territory terr = null;
			for(Entry<CountryName, Territory> set : gameState.getTerritories().entrySet()) {
				if(set.getValue().getOwnedByPlayer().getID() == player.getID()) {
					for(Territory neighbour : set.getValue().getNeighboringTerritories()) {
						if(neighbour.getOwnedByPlayer().getID() == player.getID()) {
							count++;
						}
					}
					if(count < min) {
						min = count;
						terr = set.getValue();
					}
				}
			}
			return terr;
	}
	
	//owned territory with the most owned neighbours -> most inner country
	public static Territory mostInnerCountry(GameState gameState, PlayerAI player) {
		
		int max = Integer.MIN_VALUE;
		int count = 0;
		Territory terr = null;
		for(Entry<CountryName, Territory> set : gameState.getTerritories().entrySet()) {
			if(set.getValue().getOwnedByPlayer().getID() == player.getID()) {
				for(Territory neighbour : set.getValue().getNeighboringTerritories()) {
					if(neighbour.getOwnedByPlayer().getID() == player.getID()) {
						count++;
					}
				}
				if(count > max) {
					max = count;
					terr = set.getValue();
				}
			}
		}
		return terr;
	}
	
	//decision whether to attack a territory or not
	public static boolean willAttack(GameState gameState, PlayerAI player) {
		switch(player.getLevel()) {
		case EASY:
			int prob = (int) ((Math.random() * 10));
			if(prob <=5) {
				return true;
			}else {
				return false;
			}
		case CASUAL:
			return false;
		case HARD:
			return isNeighbourWithLessTroops(gameState, player);
		default:
			return false;
		}
	}
	
	//states whether there is a not owned neighbour which has less troops
	public static boolean isNeighbourWithLessTroops(GameState gameState, PlayerAI player) {
		for(Entry<CountryName, Territory> set : gameState.getTerritories().entrySet()) {
			if(set.getValue().getOwnedByPlayer().getID() == player.getID()) {
				for(Territory neighbour : set.getValue().getNeighboringTerritories()) {
					if(neighbour.getOwnedByPlayer().getID() != player.getID() &&
							neighbour.getNumberOfTroops() < set.getValue().getNumberOfTroops()) {
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static Pair<CountryName, CountryName> chooseTerritoryPairAttack(GameState gameState, PlayerAI player) {

		HashMap<CountryName, Territory> territories = gameState.getTerritories();
		
		switch(player.getLevel()) {
			case EASY:
				Territory defender = mostOuterCountry(gameState, player);
				return new Pair(defender, getNeighbourWithHighestNumb(defender, player));
//				for(Entry<CountryName, Territory> att : territories.entrySet()) {
//					if(att.getValue().getOwnedByPlayer().getID() == player.getID()) {
//						for(Entry<CountryName, Territory> def : territories.entrySet()) {
//							if(Logic.canAttack(gameState, att.getValue(), def.getValue())) {
//								return new Pair(mostOuterCountry(gameState, player), att.getKey());
//							}
//						}
//					}
//				}
			case CASUAL:
				return null;
			case HARD:
				return null;
			default:
				return null;
		}
	}
	
	//gets the neighbour with the highest number of troops
	public static CountryName getNeighbourWithHighestNumb(Territory territory, PlayerAI player) {
		int max = 0;
		CountryName highNeigh = null;
		for(Territory t : territory.getNeighboringTerritories()) {
			if(t.getNumberOfTroops() > max && t.getOwnedByPlayer().getID() != player.getID()) {
				max = t.getNumberOfTroops();
				highNeigh = t.getCountryName();
			}
		}
		return highNeigh;
	}
	
	//gets the neighbour with the highest number of troops
	public static CountryName getNeighbourWithLowestNumb(Territory territory, PlayerAI player) {
		int min = 0;
		CountryName lowNeigh = null;
		for(Territory t : territory.getNeighboringTerritories()) {
			if(t.getNumberOfTroops() < min && t.getOwnedByPlayer().getID() != player.getID()) {
				min = t.getNumberOfTroops();
				lowNeigh = t.getCountryName();
			}
		}
		return lowNeigh;
	}
	
	public static int chooseTroopsInAttack(Territory territory) {
		return territory.getNumberOfTroops();
	}
	
	public static int chooseTroopsToSendToConqueredTerritory(Territory winner, PlayerAI player) {
		switch(player.getLevel()) {
		case EASY:
			return (int)(Math.random() * winner.getNumberOfTroops()-1) + 1;
		case CASUAL:
			return 0;
		case HARD:
			return 0;
		default:
			return 0;
		}
	}
	
	public static boolean willFortify(GameState gameState, PlayerAI player) {
		switch(player.getLevel()) {
		case EASY:
			int prob = (int) (Math.random() * 2);
			if(prob == 0) {
				return true;
			}else {
				return false;
			}
		case CASUAL:
			return false;
		// search for a country that is owned by the playerAI and is surrounded by its own territories only
		case HARD:
			ArrayList<Territory> territoriesOwnedByOwn = countriesSurroundedByOwnedCountries(gameState, player);
			if(territoriesOwnedByOwn != null) {
				for(Territory territory : territoriesOwnedByOwn) {
					if(territory.getNumberOfTroops() > 1) {
						return true;
					}
				}
			}
			return false;
		default:
			return false;
		}
	}
	
	//list of countries, that are surrounded by own countries only
	public static ArrayList<Territory> countriesSurroundedByOwnedCountries(GameState gameState, PlayerAI player) {
		ArrayList<Territory> list = new ArrayList<Territory>();
		for(Entry<CountryName, Territory> set : gameState.getTerritories().entrySet()) {
			if(set.getValue().getOwnedByPlayer().getID() == player.getID()) {
				for(Territory neighbour : set.getValue().getNeighboringTerritories()) {
					if(neighbour.getOwnedByPlayer().getID() != player.getID()) {
						break;
					}
				}
				list.add(set.getValue());
			}
		}
		return list;
	}
	
	public static Pair<CountryName, CountryName>  chooseTerritoriesPairFortify(GameState gameState, PlayerAI player){
		return null;
	}
	
	public static int chooseTroopsToSendFortify(Territory territory, PlayerAI player) {
		switch(player.getLevel()) {
		case EASY:
			return (int) (Math.random() * territory.getNumberOfTroops()) + 1;
		case CASUAL:
			return 0;
		case HARD:
			return territory.getNumberOfTroops();
		default:
			return 0;
		}
	}
}