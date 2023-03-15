package risk5openjfx.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

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
        Image image = new Image("/risk5/src/main/resources/risk5openjfx/openjfx/transparent-risk.png");
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
    }

    private Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
    
	public static void runMainApp() {
		launch();

	}

}
