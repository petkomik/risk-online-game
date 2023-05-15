package network.messages;

import game.models.Lobby;
import gameState.GameState;

/**
 * The MessageGUIupdateRanks class represents a message used to update player ranks in the game GUI.
 * It includes the game state, an array of ranks, and the lobby associated with the message.
 *
 * @author dignatov
 */
public class MessageGuiupdateRanks extends Message {

  private static final long serialVersionUID = 1L;

  private GameState gameState;
  int[] ranks;

  private Lobby lobby;

  /**
   * Constructs a MessageGUIupdateRanks object with the specified game state, ranks, and lobby.
   *
   * @param gameState The game state associated with the message
   * @param ranks The array of player ranks
   * @param clientsLobby The lobby associated with the message
   */
  public MessageGuiupdateRanks(GameState gameState, int[] ranks, Lobby clientsLobby) {
    super(MessageType.MessageGUIupdateRanks);
    this.gameState = gameState;
    this.ranks = ranks;
    this.lobby = clientsLobby;
  }

  public GameState getGameState() {
    return gameState;
  }

  public int[] getRanks() {
    return ranks;
  }

  public Lobby getLobby() {
    return lobby;
  }

}
