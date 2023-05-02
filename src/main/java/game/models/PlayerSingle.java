package game.models;

import database.Profile;
import general.Parameter;
import javafx.scene.paint.Color;

public class PlayerSingle extends Player {
	private Profile profile;
	private static final long serialVersionUID = 1L;

	public PlayerSingle(String name, int id) {
		super(name, id);
		this.isAI = false;
	}
	
	public PlayerSingle(Player player) {
		super(player);
		this.isAI = false;
	}
	
	public PlayerSingle(Profile profile) {
		super(profile.getUserName(), profile.getId());
		this.profile = profile;
		this.setColor(profile.getColor());
		this.setAvatar(Parameter.avatarsdir + profile.getPhoto());
	}

}
