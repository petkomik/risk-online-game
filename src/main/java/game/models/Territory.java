package game.models;

/**
 * Territory class handles the territories in the game as an object
 * @author srogalsk
 *
 */

public class Territory {
	
	private Player ownedByPlayer;
	private Continent continent;
	private int numberOfTroops;
	private CountryName countryName;
	
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
	
	/**Setters*/
	public void setNumberOfTroops(int numberOfTroops) {
		this.numberOfTroops = numberOfTroops;
	}
	public void setOwnedByPlayer(Player ownedByPlayer) {
		this.ownedByPlayer = ownedByPlayer;
	}
	
}