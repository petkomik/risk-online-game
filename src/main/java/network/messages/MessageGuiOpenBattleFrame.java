package network.messages;

import game.models.Battle;
import game.models.Lobby;
import gameState.GameState;

/**
 * The MessageGUIOpenBattleFrame class represents a message used to indicate the opening of the
 * battle frame in the game GUI. It includes the game state, the battle information, and the
 * associated lobby.
 *
 * @author dignatov
 */



public class MessageGuiOpenBattleFrame extends Message {

  private static final long serialVersionUID = 1L;
  private GameState gameState;
  private Battle battle;
  private Lobby lobby;

  /**
   * Constructs a MessageGUIOpenBattleFrame object with the specified game state, battle
   * information, and lobby.
   *
   * @param gameState The game state associated with the message
   * @param battle The battle information
   * @param clientsLobby The associated lobby
   */
  public MessageGuiOpenBattleFrame(GameState gameState, Battle battle, Lobby clientsLobby) {
    super(MessageType.MessageGUIOpenBattleFrame);
    this.gameState = gameState;
    this.battle = battle;
    this.lobby = clientsLobby;
  }

  public GameState getGameState() {
    return gameState;
  }

  public Battle getBattle() {
    return battle;
  }

  public Lobby getLobby() {
    return lobby;
  }

}
