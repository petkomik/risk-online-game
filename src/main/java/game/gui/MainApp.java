package game.gui;

import java.io.File;
import java.io.IOException;

import game.GameController;
import general.Parameter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * 
 * @author malaji
 * This class shows the very first page
 */
public class MainApp extends Application{
	
	public static double screenWidth;
	public static double screenHeight;
	private AnchorPane anchorPane;
    private Scene scene;
    private static GameController gameController = new GameController();
    public static GameSound sound;
    /**
     * This method returns the GameController instance
     * @return gameController 
     */
    public static GameController getGameController() {
    	return MainApp.gameController;
    }
    
    /**
     * This method initializes the required elements
     */
    @Override
    public void start(Stage stage) throws IOException {
    	screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    	screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
    	// Getting the anchor pane from the file main.fxml
    	anchorPane = (AnchorPane) loadFXML("main");
    	// Setting the size of the pane
        anchorPane.setPrefSize(MainApp.screenWidth, MainApp.screenHeight);
    	// Setting the anchor pane as a root into the scene
        scene = new Scene(anchorPane);
        // Getting the pic transparent-risk.png
        Image image = new Image(Parameter.logoImage);
        // Setting the pic transparent-risk.png as logo of the stage
        stage.getIcons().add(image);
        // Setting the scene into the stage
        stage.setScene(scene);
        // Setting the stage on the maximized mode
        stage.setMaximized(true);
        // Setting the resizability of the stage on false
        stage.setResizable(false);
        // Showing the stage
        stage.show();
        
        /**************** Sound *************************//**
        File file = new File(Parameter.themeSong);
		Media media = new Media(file.toURI().toString());
		System.out.println(file.toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();*/
        
        sound = new GameSound();
        sound.startThemeSong(); 
		
    }
    /**
     * 
     * @param fxml, file name without the ending .fxml
     * @return Parent object, to be set as a root in a Secene object
     * @throws IOException
     * 
     * This method is responsible for loading a fxml file
     */
    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    /**
     * this class method enables the launching of the App outside the class
     * @param args
     */
	public static void runMainApp(String[] args) {
		launch(args);
	}

}
