package network.messages;

import game.models.Lobby;
import game.state.GameState;

/**
 * The MessageGUIendBattle class represents a message used to indicate the end of a battle in the
 * game GUI.
 *
 * @author dignotv
 */

public class MessageGuiendBattle extends Message {

  private static final long serialVersionUID = 1L;

  private GameState gameState;
  private Lobby lobby;

  /**
   * Constructs a MessageGUIendBattle object with the specified game state and lobby.
   *
   * @param clientsLobby The associated lobby
   */
  public MessageGuiendBattle(GameState gameState, Lobby clientsLobby) {
    super(MessageType.MessageGUIendBattle);
    this.gameState = gameState;
    this.lobby = clientsLobby;
  }

  public GameState getGameState() {
    return gameState;
  }

  public Lobby getLobby() {
    return lobby;
  }


}
