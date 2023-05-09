package game.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.stream.Stream;

import game.models.CountryName;
import game.models.Player;
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
		
		HashMap<CountryName, Territory> territories = gameState.getTerritories();
		
		switch(player.getLevel()) {
			case EASY:
				return getRandomFreeCountryName(gameState);
			case CASUAL:
				if(Math.random() < 0.5) {
					return getRandomFreeCountryName(gameState);
				}
				else {
					if(getNumberOfCountriesOwnedByPlayer(territories.values(), player) == 0) {
						return getRandomFreeCountryName(gameState);
					}
					for(Territory t : territories.values()) {
						if(t.getOwnedByPlayer().getID() == player.getID()) {
							return getNearestTerritory(t.getNeighboringTerritories(), gameState);
						}
					}
				}
			case HARD:
				if(getNumberOfCountriesOwnedByPlayer(territories.values(), player) == 0) {
					return getRandomFreeCountryName(gameState);
				}
				
				ArrayList<Territory> playerTerr = new ArrayList<>();
				for(Territory t : territories.values()) {
					if(t.getOwnedByPlayer() != null && t.getOwnedByPlayer().getID() == player.getID()) {
						playerTerr.add(t);
					}
				}

				return getNearestTerritory(playerTerr, gameState);
				
			default:
				return null;
		}
	}
	private static CountryName getNearestTerritory(Collection<Territory> territories, GameState gameState) {
		for(Territory t : territories) {
			for(Territory tN : t.getNeighboringTerritories()) {
				if(tN.getOwnedByPlayer() == null) {
					return tN.getCountryName();
				}
			}
		}
		for(Territory t : territories) {
			for(Territory tN : t.getNeighboringTerritories()) {
				for(Territory tNN : tN.getNeighboringTerritories()) {
					if(tNN.getOwnedByPlayer() == null) {
						return tNN.getCountryName();
					}
				}
			}
		}
		return getRandomFreeCountryName(gameState);
	}
	
	private static int getNumberOfCountriesOwnedByPlayer(Collection<Territory> territories, Player player) {
		int i = 0;
		for(Territory t : territories) {
			if(t.getOwnedByPlayer() != null && t.getOwnedByPlayer().getID() == player.getID()) {
				i++;
			}
		}
		return i;
	}
	public static CountryName chooseTerritoryToInitialReinforce(GameState gameState, PlayerAI player) {

		HashMap<CountryName, Territory> territories = gameState.getTerritories();
		
		switch(player.getLevel()) {
			case EASY:
				return getRandomOwnedCountryName(gameState, player);
			case CASUAL:
				if(Math.random() < 0.5) {
					return getRandomOwnedCountryName(gameState, player);
				}
				else {
					ArrayList<Territory> threeMostOuterTerritories = getThreeMostOuterTerritories(territories.values(), player);
					threeMostOuterTerritories.forEach(x -> System.out.println(x.getCountryName()));
					double rand = Math.random();
					// Country with the most neighbouring countries gets more probability
					if(rand <= 0.4) {
						return threeMostOuterTerritories.get(0).getCountryName();
					}
					else if(rand <= 0.7) {
						return threeMostOuterTerritories.get(1).getCountryName();
					}
					else {
						return threeMostOuterTerritories.get(2).getCountryName();
					}
				}
			case HARD:
				ArrayList<Territory> threeMostOuterTerritories = getThreeMostOuterTerritories(territories.values(), player);
				threeMostOuterTerritories.forEach(x -> System.out.println(x.getCountryName()));
				double rand = Math.random();
				// Country with the most neighbouring countries gets more probability
				if(rand <= 0.4) {
					return threeMostOuterTerritories.get(0).getCountryName();
				}
				else if(rand <= 0.7) {
					return threeMostOuterTerritories.get(1).getCountryName();
				}
				else {
					return threeMostOuterTerritories.get(2).getCountryName();
				}
			default:
				return null;
		}
	}
	
	public static Pair<CountryName, Integer> chooseTerritoryToReinforce(GameState gameState, PlayerAI player) {
		HashMap<Integer, Integer> troopsLeft = gameState.getPlayerTroopsLeft();
		switch(player.getLevel()) {
			case EASY:
				int randNumbTroops = (int) (Math.random() * (troopsLeft.get(player.getID()) - 1) + 1);
				return new Pair<CountryName, Integer>(getRandomOwnedCountryName(gameState, player), randNumbTroops);
			case CASUAL:
				int randomNumTroops = (int) (Math.random() * (troopsLeft.get(player.getID()) - 1) + 1);
				List<Territory> list = new ArrayList<>();
				for(Territory t : gameState.getTerritories().values()) {
					if(t.getOwnedByPlayer().getID() == player.getID()) {
						list.add(t);
					}
				}
				int randomCountryIndex = (int) (Math.random() * (list.size() - 1) + 1);
				return new Pair<CountryName, Integer>(list.get(randomCountryIndex).getCountryName(), randomNumTroops);
			case HARD:
				ArrayList<Territory> threeMostOuterCountries = getThreeMostOuterTerritories(gameState.getTerritories().values(), player);
				int min = Integer.MAX_VALUE;
				CountryName minCountryName = CountryName.Afghanistan;
				for(Territory t : threeMostOuterCountries) {
					if(t.getNumberOfTroops() < min) {
						min = t.getNumberOfTroops();
						minCountryName = t.getCountryName();
					}
				}
				return new Pair<CountryName, Integer>(minCountryName, troopsLeft.get(player.getID()));
			default:
				return null;
		}
	}
	
	private static ArrayList<Territory> getThreeMostOuterCountries(GameState gameState, PlayerAI player) {
		ArrayList<Territory> list = new ArrayList<>();
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
			count = 0;
		}
		list.add(terr);
		min = Integer.MAX_VALUE;
		for(Entry<CountryName, Territory> set : gameState.getTerritories().entrySet()) {
			if(set.getValue().getCountryName() != list.get(0).getCountryName()) {
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
			count = 0;
		}
		list.add(terr);
		min = Integer.MAX_VALUE;
		for(Entry<CountryName, Territory> set : gameState.getTerritories().entrySet()) {
			if(set.getValue().getCountryName() != list.get(0).getCountryName() && set.getValue().getCountryName() != list.get(1).getCountryName()) {
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
			count = 0;
		}
		list.add(terr);
		return list;
	}
	
	private static ArrayList<Territory> getThreeMostOuterTerritories(Collection<Territory> territories, PlayerAI player) {
		List<Territory> territoriesCopy = new ArrayList<>();
		for(Territory t : territories) {
			territoriesCopy.add(t);
		}
		ArrayList<Territory> mostOuterTerritories = new ArrayList<>();
		
		mostOuterTerritories.add(mostOuterCountry2(territoriesCopy, player));
		territoriesCopy.removeIf(x -> x.getCountryName() == mostOuterTerritories.get(0).getCountryName());
		mostOuterTerritories.add(mostOuterCountry2(territoriesCopy, player));
		territoriesCopy.removeIf(x -> 
			x.getCountryName() == mostOuterTerritories.get(0).getCountryName() ||
					x.getCountryName() == mostOuterTerritories.get(1).getCountryName());
		mostOuterTerritories.add(mostOuterCountry2(territoriesCopy, player));
		
		return mostOuterTerritories;
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
		Object[] territories = gameState.getTerritories().keySet().stream()
				.filter(x -> gameState.getTerritories().get(x).getOwnedByPlayer().equals(player))
				.toArray();
		Random generator = new Random();
		CountryName randomValue = (CountryName) territories[generator.nextInt(territories.length)];
		return randomValue;
	}
	
	//owned territory with the fewest owned neighbours -> most outer country
	public static Territory mostOuterCountry(GameState gameState, PlayerAI player) {
		
			int max = Integer.MIN_VALUE;
			int count = 0;
			Territory terr = null;
			for(Entry<CountryName, Territory> set : gameState.getTerritories().entrySet()) {
				if(set.getValue().getOwnedByPlayer().getID() == player.getID() && set.getValue().getNumberOfTroops() > 1) {
					for(Territory neighbour : set.getValue().getNeighboringTerritories()) {
						if(neighbour.getOwnedByPlayer().getID() != player.getID()) {
							count++;
						}
					}
					if(count > max) {
						max = count;
						terr = set.getValue();
						System.out.println("Set most outter territory to " + terr.getCountryName().toString());
					}
				}
				count = 0;
			}
			return terr;
	}
	
	public static Territory mostOuterCountry2(Collection<Territory> territories, PlayerAI player) {
		int max = Integer.MIN_VALUE;
		int count = 0;
		Territory terr = null;
		for(Territory t : territories) {
			if(t.getOwnedByPlayer().getID() == player.getID()) {
				for(Territory neighbour : t.getNeighboringTerritories()) {
					if(neighbour.getOwnedByPlayer().getID() != player.getID()) {
						count++;
					}
				}
				if(count > max) {
					max = count;
					terr = t;
				}
			}
			count = 0;
		}
		return terr;
}
	
	//owned territory with the most owned neighbours -> most inner country
	public static Territory mostInnerCountry(Collection<Territory> territories, PlayerAI player) {
		
		int max = Integer.MIN_VALUE;
		int count = 0;
		Territory terr = null;
		for(Territory t : territories) {
			if(t.getOwnedByPlayer().getID() == player.getID() && t.getNumberOfTroops() > 1) {
				for(Territory neighbour : t.getNeighboringTerritories()) {
					if(neighbour.getOwnedByPlayer().getID() == player.getID()) {
						count++;
					}
				}
				if(count > max) {
					max = count;
					terr = t;
					System.out.println("Set most inner territory to " + terr.getCountryName().toString());
				}
			}
			count = 0;
		}
		return terr;
	}
	
	private static ArrayList<Territory> getThreeMostInnerTerritories(Collection<Territory> territories, PlayerAI player) {
		List<Territory> territoriesCopy = new ArrayList<>();
		for(Territory t : territories) {
			territoriesCopy.add(t);
		}
		ArrayList<Territory> mostOuterTerritories = new ArrayList<>();
		
		mostOuterTerritories.add(mostOuterCountry2(territoriesCopy, player));
		territoriesCopy.removeIf(x -> x.getCountryName() == mostOuterTerritories.get(0).getCountryName());
		mostOuterTerritories.add(mostOuterCountry2(territoriesCopy, player));
		territoriesCopy.removeIf(x -> 
			x.getCountryName() == mostOuterTerritories.get(0).getCountryName() ||
					x.getCountryName() == mostOuterTerritories.get(1).getCountryName());
		mostOuterTerritories.add(mostOuterCountry2(territoriesCopy, player));
		
		return mostOuterTerritories;
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
			if(Math.random() < 0.5) {
				if(Math.random() < 0.5) {
					return true;
				}else {
					return false;
				}
			}
			else {
				return isNeighbourWithLessTroops(gameState, player);
			}
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
		Territory attacker = null;
		
		switch(player.getLevel()) {
			case EASY:
				attacker = mostOuterCountry(gameState, player);
				return new Pair(attacker.getCountryName(), getNeighbourWithHighestNumb(attacker, player));
			case CASUAL:
				return null;
			case HARD:
				ArrayList<Territory> ownTerritories = getAllOwnTerritories(gameState, player);
				Collections.sort(ownTerritories, (t1, t2) -> t2.getNumberOfTroops() - t1.getNumberOfTroops());
				A: for(Territory t : ownTerritories) {
					for(Territory tN : t.getNeighboringTerritories()) {
						if(tN.getOwnedByPlayer().getID() != player.getID()) {
							attacker = t;
							break A;
						}
					}
				}
				return new Pair(attacker.getCountryName(), getNeighbourWithLowestNumb(attacker, player));
			default:
				return null;
		}
	}
	
	private static ArrayList<Territory> getAllOwnTerritories(GameState gameState, PlayerAI player) {
		ArrayList<Territory> list = new ArrayList<Territory>();
		for(Entry<CountryName, Territory> set : gameState.getTerritories().entrySet()) {
			if(set.getValue().getOwnedByPlayer().getID() == player.getID()) {
				list.add(set.getValue());
			}
		}
		return list;
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
	
	//gets the neighbour with the lowest number of troops
	public static CountryName getNeighbourWithLowestNumb(Territory territory, PlayerAI player) {
		int min = Integer.MAX_VALUE;
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
	
	private static int chooseTroopsToSendToConqueredTerritoryHard(Territory oldTerritory, Territory newTerritory, PlayerAI player) {
		int numNeighbourOldTerritories = 0;
		int numNeighbourNewTerritories = 0;
		
		int numTroopsNeighbourOldTerritories = 0;
		int numTroopsNeighbourNewTerritories = 0;
		for(Territory t : oldTerritory.getNeighboringTerritories()) {
			if(t.getOwnedByPlayer().getID() != player.getID()) {
				numNeighbourOldTerritories++;
				numTroopsNeighbourOldTerritories += t.getNumberOfTroops();
			}
		}
		for(Territory t : newTerritory.getNeighboringTerritories()) {
			if(t.getOwnedByPlayer().getID() != player.getID()) {
				numNeighbourNewTerritories++;
				numTroopsNeighbourNewTerritories += t.getNumberOfTroops();
			}
		}
		if(numNeighbourOldTerritories == 0 && numNeighbourNewTerritories != 0) {
			return oldTerritory.getNumberOfTroops() - 1;
		}
		else if(numNeighbourOldTerritories != 0 && numNeighbourNewTerritories == 0) {
			return 1;
		}
		else if(numNeighbourOldTerritories != 0 && numNeighbourNewTerritories != 0) {
			double sum = numTroopsNeighbourOldTerritories + numTroopsNeighbourNewTerritories;
			double oldRatio = numTroopsNeighbourOldTerritories / sum;
			double newRatio = 1 - oldRatio;
			int tmp = (int) (newRatio * oldTerritory.getNumberOfTroops());
            if(tmp < 1) {
                return 1;
            }
            else {
                return tmp;
            }
		}
		return 1;
	}
	
	public static int chooseTroopsToSendToConqueredTerritory(Territory oldTerritory, Territory newTerritory, PlayerAI player) {
		int troopsRandom = (int)(Math.random() * oldTerritory.getNumberOfTroops() - 1);
		troopsRandom = troopsRandom < 1 ? 1 : troopsRandom;
		switch(player.getLevel()) {
		case EASY:
			return troopsRandom;
		case CASUAL:
			if(Math.random() < 0.5) {
				return troopsRandom;
			} else {
				return chooseTroopsToSendToConqueredTerritoryHard(oldTerritory, newTerritory, player);
			}
		case HARD:
			return chooseTroopsToSendToConqueredTerritoryHard(oldTerritory, newTerritory, player);
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
			if(Math.random() < 0.5) {
				if(Math.random() < 0.5) {
					return true;
				}else {
					return false;
				}
			}
			else {
				ArrayList<Territory> territoriesOwnedByOwn = countriesSurroundedByOwnedCountries(gameState, player);
				if(territoriesOwnedByOwn != null) {
					for(Territory territory : territoriesOwnedByOwn) {
						if(territory.getNumberOfTroops() > 1) {
							return true;
						}
					}
				}
				return false;
			}
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
	
	private static Pair<CountryName, CountryName>  chooseTerritoriesPairFortifyHard(GameState gameState, PlayerAI player){
		gameState.getTerritories().values().forEach(x -> System.out.println(x.getCountryName()+ ", " + x.getOwnedByPlayer().getID()));
		CountryName fromCountry = null;
		ArrayList<Territory> threeMostInner = getThreeMostInnerTerritories(gameState.getTerritories().values(), player);
		int max = 0;
		for(Territory territory : threeMostInner) {
			if(territory.getNumberOfTroops() > max) {
				max = territory.getNumberOfTroops();
				fromCountry = territory.getCountryName();
			}
		}
		return new Pair(fromCountry, mostOuterCountry2(gameState.getTerritories().values(), player).getCountryName());
	}
	
	public static Pair<CountryName, CountryName>  chooseTerritoriesPairFortify(GameState gameState, PlayerAI player){
		switch(player.getLevel()) {
		case EASY:
			return new Pair(getRandomOwnedCountryName(gameState, player), getRandomOwnedCountryName(gameState, player));
		case CASUAL:
			if(Math.random() < 0.5) {
				return new Pair(getRandomOwnedCountryName(gameState, player), getRandomOwnedCountryName(gameState, player));
			}
			else {
				return chooseTerritoriesPairFortifyHard(gameState, player);
			}
		case HARD:
			return chooseTerritoriesPairFortifyHard(gameState, player);
		default:
			return null;
		}
	}
	
	public static int chooseTroopsToSendFortify(Territory territory, PlayerAI player) {
		switch(player.getLevel()) {
		case EASY:
			return (int) (Math.random() * territory.getNumberOfTroops() - 1) + 1;
		case CASUAL:
			if(Math.random() < 0.5) {
				return (int) (Math.random() * territory.getNumberOfTroops() - 1) + 1;
			}
			else {
				return territory.getNumberOfTroops() - 1;
			}
		case HARD:
			return territory.getNumberOfTroops() - 1;
		default:
			return 0;
		}
	}
	public static int chooseTroopsToAttackWith(Territory territory, PlayerAI player, GameState gameState) {
		// TODO Auto-generated method stub
		return gameState.getTerritories().get(territory.getCountryName()).getNumberOfTroops() - 1;
	}
}