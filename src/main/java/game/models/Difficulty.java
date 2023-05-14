package game.models;

/**
 * This enum is used for the AI Difficulty.
 *
 * @author pmikov
 */

public enum Difficulty {
  EASY {
    @Override
    public String toString() {
      return "easy";
    }
  },
  CASUAL {
    @Override
    public String toString() {
      return "casual";
    }
  },
  HARD {
    @Override
    public String toString() {
      return "hard";
    }
  }
}
