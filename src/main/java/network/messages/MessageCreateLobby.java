package network.messages;

import game.models.Lobby;

/**
 * This Message informs players that a new lobby has been created.
 * 
 * @author dignatov
 */

public class MessageCreateLobby extends Message {
  private static final long serialVersionUID = 1L;
  private int playerID;
  private Lobby lobby;
  private Message message;

  /**
   * Constructs a MessageCreateLobby object with the given profile.
   *
   * @param playerID of the player and aLobby which is created
   */
  public MessageCreateLobby(int playerID, Lobby aLobby) {
    super(MessageType.MessageCreateLobby);
    this.playerID = playerID;
    this.lobby = aLobby;
  }

  /**
   * Constructs a MessageCreateLobby object with the given profile.
   *
   * @param aLobby which is created
   */
  public MessageCreateLobby(Lobby aLobby) {
    super(MessageType.MessageCreateLobby);
    lobby = aLobby;
  }

  public MessageCreateLobby() {
    super(MessageType.MessageCreateLobby);
  }

  public int getPlayerID() {
    return playerID;
  }

  public Lobby getLobby() {
    return lobby;
  }

  public Message getMessage() {
    return message;
  }

}
