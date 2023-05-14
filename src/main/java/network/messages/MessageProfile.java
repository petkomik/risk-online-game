package network.messages;

import database.Profile;

public class MessageProfile extends Message {

  private static final long serialVersionUID = 1L;
  private Profile profile;

  public MessageProfile(Profile profile) {
    super(MessageType.MessageProfile);
    this.profile = profile;

  }

  public Profile getProfile() {
    return profile;
  }

}
