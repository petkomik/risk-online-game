package game.models;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Player class to model the player entity.
 *
 * @author jorohr
 */

public abstract class Player implements Serializable {

  private static final long serialVersionUID = 1L;

  private String username;
  private String color;
  private String avatar;
  protected int id;
  protected boolean isAi;
  private int rank;
  private LocalDateTime gameEndTime;

  /**
   * Constructor. Sets the name and the id of a player.
   */
  public Player(String name, int id) {
    this.username = name;
    this.id = id;
  }

  /**
   * Constructor. Sets the name, id and the imgae Path of a player.
   */
  public Player(String name, int id, String imagePath) {
    this.username = name;
    this.id = id;
    this.avatar = imagePath;
  }

  /** constructor for defensive copying. */
  public Player(Player player) {
    this.username = new String(player.getName());
    this.color = player.getColor();
    this.id = player.getId();
    this.rank = player.getRank();
    this.gameEndTime = player.getGameEndTime();
    this.avatar = player.avatar;
  }

  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.username;
  }

  public LocalDateTime getGameEndTime() {
    return gameEndTime;

  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public String getAvatar() {
    return this.avatar;
  }

  public void setAvatar(String imagePath) {
    this.avatar = imagePath;
  }

  public boolean isAi() {
    // TODO Auto-generated method stub
    return this.isAi;
  }

}
