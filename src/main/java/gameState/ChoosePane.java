package gameState;

/**
 * @author pmikov
 *
 */

public enum ChoosePane {
  REINFORCE {
    @Override
    public String toString() {
      return "Reinforce";
    }
  },
  ATTACK_ATTACK {
    @Override
    public String toString() {
      return "Attack";
    }
  },
  ATTACK_COLONISE {
    @Override
    public String toString() {
      return "Move Troops";
    }
  },
  FORTIFY {
    @Override
    public String toString() {
      return "Fortify";
    }
  }
}
