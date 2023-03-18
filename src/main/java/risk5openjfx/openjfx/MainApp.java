package risk5openjfx.openjfx;

import java.io.File;
import java.io.IOException;

import general.Parameter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * 
 * @author malaji
 * This class shows the very first page
 */
public class MainApp extends Application{
	
	private AnchorPane anchorPane;
    private Scene scene;
    /**
     * This method initializes the required elements
     */
    @Override
    public void start(Stage stage) throws IOException {
    	// Getting the anchor pane from the file main.fxml
    	anchorPane = (AnchorPane) loadFXML("main");
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
        
        (new GameSound()).startThemeSong();
		
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
