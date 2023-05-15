package game.state;

/**
 * This enum stores the different hints shown to players.
 *
 * @author pmikov
 */

public enum Hint {
  INTRODUCTION {
    @Override
    public String toString() {
      return "Welcome to the tutorial of the Game Risk. "
          + "Risk is traditionally a board game that has been loved by many "
          + "for long time. The goal is to conquer all of the territories on the map. "
          + "You conquer new territories by attacking other players and destroying their "
          + "armies in battle. Whoever is the last one standing wins, alliances can help "
          + "you reach this goal";
    }
  },
  GUIELEMENTS {
    @Override
    public String toString() {
      return "The GUI shows all the information about the current state of the game. "
          + "In the center is the game map. It is the same as the world map but is separated in "
          + "geographical region which we call territories. "
          + "On the right you can see the avatars and colors of all the players. "
          + "To indicate the current turn a banner is shown next to the players avatar. "
          + "A leave game button is on the top left for when you decide forfeit.";
    }
  },
  DICETHROW {
    @Override
    public String toString() {
      return "The game starts with a dice throw period. Here each player throws a dice. "
          + "Whoever throws the highest one gets to go first. To throw click on the "
          + "\"Throw Dice\" button. "
          + "When you are ready click on the next button - the one with the check mark - ✓. ";
    }
  },
  COUNTRYPOSESSION {
    @Override
    public String toString() {
      return "The next period is Coutry Possession. Here players do the initial "
          + "claiming of territories."
          + "Try to choose which one to conquer next strategically. If you manage to "
          + "get all of the territories "
          + "in a continent you get bonus troops. Think about how vunerable your "
          + "territories are to attacks " + "from enemies";
    }
  },
  INITIALDEPLOY {
    @Override
    public String toString() {
      return "Now that all territories have been claimed you can reinforce your "
          + "territories with the left over troops. "
          + "Place your troops smartly. You don't need any troops in territories "
          + "that dont have neighboring territories, "
          + "controller by enemy players. That is not the case for territories "
          + "that are on the outside of the area you controll."
          + "Send reinforcements where you expect to attack from or to get attacked.";
    }
  },
  PHASES {
    @Override
    public String toString() {
      return "In the Reinforce you get a additional troops based on the territories you "
          + "own divided by 3 (minimum 3), "
          + "if you own all the territories of a continents (number of additional armies "
          + "depends on the continents held) or traded cards. "
          + "You can only trade cards during the reinforce phase by clicking on the card "
          + "button bottom left. "
          + "During the Attack Phase you use your armies to conquer new territories. You "
          + "can attack any enemy territory as long as you "
          + "have at least 2 armies in a neighbouring territory. Click on the attacking "
          + "territory then on the target territory. "
          + "Fortify gives you the ability to move armies from any connecting territory "
          + "to another one once. "
          + "Click on the territory with the armies to move then on the destination territory. "
          + "After you will be asked how many troops you wish to move. After that end your turn.";
    }
  },
  BATTLE {
    @Override
    public String toString() {
      return "Battles are decided via dice rolls. Highest dice wins. In case of a tie, "
          + "the defender wins. "
          + "The battle continues until one of the player loses all of his troops. "
          + "The number of dice used is also the minimum number of armies that need to be "
          + "transferred to a newly conquered territory. ";
    }
  },
  OVER {
    @Override
    public String toString() {
      return "Now you are ready to play a full game. Hop over to singleplayer or go into a "
          + "online game with some friends.";
    }
  },
}
