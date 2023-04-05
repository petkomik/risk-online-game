
package game.models;

import network.ClientHandler;

/**
 * 
 * @author srogalsk
 *
 */

enum Difficulty {
	EASY,
	CASUAL,
	HARD
}

public class PlayerAI extends Player {
	Difficulty level;
	
	public PlayerAI(Player player, Difficulty level) {
		super(player.getName(), player.getID());
		this.level = level;
	}
	
	public PlayerAI(String name, int id, Difficulty setLevel) {
		super(name, id);
		this.level = setLevel;
	}
	
	public int getRank() {
		switch(this.level) {
		case CASUAL:
			return 30;
		case HARD: 
			return 70;
		default:
			return 10;
		}
	}

}
