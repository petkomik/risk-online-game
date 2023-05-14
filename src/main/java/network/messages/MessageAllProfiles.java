package network.messages;

import java.util.ArrayList;

import database.Profile;
/**
 * Represents a message containing a list of profiles.
 * Author: dignatov
 */

public class MessageAllProfiles extends Message {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Profile> profiles;

/**
 * Constructs a MessageAllProfiles object with the given list of profiles.
 *
 * @param profiles The list of profiles to be sent in the message
 */
  public MessageAllProfiles(ArrayList<Profile> profiles) {
    super(MessageType.MessageAllProfiles);
    this.setProfiles(profiles);
  }

  public ArrayList<Profile> getProfiles() {
    return profiles;
  }

  public void setProfiles(ArrayList<Profile> profiles) {
    this.profiles = profiles;
  }



}
