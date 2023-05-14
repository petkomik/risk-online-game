package network.messages;

import java.util.ArrayList;

import database.Profile;

public class MessageConnect extends Message {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String playername;
  private Profile profile;
  private int idTo;

  public MessageConnect(Profile profile) {
    super(MessageType.Connect);
    playername = profile.getUserName();
    this.profile = profile;

  }

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
