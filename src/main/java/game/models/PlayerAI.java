
package game.models;

import java.time.LocalDateTime;

import network.ClientHandler;

/**
 * 
 * @author srogalsk
 *
 */

public class PlayerAI extends Player {
	Difficulty level;
	
	public PlayerAI(Player player, Difficulty level) {
		super(player.getName(), player.getID());
		this.level = level;
		this.isAI = true;
		this.id = (int)Math.round((int)Math.random()*10000);
	}
	
	public PlayerAI(String name, int id, Difficulty setLevel) {
		super(name, id);
		this.level = setLevel;
		this.isAI = true;
		this.id = (int)Math.round((int)Math.random()*10000);

	}
	
	public PlayerAI(String name, int id, int setLevel) {
		super(name, id);
		this.level = Difficulty.values()[setLevel];
		this.isAI = true;
		this.id = (int)Math.round((int)Math.random()*10000);

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
	
	public Difficulty getLevel() {
		return this.level;
	}

}
