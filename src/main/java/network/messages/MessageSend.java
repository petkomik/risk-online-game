package network.messages;

import game.models.Player;
/**
 * 
 * @author srogalsk
 *
 */

public class MessageSend extends Message {
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	private Player playerFrom;
	private Player playerTo;

	public MessageSend(String message, Player playerFrom, Player playerTo) {
		super(MessageType.MessageSend);
		this.message = message;
		this.playerFrom = playerFrom;
		this.playerTo = playerTo;
	}

}
