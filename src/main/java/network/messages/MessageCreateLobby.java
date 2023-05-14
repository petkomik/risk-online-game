package network.messages;

import game.models.Lobby;

/**
 * This message informs players that a new lobby has been created.
 *
 * @author dignatov
 */


public class MessageCreateLobby extends Message {
  private static final long serialVersionUID = 1L;
  private int playerId;
  private Lobby lobby;
  private Message message;

  /**
   * Constructs a MessageCreateLobby object with the given profile.
   *
   * @param playerId of the player and aLobby which is created
   */
  public MessageCreateLobby(int playerId, Lobby lobby) {
    super(MessageType.MessageCreateLobby);
    this.playerId = playerId;
    this.lobby = lobby;
  }

  /**
   * Constructs a MessageCreateLobby object with the given profile.
   *
   * @param lobby which is created
   */
  public MessageCreateLobby(Lobby lobby) {
    super(MessageType.MessageCreateLobby);
    this.lobby = lobby;
  }

  public MessageCreateLobby() {
    super(MessageType.MessageCreateLobby);
  }

  public int getPlayerId() {
    return playerId;
  }

  public Lobby getLobby() {
    return lobby;
  }

  public Message getMessage() {
    return message;
  }

}
