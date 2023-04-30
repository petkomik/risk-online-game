package game.gui;

import java.util.ArrayList;

import database.Profile;
import network.messages.Message;
import network.messages.MessageType;

public class MessageAllProfiles extends Message {
	private ArrayList<Profile> profiles ;
	
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
