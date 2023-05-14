package network.messages;

import game.models.CountryName;
import game.models.Lobby;
import gameState.GameState;

public class MessageGUIconquerCountry extends Message {
	/**
	 * Author: dignatov
	 *
	 * The MessageGUIconquerCountry class represents a message used to convey the conquering of a country in the game GUI.
	 * It includes the game state, the name of the conquered country, player ID, number of troops, and the associated lobby.
	 */
  private static final long serialVersionUID = 1L;

  public GameState getGameState() {
    return gameState;
  }

  public CountryName getCountry() {
    return country;
  }

  public int getId() {
    return id;
  }

  public int getTroops() {
    return troops;
  }

  private GameState gameState;
  private CountryName country;
  private int id;
  private int troops;
  private Lobby lobby;
  /**
   * Constructs a MessageGUIconquerCountry object with the specified game state, country name, player ID,
   * number of troops, and lobby.
   *
   * @param gameState The game state associated with the message
   * @param country The name of the conquered country
   * @param id The ID of the player who conquered the country
   * @param troops The number of troops used for conquering
   * @param clientsLobby The associated lobby
   */
  public MessageGUIconquerCountry(GameState gameState, CountryName country, int id, int troops,
      Lobby clientsLobby) {
    super(MessageType.MessageGUIconquerCountry);
    this.country = country;
    this.gameState = gameState;
    this.id = id;
    this.troops = troops;
    this.lobby = clientsLobby;
  }

  public Lobby getLobby() {
    return lobby;
  }

}
