package network.messages;

import game.models.Lobby;
import gameState.GameState;

/**
 * The MessageGUIRollInitalDice class represents a message used to indicate the result of rolling
 * the initial dice in the game GUI. It includes the game state, the ID of the player, the rolled
 * value, and the associated lobby.
 * 
 * </p>
 * author dignatov
 */
public class MessageGuiRollInitalDice extends Message {

  private static final long serialVersionUID = 1L;
  GameState gameState;

  private int id;
  private int value;
  private Lobby lobby;

  /**
   * Constructs a MessageGUIRollInitalDice object with the specified game state, player ID, rolled
   * value, and associated lobby.
   *
   * @param gameState The game state associated with the message
   * @param id The ID of the player
   * @param value The rolled value
   * @param lobby The associated lobby
   */
  public MessageGuiRollInitalDice(GameState gameState, int id, int value, Lobby lobby) {
    super(MessageType.MessageGUIRollInitalDice);
    this.gameState = gameState;
    this.id = id;
    this.value = value;
    this.lobby = lobby;
  }

  public int getId() {
    return id;
  }

  public GameState getGameState() {
    return gameState;
  }

  public int getValue() {
    return value;
  }

  public Lobby getLobby() {
    return lobby;
  }

}
