package network.messages;

import game.models.Player;

public class MessagePlayerTurn extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Player player;
	public MessagePlayerTurn(Player player) {
		super(MessageType.MessagePlayerTurn);
		this.player = player;
	}

}
