package network.messages;

public class MessageDiceThrow extends Message {

	private static final long serialVersionUID = 1L;
	private int diceThrow;
	public MessageDiceThrow(MessageType type, int diceThrow) {
		super(type);
		this.diceThrow = diceThrow;
	}
	
	public int getDiceThrow() {
		return this.diceThrow;
	}

}
