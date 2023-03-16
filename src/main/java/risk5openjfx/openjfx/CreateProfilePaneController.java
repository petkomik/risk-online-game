package risk5openjfx.openjfx;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class CreateProfilePaneController {
	private Stage stage;
	private AnchorPane anchorPane;
	
    public void clickCreate(ActionEvent e) throws IOException {
    	Node node = (Node)e.getSource();
		// Getting the Stage where the event is happened
		stage = (Stage)node.getScene().getWindow();
		// changing the AnchorPane from the main file
		anchorPane = (AnchorPane) loadFXML("mainMenu");
		// Setting the AnchorPane as a root of the main scene
		stage.getScene().setRoot(anchorPane);
		// Showing the Stage
		stage.show();
	}
	
	public void clickBack(ActionEvent e) throws IOException {
		Node node = (Node)e.getSource();
		// Getting the Stage where the event is happened
		stage = (Stage)node.getScene().getWindow();
		// changing the AnchorPane from the main file
		anchorPane = (AnchorPane) loadFXML("main");
		// Setting the AnchorPane as a root of the main scene
		stage.getScene().setRoot(anchorPane);
		// Showing the Stage
		stage.show();
	}
	
	private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CreateProfilePaneController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
