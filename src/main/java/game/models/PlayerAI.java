
package game.models;

/**
 * 
 * @author srogalsk
 *
 */

public class PlayerAI extends Player {
  Difficulty level;

  private static final long serialVersionUID = 123456789L;

  public PlayerAI(Player player, Difficulty level) {
    super(player.getName(), player.getId());
    this.level = level;
    this.isAi = true;
    this.id = (int) Math.round(Math.random() * 1000000);
  }

  public PlayerAI(String name, int id, Difficulty setLevel) {
    super(name, id);
    this.level = setLevel;
    this.isAi = true;
    this.id = (int) Math.round(Math.random() * 1000000);

  }

  public PlayerAI(String name, int id, int setLevel) {
    super(name, id);
    this.level = Difficulty.values()[setLevel];
    this.isAi = true;
    this.id = (int) Math.round(Math.random() * 10000000);

  }

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
