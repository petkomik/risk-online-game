package general;

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
	public static final String avatarsdir = resourcesdir + "avatars" + sep;
	public static final String dicedir = resourcesdir + "dice" + sep;
	public static final String territoryPNGdir = resourcesdir + "territories" + sep;
	public static final String logoImage = imagesdir + "transparent-risk.png";
	public static final String errorIcon = imagesdir + "error_icon.png";
	public static final String coord = resourcesdir + sep + "game" + sep + "gui" + sep + "coord.txt";
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
	public static final Color greenColor = Color.web("#59f23a");
	public static final Color yellowColor = Color.web("#d9e021");
	public static final Color orangeColor = Color.web("#f28a3a");
	public static final Color purpleColor = Color.web("#c73af2");
	public static final Color white = Color.web("#ffffff");




}

