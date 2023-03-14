package network.messages;

import game.models.Player;

public class MessageSend extends Message {
	
	private static final long serialVersionUID = 1L;
	
	private String message;
	private Player player;

	public MessageSend(String message, Player player) {
		super(MessageType.MessageSend);
		this.message = message;
		this.player = player;
	}

}
