package network.messages;

public class MessagePlayerTurn extends Message {
	
	int playerID;

	private static final long serialVersionUID = 1L;

	public MessagePlayerTurn(MessageType type, int playerID) {
		super(type);
		this.playerID = playerID;
	}
	
	public int getPlayerID() {
		return this.playerID;
	}

}
