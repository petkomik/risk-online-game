package network.messages;

import game.models.Lobby;
import gameState.GameState;

/**
 * The MessageGUIsetCurrentPlayer class represents a message used to update the current player in
 * the game GUI. It includes the game state, the player ID, the number of troops left for the
 * player, and the lobby information.
 * 
 * author dignatov
 */
public class MessageGUIsetCurrentPlayer extends Message {

  private static final long serialVersionUID = 1L;
  private int id;
  private int troopsLeft;
  private GameState gameState;
  private Lobby lobby;


  /**
   * Constructs a MessageGUIsetCurrentPlayer object with the specified game state, player ID, number
   * of troops left, and lobby information.
   *
   * @param gameState The game state associated with the message
   * @param id The player ID
   * @param troopsLeft The number of troops left for the player
   * @param clientsLobby The lobby information
   */
  public MessageGUIsetCurrentPlayer(GameState gameState, int id, int troopsLeft,
      Lobby clientsLobby) {
    super(MessageType.MessageGUIsetCurrentPlayer);
    this.id = id;
    this.gameState = gameState;
    this.troopsLeft = troopsLeft;
    this.lobby = clientsLobby;
  }

  public int getId() {
    return id;
  }

  public GameState getGameState() {
    return gameState;
  }

  public int getTroopsLeft() {
    return troopsLeft;
  }

  public Lobby getLobby() {
    return lobby;
  }

}
