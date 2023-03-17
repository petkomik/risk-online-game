
package game.models;

import java.time.LocalTime;

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
	
	public PlayerAI(Difficulty setLevel) {
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
