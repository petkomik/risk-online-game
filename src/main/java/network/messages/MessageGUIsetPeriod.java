package network.messages;

import java.time.Period;

import gameState.GameState;

public class MessageGUIsetPeriod extends Message {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  GameState gameState;
	private Period period;
	public GameState getGameState() {
		return gameState;
	}
	public Period getPeriod() {
		return period;
	}
	public MessageGUIsetPeriod(GameState gameState, Period period) {
		super(MessageType.MessageGUIsetPeriod);
		this.gameState = gameState;
		this.period = period;
		
	}

}
