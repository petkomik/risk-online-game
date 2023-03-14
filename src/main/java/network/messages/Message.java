package network.messages;

import java.io.Serializable;

public abstract class Message implements Serializable {
	private static final long serialVersionUID = 1L;

	private MessageType mType;
	
	public Message(MessageType type) {
		this.mType = type;
	}

	public MessageType getMessageType() {
		return this.mType;
	}
}
