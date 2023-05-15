package network.messages;

import game.models.Lobby;

/**
 * The MessageReadyToPlay class represents a message indicating that a player is ready to play. It
 * is used to notify the server or other players about the player's readiness.
 *
 * @author dignatov
 */
public class MessageReadyToPlay extends Message {


  private static final long serialVersionUID = 1L;
  private Lobby lobby;

  /**
   * Constructs a MessageReadyToPlay object with the specified lobby.
   *
   * @param lobby The lobby associated with the ready player
   */
  public MessageReadyToPlay(Lobby lobby) {
    super(MessageType.MessageReadyToPlay);
    this.lobby = lobby;
  }

  public Lobby getLobby() {
    return lobby;
  }
}
