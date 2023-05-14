package network.messages;

import database.Profile;
import game.models.Player;

/**
 * The MessageSend class represents a message that is sent from a player to other players or the
 * lobby. It contains the message content, the sender's profile, and a flag indicating whether it is
 * intended for the lobby or other players.
 *
 * author dignatov
 */
public class MessageSend extends Message {

  private static final long serialVersionUID = 1L;

  private String message;
  private Profile profileFrom;
  private boolean forLobby;

  /**
   * Constructs a MessageSend object with the specified message, sender's profile, and target.
   *
   * @param message The message content
   * @param profileFrom The sender's profile
   * @param forLobby Indicates if the sender is in a lobby (true) or not(false)
   */
  public MessageSend(String message, Profile profileFrom, boolean forLobby) {
    super(MessageType.MessageSend);
    this.message = message;
    this.profileFrom = profileFrom;
    this.forLobby = forLobby;
  }



  public String getMessage() {
    return message;
  }

  public Profile getProfileFrom() {
    return profileFrom;
  }

  public boolean isForLobby() {
    return forLobby;
  }

}
