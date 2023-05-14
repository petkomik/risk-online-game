package network.messages;

import game.models.CountryName;
import game.models.Lobby;
import gameState.GameState;
/**
 * The MessageGUIsetTroopsOnTerritoryAndLeft class represents a message used to set the number of troops on a territory and the number of troops left for a player in the game GUI.
 * It includes the game state, the country name, the number of troops on the territory, the number of troops left for the player, and the lobby information.
 *
 * author dignatov
 */
public class MessageGUIsetTroopsOnTerritoryAndLeft extends Message {

  private static final long serialVersionUID = 1L;
  private GameState gameState;
  private CountryName countryName;
  private int numTroopsOfCountry;
  private int numTroopsOfPlayer;
  private Lobby lobby;
  /**
   * Constructs a MessageGUIsetTroopsOnTerritoryAndLeft object with the specified game state, country name, number of troops on the territory, number of troops left for the player, and lobby information.
   *
   * @param gameState The game state associated with the message
   * @param countryName The name of the country where troops are being set
   * @param numTroopsOfCountry The number of troops on the territory
   * @param numTroopsOfPlayer The number of troops left for the player
   * @param lobby The lobby information
   */
  public MessageGUIsetTroopsOnTerritoryAndLeft(GameState gameState, CountryName countryName,
      int numTroopsOfCountry, int numTroopsOfPlayer, Lobby clientsLobby) {
    super(MessageType.MessageGUIsetTroopsOnTerritoryAndLeft);
    this.gameState = gameState;
    this.countryName = countryName;
    this.numTroopsOfCountry = numTroopsOfCountry;
    this.numTroopsOfPlayer = numTroopsOfPlayer;
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

  public int getNumTroopsOfPlayer() {
    return numTroopsOfPlayer;
  }

  public Lobby getLobby() {
    return lobby;
  }

}
