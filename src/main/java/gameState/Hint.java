package gameState;

public enum Hint {
	TIMER  {
        @Override
        public String toString() {
            return "- Risk uses 3 different phases of play during a turn. A turn has an amount of time you can spend in any phase.\r\n"
            		+ "- Each phase is finished and it moves to the next one or to the next player\r\n"
            		+ "- During the attack phase, every attack made by you increases the timer by 7 seconds.\r\n";
        }
    }, 
	INITIALCLAIMING  {
        @Override
        public String toString() {
            return "- You select one territory each time it comes to your turn.\r\n"
            		+ "- Once all the territories have been selected, each one of the players can start adding armies to a territory. All the territories must be claimed before a player can place a second army on a territory.\r\n";
        }
    },
	PHASES  {
        @Override
        public String toString() {
            return "- Reinforce allows you to place a number of troops based upon the territories you own divided by 3 (minimum 3), any completed continents (number of additional armies depends on the continents held) or traded cards. You can only trade cards during the reinforce phase.\r\n"
            		+ "- Attack let’s you use your armies to conquer new territories. You can attack any enemy territory as long as you have at least 2 armies in a neighbouring territory. Click on the attacking territory them on the target territory.\r\n"
            		+ "- Fortify gives you the possibility to move armies from any connecting territory to another one once. Click on the territory with the armies to move them on the destination territory.\r\n";
        }
    },
	GAMEELEMENTS  {
        @Override
        public String toString() {
            return "1.Timer\r\n"
            		+ "2.Players Order / Players’ avatars\r\n"
            		+ "3.Player’s phase\r\n"
            		+ "4.Players’ cards\r\n";
        }
    },
	BATTLE  {
        @Override
        public String toString() {
            return "- Battles are decided via dice rolls. Highest dice wins. In case of a tie, the defender wins. Select number of dice and hit the check mark.\r\n"
            		+ "- The attacker decides how many dice he wants to roll (to a maximum of 3). The number of dice used is also the minimum number of armies that need to be transferred to a newly conquered territory\r\n";
        }
    }
}
