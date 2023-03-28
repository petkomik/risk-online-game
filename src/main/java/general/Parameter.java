package general;

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
	public static final String logoImage = imagesdir + "transparent-risk.png";
	public static final String errorIcon = imagesdir + "error_icon.png";
	public static final String dbName = "risk-database.db";
	public static final String soundsdir = resourcesdir + "sounds" + sep;
	public static final String themeSong = soundsdir + "Now-We-Ride.mp3";
	
	public static final String buttonClick03Sound = soundsdir + "zapsplat_multimedia_button_click_fast_short_003.mp3";
	public static final String buttonClick04Sound = soundsdir + "zapsplat_multimedia_button_click_fast_short_004.mp3";
	
	
}

