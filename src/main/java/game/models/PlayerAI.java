
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
		this.id = Integer.parseInt(LocalDateTime.now().toString().replace("-", "").replace(":", "").replace("'", "").replace("T", "").substring(15,24));
		this.isAI = true;
	}
	
	public PlayerAI(String name, int id, Difficulty setLevel) {
		super(name, id);
		this.level = setLevel;
		this.id = Integer.parseInt(LocalDateTime.now().toString().replace("-", "").replace(":", "").replace("'", "").replace("T", "").substring(15,24));
		this.isAI = true;
	}
	
	public PlayerAI(String name, int id, int setLevel) {
		super(name, id);
		this.level = Difficulty.values()[setLevel];
		this.id = Integer.parseInt(LocalDateTime.now().toString().replace("-", "").replace(":", "").replace("'", "").replace("T", "").substring(15,24));
		this.isAI = true;
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
