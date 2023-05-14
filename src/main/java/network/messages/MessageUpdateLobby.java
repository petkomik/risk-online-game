package network.messages;

import game.models.Lobby;

/**
 * MessageUpdateLobby class represents a message for updating the lobby information.
 * It contains the updated lobby object.
 *
 * author dignatov
 */
public class MessageUpdateLobby extends Message {

   
  private Lobby lobby;

  private static final long serialVersionUID = 1L;

  public MessageUpdateLobby(Lobby lobby) {
    super(MessageType.MessageUpdateLobby);
    this.lobby = lobby;
  }

  public void setLobby(Lobby lobby) {
    this.lobby = lobby;
  }
  public Lobby getLobby() {
    return lobby;
  }


}
