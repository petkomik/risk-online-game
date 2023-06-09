package network.messages;

import game.models.Lobby;
import game.state.GameState;
import game.state.Phase;

/**
 * The MessageGUIsetPhase class represents a message used to set the current phase in the game GUI.
 * It includes the game state, the phase, and the lobby information.
 *
 * @author dignatov
 */
public class MessageGuisetPhase extends Message {

  private static final long serialVersionUID = 1L;
  private GameState gameState;
  private Lobby lobby;
  private Phase phase;

  /**
   * Constructs a MessageGUIsetPhase object with the specified game state, phase, and lobby
   * information.
   *
   * @param phase The phase to be set in the GUI
   * @param clientsLobby The lobby information
   */
  public MessageGuisetPhase(GameState gameState, Phase phase, Lobby clientsLobby) {
    super(MessageType.MessageGUIsetPhase);
    this.gameState = gameState;
    this.phase = phase;
    this.lobby = clientsLobby;
  }

  public Lobby getLobby() {
    return lobby;
  }

  public GameState getGameState() {
    return gameState;
  }

  public Phase getPhase() {
    return phase;
  }
}
