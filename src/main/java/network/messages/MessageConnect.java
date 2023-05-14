package network.messages;

import database.Profile;

public class MessageConnect extends Message {

	/**
	 * @author dignatov
	 *
	 * The MessageConnect class represents a message used for client connection.
	 * It includes the player's name, profile, and an optional ID for targeted connection.
	 */
	
  private static final long serialVersionUID = 1L;
  private String playername;
  private Profile profile;
  private int idTo;
  /**
   * Constructs a MessageConnect object with the given profile.
   *
   * @param profile The profile of the connecting player
   */
  public MessageConnect(Profile profile) {
    super(MessageType.Connect);
    playername = profile.getUserName();
    this.profile = profile;

  }
  /**
   * Constructs a MessageConnect object with the given profile and target ID.
   *
   * @param profileFrom The profile of the connecting player
   * @param idTo The ID of the target connection
   */
  public MessageConnect(Profile profileFrom, int idTo) {
    super(MessageType.Connect);
    this.idTo = idTo;
    this.profile = profileFrom;

  }

  public String getPlayername() {
    return playername;
  }

  public Profile getProfile() {
    return profile;
  }

  public int getIdTo() {
    return idTo;
  }

}
