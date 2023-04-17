package network.messages;

public class MessageServerCloseConnection extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String message;
	public MessageServerCloseConnection() {
		super(MessageType.MessageServerCloseConnection);
		this.message = message;
	}
	public String getMessage() {
		return message;
	}

}
