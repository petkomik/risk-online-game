package network.messages;

import game.models.Lobby;

/**
 * The MessageinLobby class represents a message used to send a message within a lobby. It includes
 * the lobby and the message content.
 *
 * @author dignatov
 */
public class MessageinLobby extends Message {

  private Lobby lobby;
  private String message;
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a MessageinLobby object with the specified lobby and message.
   *
   * @param lobby The lobby associated with the message
   * @param messageToBeSend The message content
   */
  public MessageinLobby(Lobby lobby, String messageToBeSend) {
    super(MessageType.MessageinLobby);
    this.lobby = lobby;
    this.message = messageToBeSend;

  }

  public Lobby getLobby() {
    return lobby;
  }

  public void setLobby(Lobby lobby) {
    this.lobby = lobby;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

}
