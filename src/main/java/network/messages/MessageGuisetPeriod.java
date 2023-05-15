package network.messages;



import game.models.Lobby;
import gameState.GameState;
import gameState.Period;

/**
 * The MessageGUIsetPeriod class represents a message used to set the current period in the game
 * GUI. It includes the game state, the period, and the lobby information.
 *
 * @author dignatov
 */
public class MessageGuisetPeriod extends Message {

  private static final long serialVersionUID = 1L;
  private GameState gameState;
  private Period period;
  private Lobby lobby;

  /**
   * Constructs a MessageGUIsetPeriod object with the specified game state, period, and lobby
   * information.
   *
   * @param gameState The game state associated with the message
   * @param period The period to be set in the GUI
   * @param lobby The lobby information
   */
  public MessageGuisetPeriod(GameState gameState, Period period, Lobby lobby) {
    super(MessageType.MessageGUIsetPeriod);
    this.gameState = gameState;
    this.period = period;
    this.lobby = lobby;

  }

  public Lobby getLobby() {
    return lobby;
  }

  public GameState getGameState() {
    return gameState;
  }

  public Period getPeriod() {
    return period;
  }
}
