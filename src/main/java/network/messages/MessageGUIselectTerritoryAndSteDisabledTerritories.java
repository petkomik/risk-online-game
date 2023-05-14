package network.messages;

import java.util.ArrayList;

import game.models.CountryName;
import gameState.GameState;
/**
 * The MessageGUIselectTerritoryAndSteDisabledTerritories class represents a message used to indicate the selection of a territory and provide a list of disabled territories in the game GUI.
 * It includes the game state, the selected territory, and a list of unreachable territories.
 * 
 * author dignatov
 */
public class MessageGUIselectTerritoryAndSteDisabledTerritories extends Message {

  private static final long serialVersionUID = 1L;

  GameState gameState;
  CountryName countryName;
  ArrayList<CountryName> unreachableCountries;
	/**
	 * Constructs a MessageGUIselectTerritoryAndSteDisabledTerritories object with the specified game state, selected territory, and list of unreachable territories.
	 *
	 * @param gameState The game state associated with the message
	 * @param countryName The selected territory
	 * @param unreachableCountries The list of unreachable territories
	 */
  public MessageGUIselectTerritoryAndSteDisabledTerritories(GameState gameState,
      CountryName countryName, ArrayList<CountryName> unreachableCountries) {
    super(MessageType.MessageGUIselectTerritoryAndSteDisabledTerritories);
    this.gameState = gameState;
    this.countryName = countryName;
    this.unreachableCountries = unreachableCountries;
  }

  public GameState getGameState() {
    return gameState;
  }

  public CountryName getCountryName() {
    return countryName;
  }

  public ArrayList<CountryName> getUnreachableCountries() {
    return unreachableCountries;
  }


}
