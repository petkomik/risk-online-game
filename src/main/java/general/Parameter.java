package general;

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
	public static final String dbName = "risk-database.db";

}

