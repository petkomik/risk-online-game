package gameState;

/**
 * Enums do the game periods.
 *
 * @author pmikov
 */

public enum Period {
  DICETHROW {
    @Override
    public String toString() {
      return "Dice Throw";
    }
  },
  COUNTRYPOSESSION {
    @Override
    public String toString() {
      return "Claim";
    }
  },
  INITIALDEPLOY {
    @Override
    public String toString() {
      return "Deploy";
    }
  },
  MAINPERIOD {
    @Override
    public String toString() {
      return "Main Period";
    }
  }
}
