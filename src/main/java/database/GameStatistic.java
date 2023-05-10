package database;

import java.time.LocalDateTime;

/**
 * Dataset for game statistics to get/set information via database
 *
 * @author jorohr
 */
public class GameStatistic {
	
	private int gameID;
	private int length;
	private LocalDateTime startTime;
	private int idOfWinner;
	private int numberOfPlayers;
	
	
	public GameStatistic(LocalDateTime startTime, int numberOfPlayers) {
		this.gameID = (int)Math.round(Math.random()*10000);
		this.setGameID(gameID);
		this.setStartTime(LocalDateTime.now());
		this.setNumberOfPlayers(numberOfPlayers);	
	}
	
	public GameStatistic(int gameID, int length, LocalDateTime startTime, int idOfWinner, int numberOfPlayers) {
		this.setGameID(gameID);
		this.setLength(length);
		this.setStartTime(startTime);
		this.setIdOfWinner(idOfWinner);
		this.setNumberOfPlayers(numberOfPlayers);
	}

	public int getGameID() {
		return gameID;
	}

	public void setGameID(int gameID) {
		this.gameID = gameID;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public int getIdOfWinner() {
		return idOfWinner;
	}

	public void setIdOfWinner(int idOfWinner) {
		this.idOfWinner = idOfWinner;
	}

	public int getNumberOfPlayers() {
		return numberOfPlayers;
	}

	public void setNumberOfPlayers(int numberOfPlayers) {
		this.numberOfPlayers = numberOfPlayers;
	}
	
	public String toString() {
		return "Game with GameID: " + this.gameID + " and " + this.numberOfPlayers + " Players. It started " + this.startTime + " and durated " + this.length;
	}
}