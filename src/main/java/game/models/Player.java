package game.models;

import java.util.ArrayList;
import java.util.HashMap;

import game.GameController;

/**
 * Player class to model the player entity
 *
 * @author jorohr
 */

public class Player {
	
	private String name;
	private String color;
	private int id;
	private int rank;
	private HashMap<CountryName, Territory> ownedCountries;
	private ArrayList<Continent> ownedContinents;
	private int sumOfAllTroops;
	private int troopsAvailable;
	private ArrayList<Card> cards;
	private boolean canContinuePlaying = true;
	private volatile boolean cardsTurningInPhase;
	private volatile boolean initialPlacementPhase;
	private volatile boolean preparationPhase;
	private volatile boolean attackPhase;
	private volatile boolean fortificationPhase;
	
	public Player(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	
	public void incrementRank() {
		rank+=1;
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	public int getTroopsAvailable() {
		return troopsAvailable;
	}
	public void setTroopsAvailable(int troopsAvailable) {
		this.troopsAvailable = troopsAvailable;
	}
	public void addTroopsAvailable(int number) {
		this.troopsAvailable += number;
	}
	public int getSumOfAllTroops() {
		return sumOfAllTroops;
	}
	public void setSumOfAllTroops(int sumOfAllTroops) {
		this.sumOfAllTroops = sumOfAllTroops;
	}
	public void removeTroopsAvailable(int numberOfTroops) {
		this.troopsAvailable -= numberOfTroops;
	}


	public HashMap<CountryName, Territory> getOwnedCountries() {
		return ownedCountries;
	}

	public void addAndUpdateOwnedCountries(Territory territory) {
		if(territory.getOwnedByPlayer() != null && territory.getOwnedByPlayer() != this) {
			territory.getOwnedByPlayer().getOwnedCountries().remove(territory.getCountryName(), territory);
			territory.getOwnedByPlayer().updateOwnedContinents(GameController.getContinents());
		}
		territory.setOwnedByPlayer(this);
 		this.ownedCountries.put(territory.getCountryName(),territory);
		updateOwnedContinents(GameController.getContinents());
	}
	
	public ArrayList<Continent> getOwnedContinents() {
		return ownedContinents;
	}
	
	private void updateOwnedContinents(HashMap<Continent, ArrayList<Territory>> continents) {
		 // Create a HashMap to store the number of territories owned by the player in each continent
	    HashMap<Continent, Integer> territoriesOwnedInContinent = new HashMap<>();
	    for (Continent continent : continents.keySet()) {
	        territoriesOwnedInContinent.put(continent, 0);
	    }
	    
	    
	    // Count the number of territories owned by the player in each continent
	    for (Territory territory : this.getOwnedCountries().values()) {
	        Continent continent = territory.getContinent();
	        int numTerritoriesOwned = territoriesOwnedInContinent.get(continent);
	        territoriesOwnedInContinent.put(continent, numTerritoriesOwned + 1);
	    }
	    
	    // Check if the player owns all territories in each continent and update the player's ownedContinents accordingly
	    for (Continent continent : continents.keySet()) {
	        ArrayList<Territory> territoriesInContinent = continents.get(continent);
	        int numTerritoriesInContinent = territoriesInContinent.size();
	        int numTerritoriesOwned = territoriesOwnedInContinent.get(continent);
	        if (numTerritoriesOwned == numTerritoriesInContinent) {
	            ownedContinents.add(continent);
	        } else {
	            ownedContinents.remove(continent);
	        }
	    }
	}


	public ArrayList<Card> getCards() {
		return cards;
	}


	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}


	public boolean isCanContinuePlaying() {
		return canContinuePlaying;
	}


	public void setCanContinuePlaying(boolean stillActive) {
		this.canContinuePlaying = stillActive;
	}


	public boolean isCardsTurningInPhase() {
		return cardsTurningInPhase;
	}


	public void setCardsTurningInPhase(boolean cardsTurningInPhase) {
		this.cardsTurningInPhase = cardsTurningInPhase;
	}


	public boolean isInitialPlacementPhase() {
		return initialPlacementPhase;
	}


	public void setInitialPlacementPhase(boolean initialPlacementPhase) {
		this.initialPlacementPhase = initialPlacementPhase;
	}


	public boolean isPreparationPhase() {
		return preparationPhase;
	}


	public void setPreparationPhase(boolean preparationPhase) {
		this.preparationPhase = preparationPhase;
	}


	public boolean isAttackPhase() {
		return attackPhase;
	}


	public void setAttackPhase(boolean attackPhase) {
		this.attackPhase = attackPhase;
	}


	public boolean isFortificationPhase() {
		return fortificationPhase;
	}


	public void setFortificationPhase(boolean fortificationPhase) {
		this.fortificationPhase = fortificationPhase;
	}
	
}
