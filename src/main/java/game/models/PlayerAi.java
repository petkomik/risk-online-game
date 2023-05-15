
package game.models;

/**
 * class models an AI player.
 *
 * @author srogalsk
 *
 */

public class PlayerAi extends Player {
  Difficulty level;

  private static final long serialVersionUID = 123456789L;

  /** Constructor using player instance. */

  public PlayerAi(Player player, Difficulty level) {
    super(player.getName(), player.getId());
    this.level = level;
    this.isAi = true;
    this.id = (int) Math.round(Math.random() * 1000000);
  }

  /** Constructor using name and id. */

  public PlayerAi(String name, int id, Difficulty setLevel) {
    super(name, id);
    this.level = setLevel;
    this.isAi = true;
    this.id = (int) Math.round(Math.random() * 1000000);

  }

  /** Constructor with name and id, but difficulty as string. */

  public PlayerAi(String name, int id, int setLevel) {
    super(name, id);
    this.level = Difficulty.values()[setLevel];
    this.isAi = true;
    this.id = (int) Math.round(Math.random() * 10000000);

  }

  /** Returns the rank of the AI. */

  public int getRank() {
    switch (this.level) {
      case CASUAL:
        return 30;
      case HARD:
        return 70;
      default:
        return 10;
    }
  }

  public Difficulty getLevel() {
    return this.level;
  }

  public void setLevel(Difficulty level) {
    this.level = level;
  }
}
