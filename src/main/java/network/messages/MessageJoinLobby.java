package network.messages;

import game.models.Lobby;

/**
 * The MessageJoinLobby class represents a message used to join a lobby. It includes the lobby to
 * join.
 *
 * author dignatov
 */
public class MessageJoinLobby extends Message {

  private static final long serialVersionUID = 1L;
  private Lobby lobby;

  /**
   * Constructs a MessageJoinLobby object with the specified lobby to join.
   *
   * @param lobby The lobby to join
   */
  public MessageJoinLobby(Lobby lobby) {
    super(MessageType.MessageJoinLobby);
    this.lobby = lobby;
  }

  public Lobby getLobby() {
    return lobby;
  }

}
