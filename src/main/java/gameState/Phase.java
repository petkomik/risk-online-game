package gameState;

/**
 * Enums for the phases during a player turn in main period.
 *
 * @author pmikov
 *
 */

public enum Phase {
  REINFORCE {
    @Override
    public String toString() {
      return "Reinforce";
    }
  },
  ATTACK {
    @Override
    public String toString() {
      return "Attack";
    }
  },
  FORTIFY {
    @Override
    public String toString() {
      return "Fortify";
    }
  }
}
