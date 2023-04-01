package game.models;

import java.time.LocalTime;

import game.GameState;
import game.Profile;

/**
 * Player class to model the player entity
 *
 * @author jorohr
 */

public class Player {
	
	private String name;
	private String color;
	private int id;
	private int rank;
	
	public Player(String name, int id) {
		this.name = name;
		this.id = id;
	}
	
	public void incrementRank() {
		rank+=1;
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getID() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	
	

	
}
