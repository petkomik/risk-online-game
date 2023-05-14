package network.messages;

import game.models.CountryName;
import game.models.Lobby;
import gameState.GameState;

/**
 * The MessageGUIpossessCountry class represents a message used to indicate the possession of a
 * country in the game GUI. It includes the game state, the country name, the player ID, the number
 * of troops left, and the associated lobby.
 * 
 * @author dignatov
 */
public class MessageGUIpossessCountry extends Message {

  private static final long serialVersionUID = 1L;
  private GameState gameState;
  private CountryName country;
  private int id;
  private int troopsLeft;
  private Lobby lobby;

  /**
   * Constructs a MessageGUIpossessCountry object with the specified game state, country name,
   * player ID, number of troops left, and lobby.
   *
   * @param gameState The game state associated with the message
   * @param country The name of the possessed country
   * @param id The ID of the player possessing the country
   * @param troopsLeft The number of troops left in the country
   * @param clientsLobby The associated lobby
   */
  public MessageGUIpossessCountry(GameState gameState, CountryName country, int id, int troopsLeft,
      Lobby clientsLobby) {
    super(MessageType.MessageGUIpossessCountry);
    this.country = country;
    this.id = id;
    this.troopsLeft = troopsLeft;
    this.gameState = gameState;
    this.lobby = clientsLobby;
  }

  public GameState getGameState() {
    return gameState;
  }

  public CountryName getCountry() {
    return country;
  }

  public int getId() {
    return id;
  }

  public int getTroopsLeft() {
    return troopsLeft;
  }

  public Lobby getLobby() {
    return lobby;
  }

}
