package network.messages;

import game.models.CountryName;
import game.models.Lobby;
import gameState.GameState;
/**
 * The MessageGUIsetTroopsOnTerritory class represents a message used to set the number of troops on a territory in the game GUI.
 * It includes the game state, the country name, the number of troops, and the lobby information.
 *
 * author dignatov
 */
public class MessageGUIsetTroopsOnTerritory extends Message {

  private static final long serialVersionUID = 1L;
  private GameState gameState;
  private CountryName countryName;
  private int numTroopsOfCountry;
  private Lobby lobby;
  
  /**
   * Constructs a MessageGUIsetTroopsOnTerritory object with the specified game state, country name, number of troops, and lobby information.
   *
   * @param gameState The game state associated with the message
   * @param countryName The name of the country where troops are being set
   * @param numTroopsOfCountry The number of troops to set on the country
   * @param lobby The lobby information
   */

  public MessageGUIsetTroopsOnTerritory(GameState gameState, CountryName countryName,
      int numTroopsOfCountry, Lobby clientsLobby) {
    super(MessageType.MessageGUIsetTroopsOnTerritory);
    this.gameState = gameState;
    this.countryName = countryName;
    this.numTroopsOfCountry = numTroopsOfCountry;
    this.lobby = clientsLobby;
  }

  public GameState getGameState() {
    return gameState;
  }


  public CountryName getCountryName() {
    return countryName;
  }


  public int getNumTroopsOfCountry() {
    return numTroopsOfCountry;
  }

  public Lobby getLobby() {
    return lobby;
  }


}
