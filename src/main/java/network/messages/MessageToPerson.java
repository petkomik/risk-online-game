package network.messages;

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

}
