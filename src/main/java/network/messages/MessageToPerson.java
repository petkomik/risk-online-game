package network.messages;

import database.Profile;
import network.Client;

public class MessageToPerson extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String to;
	Message message;
	String strinMessage;
	
	private Profile fromProfile;
	private Profile toProfile;
	private  boolean inALobby;
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public MessageToPerson(Message message ,String to) {
		super(MessageType.MessageToPerson);
		
		this.to = to;
		this.message = message;
	}
	public MessageToPerson(String message ,Profile fromProfile, Profile toProfile, boolean inAlobby) {
		super(MessageType.MessageToPerson);
		this.fromProfile = fromProfile;
		this.toProfile = toProfile;
		this.strinMessage = message;
		this.setInALobby(inAlobby);
	}
	
	public MessageToPerson(String message ,Profile fromProfile, Profile toProfile) {
		super(MessageType.MessageToPerson);
		this.fromProfile = fromProfile;
		this.toProfile = toProfile;
		this.strinMessage = message;
		
	}
	
	public MessageToPerson(String stringMessage, String username) {
		super(MessageType.MessageToPerson);
		this.strinMessage =  stringMessage;
		to=username;
	}
	
	public String getTo() {
		return to;
	}

	
	public void setTo(String to) {
		this.to = to;
	}
	public String getStringMessage() {
		return strinMessage;
	}
	public Profile getFromProfile() {
		return fromProfile;
	}
	public Profile getToProfile() {
		return toProfile;
	}
	public boolean isInALobby() {
		return inALobby;
	}
	public void setInALobby(boolean inALobby) {
		this.inALobby = inALobby;
	}

}
