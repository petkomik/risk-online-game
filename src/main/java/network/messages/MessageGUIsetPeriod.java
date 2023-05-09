package network.messages;



import game.Lobby;
import gameState.GameState;
import gameState.Period;

public class MessageGUIsetPeriod extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  GameState gameState;
	private Period period;
	private Lobby lobby;
	public GameState getGameState() {
		return gameState;
	}
	public Period getPeriod() {
		return period;
	}
	public MessageGUIsetPeriod(GameState gameState, Period period, Lobby lobby) {
		super(MessageType.MessageGUIsetPeriod);
		this.gameState = gameState;
		this.period = period;
		this.lobby = lobby;
		
	}
	public Lobby getLobby() {
		return lobby;
	}

}
