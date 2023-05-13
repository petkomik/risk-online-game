package database;

import java.time.LocalDateTime;

/**
 * Dataset for game statistics to get/set information via database.
 *
 * @author jorohr
 */
public class GameStatistic {

  private int gameId;
  private int length;
  private LocalDateTime startTime;
  private int idOfWinner;
  private int numberOfPlayers;

  /**
   * GameStatistic constructor with start time and number of players.
   * 
   * @param startTime the start time of the game
   * @param numberOfPlayers the number of players in the game
   */
  public GameStatistic(LocalDateTime startTime, int numberOfPlayers) {
    this.gameId = (int) Math.round(Math.random() * 10000);
    this.setGameId(gameId);
    this.setStartTime(LocalDateTime.now());
    this.setNumberOfPlayers(numberOfPlayers);
  }

  /**
   * GameStatistic constructor with game ID, length, start time, ID of winner, and number of
   * players.
   * 
   * @param gameID the ID of the game
   * @param length the length of the game
   * @param startTime the start time of the game
   * @param idOfWinner the ID of the winner of the game
   * @param numberOfPlayers the number of players in the game
   */
  public GameStatistic(int gameId, int length, LocalDateTime startTime, int idOfWinner,
      int numberOfPlayers) {
    this.setGameId(gameId);
    this.setLength(length);
    this.setStartTime(startTime);
    this.setIdOfWinner(idOfWinner);
    this.setNumberOfPlayers(numberOfPlayers);
  }

  public int getGameId() {
    return gameId;
  }

  public void setGameId(int gameId) {
    this.gameId = gameId;
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
    return "Game with GameID: " + this.gameId + " and " + this.numberOfPlayers
        + " Players. It started " + this.startTime + " and durated " + this.length;
  }
}
