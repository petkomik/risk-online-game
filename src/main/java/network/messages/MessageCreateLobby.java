package network.messages;

public class MessageCreateLobby extends Message {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int playerID;
	private String lobbyname;
	public MessageCreateLobby(int playerID, String lobbyName) {
		super(MessageType.MessageCreateLobby);
		this.playerID = playerID;
		this.lobbyname = lobbyName;
	}
	public int getPlayerID() {
		return playerID;
	}
	public String getLobbyname() {
		return lobbyname;
	}

}
