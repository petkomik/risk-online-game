package general;

import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.stage.Screen;

/**
 * Parameter class to get to directories, files, the database, etc.
 *
 * @author jorohr
 */

public class Parameter {

	public static final String sep = System.getProperty("file.separator");
	public static final String userdir = System.getProperty("user.dir");
	public static final String resourcesdir = userdir + sep + "src" + sep + "main" + sep + "resources" + sep;
	public static final String imagesdir = resourcesdir + "images" + sep;
	public static final String podiumdir = resourcesdir + "podium" +sep;
	public static final String avatarsdir = resourcesdir + "avatars" + sep;
	public static final String dicedir = resourcesdir + "dice" + sep;
	public static final String territoryPNGdir = resourcesdir + "territories" + sep;
	public static final String phaseLogosdir = resourcesdir + "phaseLogos" + sep;
	public static final String logoImage = imagesdir + "transparent-risk.png";
	public static final String dicesIcon = imagesdir + "dices-icon.png";
	public static final String errorIcon = imagesdir + "error_icon.png";
	public static final String chatIcon = imagesdir + "chat-img.png";
	public static final String refreshIcon = imagesdir + "refresh.png";
	public static final String coord = resourcesdir + sep + "game" + sep + "gui" + sep + "coord.txt";
	public static final String css = resourcesdir + "game" + sep + "gui" + sep + "application.css";
	public static final String dbName = "risk-database.db";
	public static final String soundsdir = resourcesdir + "sounds" + sep;
	public static final String themeSong = soundsdir + "Now-We-Ride.mp3";
	
	public static final String buttonClick03Sound = soundsdir + "zapsplat_multimedia_button_click_fast_short_003.mp3";
	public static final String buttonClick04Sound = soundsdir + "zapsplat_multimedia_button_click_fast_short_004.mp3";
	
	public static final String hostDefault = "localhost";
	public static final int portDefault = 1234;
	
	//player color codes
	public static final Color redColor = Color.web("#c1272d");
	public static final Color blueColor = Color.web("#0071bc");
	public static final Color greenColor = Color.web("#26d124");
	public static final Color yellowColor = Color.web("#d9e021");
	public static final Color orangeColor = Color.web("#f28a3a");
	public static final Color purpleColor = Color.web("#c73af2");
	public static final String[] allColors = new String[] {"#c1272d", "#0071bc",
			"#26d124", "#d9e021", "#f28a3a", "#c73af2"};
	
	public static final Color darkGrey = Color.web("#303030");
	
	//player avatars dirs
	public static final String blondBoy = avatarsdir + "blonde-boy.png";
	public static final String bruntetteBoy = avatarsdir + "bruntette-boy.png";
	public static final String earringsGirl = avatarsdir + "earrings-girl.png";
	public static final String gingerGirl = avatarsdir + "ginger-girl.png";
	public static final String hatBoy = avatarsdir + "hat-boy.png";
	public static final String mustacheMan = avatarsdir + "mustache-man.png";
	public static final String[] allAvatars = new String[] {"earrings-girl.png", 
			"bruntette-boy.png", "blonde-boy.png", "ginger-girl.png", 
			"hat-boy.png", "mustache-man.png"};

	//army pngs 
	public static final String armiesDir = resourcesdir + "armies-icons" + sep;
	public static final String artillery = armiesDir + "artillery.png";
	public static final String cavalry = armiesDir + "cavalry.png";
	public static final String infantry = armiesDir + "infantry.png";


	

	





}

