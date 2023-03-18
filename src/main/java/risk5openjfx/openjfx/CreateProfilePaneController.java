package risk5openjfx.openjfx;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 
 * @author majda
 * The class handles the event on the create profile frame
 */
public class CreateProfilePaneController {
	private Stage stage;
	private AnchorPane anchorPane;
	/**
	 * The method handles the event, when the player clicks on the button 'create'
	 * @param e
	 * @throws IOException
	 */
    public void clickCreate(ActionEvent e) throws IOException {
    	
    	(new GameSound()).buttonClickForwardSound();
    	
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
    /**
	 * The method handles the event, when the player clicks on the button 'back'
	 * @param e
	 * @throws IOException
	 */
	public void clickBack(ActionEvent e) throws IOException {
		
		(new GameSound()).buttonClickBackwardSound();
		
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
	/**
     * 
     * @param fxml, file name without the ending .fxml
     * @return Parent object, to be set as a root in a Secene object
     * @throws IOException
     * 
     * This method is responsible for loading a fxml file
     */
	private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CreateProfilePaneController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
