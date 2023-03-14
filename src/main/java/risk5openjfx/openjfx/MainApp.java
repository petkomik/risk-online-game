package risk5openjfx.openjfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application{
	
	private AnchorPane anchorPane;
    private Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
    	// Getting the anchor pane from the file main.fxml
    	anchorPane = (AnchorPane) loadFXML("main");
    	// Setting the anchor pane as a root into the scene
        scene = new Scene(anchorPane);
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
