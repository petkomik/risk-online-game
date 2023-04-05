package game.models;

import java.util.ArrayList;

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
	private ArrayList<Territory> ownedCountries;
	private ArrayList<Continent> ownedContinents;
	private int sumOfAllTroops;
	private int troopsAvailable;
	private int numberOfCardsTurnedIn;
	private ArrayList<Card> cards;

	
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
	public int getSumOfAllTroops() {
		return sumOfAllTroops;
	}
	public void setSumOfAllTroops(int sumOfAllTroops) {
		this.sumOfAllTroops = sumOfAllTroops;
	}


	public ArrayList<Territory> getOwnedCountries() {
		return ownedCountries;
	}

	public void addOwnedCountries(Territory territory) {
		this.ownedCountries.add(territory);
	}
	
	public ArrayList<Continent> getOwnedContinents() {
		return ownedContinents;
	}
	
	public void updateOwnedContinents() {
		
	}
	
}
