package network.messages;

import java.util.ArrayList;

import database.Profile;

public class MessageAllProfiles extends Message {
  private ArrayList<Profile> profiles;

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
