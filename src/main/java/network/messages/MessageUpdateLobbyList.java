package network.messages;

import game.models.Lobby;
import java.util.HashMap;

/**
 * MessageUpdateLobbyList class represents a message for updating the list of lobbies. It contains a
 * HashMap of lobby names mapped to their corresponding Lobby objects, as well as an ID for the
 * recipient of the message.
 *
 * @author dignatov
 */
public class MessageUpdateLobbyList extends Message {


  private static final long serialVersionUID = 1L;
  private HashMap<String, Lobby> lobbyList;

  public int getIdTo() {
    return idTo;
  }

  public void setIdTo(int idTo) {
    this.idTo = idTo;
  }

  private int idTo;

  /**
   * The MessageUpdateLobbyList class represents a message used to update the lobby list in the game
   * GUI. It includes the updated lobby list and the ID of the recipient.
   *
   * @param lobbyList The updated lobby list
   * @param idTo The ID of the recipient
   */
  public MessageUpdateLobbyList(HashMap<String, Lobby> lobbyList, int idTo) {
    super(MessageType.MessageUpdateLobbyList);
    this.idTo = idTo;
    this.lobbyList = lobbyList;

  }

  public HashMap<String, Lobby> getLobbyList() {
    return lobbyList;
  }

}
