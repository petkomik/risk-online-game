package network.messages;

import database.Profile;
import network.Client;

public class MessageToPerson extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Client from;
	String to;
	Message message;
	String msg;
	private Profile fromProfile;
	private Profile toProfile;
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public MessageToPerson(Message message ,String to) {
		super(MessageType.MessageToPerson);
		this.from = from;
		this.to = to;
		this.message = message;
	}
	public MessageToPerson(String message ,Profile fromProfile, Profile toProfile) {
		super(MessageType.MessageToPerson);
		this.fromProfile = fromProfile;
		this.toProfile = toProfile;
		this.msg = message;
	}
	
	public MessageToPerson(String msg, String username) {
		super(MessageType.MessageToPerson);
		this.msg=msg;
		to=username;
	}
	
	public String getTo() {
		return to;
	}
	public Client getFrom() {
		return from;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getMsg() {
		return msg;
	}
	public Profile getFromProfile() {
		return fromProfile;
	}
	public Profile getToProfile() {
		return toProfile;
	}

}
