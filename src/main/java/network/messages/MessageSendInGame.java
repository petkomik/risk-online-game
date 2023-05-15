package network.messages;

import database.Profile;
import game.models.Lobby;

/**
 * The MessageSendInGame class represents a message that is sent from a player within the game. It
 * contains the message content, the lobby where the game is being played, and the sender's profile.
 *
 * @author dignatov
 */
public class MessageSendInGame extends Message {

  private static final long serialVersionUID = 1L;

  private String message;
  private Lobby lobby;
  private Profile profile;

  /**
   * Constructs a MessageSendInGame object with the specified message, lobby, and sender's profile.
   *
   * @param message The message content
   * @param lobby The lobby where the game is being played
   * @param profile The sender's profile
   */
  public MessageSendInGame(String message, Lobby lobby, Profile profile) {
    super(MessageType.MessageSendInGame);
    this.message = message;
    this.lobby = lobby;
    this.profile = profile;
  }

  public Lobby getLobby() {
    return lobby;
  }

  public String getMessage() {
    return message;
  }

  public Profile getProfile() {
    return profile;
  }

}
