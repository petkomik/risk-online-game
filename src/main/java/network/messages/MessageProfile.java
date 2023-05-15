package network.messages;

import database.Profile;

/**
 * The MessageProfile class represents a message containing a profile. It is used to transmit
 * profile information.
 *
 * @author dignatov
 */
public class MessageProfile extends Message {

  private static final long serialVersionUID = 1L;
  private Profile profile;

  /**
   * Constructs a MessageProfile object with the specified profile.
   *
   * @param profile The profile to transmit
   */
  public MessageProfile(Profile profile) {
    super(MessageType.MessageProfile);
    this.profile = profile;

  }

  public Profile getProfile() {
    return profile;
  }

}
