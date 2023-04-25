package game.models;

import database.Profile;
import general.Parameter;
import javafx.scene.paint.Color;

public class PlayerSingle extends Player {
	private Profile profile;
	private static final long serialVersionUID = 1L;

	public PlayerSingle(String name, int id) {
		super(name, id);
	}
	
	public PlayerSingle(Player player) {
		super(player);
	}
	
	public PlayerSingle(Profile profile) {
		super(profile.getUserName(), profile.getId());
		this.profile = profile;
	}
	
	public String getPrefColor() {
		return this.profile.getColor();
	}
	
	public String getPrefAvatar() {
		return Parameter.avatarsdir + this.profile.getPhoto();
	}

}
