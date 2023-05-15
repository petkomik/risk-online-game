package game.models;

import general.Parameter;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Territory class handles the territories in the game as an object.
 *
 * @author srogalsk
 *
 */

public class Territory implements Serializable {

  private static final long serialVersionUID = 1L;

  private Player ownedByPlayer;
  private final Continent continent;
  private int numberOfTroops;
  private final CountryName countryName;
  private ArrayList<Territory> neighboringTerritories;
  private String addressToPng;

  /**
   * Constructor for building an territory object.
   *
   * @param countryName of the Territory
   * @param continent of the Territory
   */
  public Territory(CountryName countryName, Continent continent) {
    this.countryName = countryName;
    this.continent = continent;
    neighboringTerritories = new ArrayList<>();
    addressToPng = Parameter.territoryPNGdir + continent.toString().toLowerCase() + "-"
        + countryName.toString().toLowerCase() + ".png";
  }

  /** Constructor for defensive copying. */
  public Territory(Territory territory) {
    this.ownedByPlayer =
        territory.getOwnedByPlayer() != null ? new PlayerSingle(territory.getOwnedByPlayer())
            : null;
    this.continent = territory.getContinent();
    this.numberOfTroops = territory.getNumberOfTroops();
    this.countryName = territory.getCountryName();
    // Defensive copy of neighboringTerritories
    this.neighboringTerritories = new ArrayList<>(territory.getNeighboringTerritories());
    this.addressToPng = territory.getAddressToPng();
  }

  public String getAddressToPng() {
    return addressToPng;
  }

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

  public void addNumberOfTroops(int numberOfTroops) {
    this.numberOfTroops += numberOfTroops;
  }

  public void setNumberOfTroops(int numberOfTroops) {
    this.numberOfTroops = numberOfTroops;
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
