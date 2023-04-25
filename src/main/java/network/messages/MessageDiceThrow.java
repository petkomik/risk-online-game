package network.messages;

public class MessageDiceThrow extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int diceNumber;

	public MessageDiceThrow(int diceNumber) {
		super(MessageType.MessageDiceThrow);
		this.diceNumber = diceNumber;
	}

	public MessageDiceThrow() {
		super(MessageType.MessageDiceThrow);
	}

	public int getDiceNumber() {
		return diceNumber;
	}

}
