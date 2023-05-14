package network.messages;

import game.models.Lobby;

public class MessageinLobby extends Message {

  /**
   * 
   */
  private Lobby lobby;
  private String message;
  private static final long serialVersionUID = 1L;

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
