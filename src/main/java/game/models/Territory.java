package game.models;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Territory class handles the territories in the game as an object
 * @author srogalsk
 *
 */

public class Territory {
	
	private Player ownedByPlayer;
	private final Continent continent;
	private int numberOfTroops;
	private final CountryName countryName;
	private ArrayList<Territory> neighboringTerritories = new ArrayList<>();
	
	public Territory(CountryName countryName, Continent continent) {
		this.countryName = countryName;
		this.continent = continent;
	}
	
	
	
	/**Getters*/
	public Player getOwnedByPlayer() {
		return ownedByPlayer;
	}
	public int getNumberOfTroops() {
		return numberOfTroops;
	}
	public Continent getContinent() {
		return continent;
	}
	public CountryName getCountryName() {
		return countryName;
	}
	public ArrayList<Territory> getNeighboringTerritories() {
		return neighboringTerritories;
	}
	
	/**Setters*/
//	public void setNumberOfTroops(int numberOfTroops) {
//		this.numberOfTroops = numberOfTroops;
//	}
	public void addNumberOfTroops(int numberOfTroops) {
		this.numberOfTroops += numberOfTroops;
	}
	public void removeNumberOfTroops(int numberOfTroops) {
		this.numberOfTroops -= numberOfTroops;
	}
	public void setOwnedByPlayer(Player ownedByPlayer) {
		this.ownedByPlayer = ownedByPlayer;
	}
	public void setNeighboringTerritories(ArrayList<Territory> territoryNeighbours) {
		this.neighboringTerritories = territoryNeighbours;
	}
	
}
