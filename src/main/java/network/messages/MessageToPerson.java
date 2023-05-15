package network.messages;

import database.Profile;

/**
 * The MessageToPerson class represents a message sent to a specific person. It can contain various
 * types of messages and is used for direct communication between clients.
 *
 * @author dignatov
 */
public class MessageToPerson extends Message {

  private static final long serialVersionUID = 1L;

  String stringMessage;
  private Profile fromProfile;
  private Profile toProfile;
  private boolean inaLobby;

  /**
   * Constructs a MessageToPerson object with a string message, sender and recipient profiles, and
   * lobby information.
   *
   * @param message The string message to be sent.
   * @param fromProfile The profile of the sender.
   * @param toProfile The profile of the recipient.
   * @param inalobby Indicates whether the message is sent within a lobby.
   */
  public MessageToPerson(String message, Profile fromProfile, Profile toProfile, boolean inalobby) {
    super(MessageType.MessageToPerson);
    this.fromProfile = fromProfile;
    this.toProfile = toProfile;
    this.stringMessage = message;
    this.setInaLobby(inalobby);
  }

  /**
   * Constructs a MessageToPerson object with a string message, sender and recipient profiles, and
   * lobby information.
   *
   * @param message The string message to be sent.
   * @param fromProfile The profile of the sender.
   * @param toProfile The profile of the recipient.
   */

  public MessageToPerson(String message, Profile fromProfile, Profile toProfile) {
    super(MessageType.MessageToPerson);
    this.fromProfile = fromProfile;
    this.toProfile = toProfile;
    this.stringMessage = message;

  }

  public boolean isInaLobby() {
    return inaLobby;
  }

  public void setInaLobby(boolean inaLobby) {
    this.inaLobby = inaLobby;
  }

  public String getStringMessage() {
    return stringMessage;
  }

  public Profile getFromProfile() {
    return fromProfile;
  }

  public Profile getToProfile() {
    return toProfile;
  }
}
