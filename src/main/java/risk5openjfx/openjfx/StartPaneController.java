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
 * this class handles the events on the very first frame
 */
public class StartPaneController {
	private Stage stage;
	private AnchorPane anchorPane;
	/**
	 * this method shows the create profile frame after the player has clicked the button 'play'
	 * @param e
	 * @throws IOException
	 */
	public void clickPlay(ActionEvent e) throws IOException {
		
		(new GameSound()).buttonClickForwardSound();
		
		Node node = (Node)e.getSource();
		stage = (Stage)node.getScene().getWindow();
		anchorPane = (AnchorPane) loadFXML("createProfile");
		stage.getScene().setRoot(anchorPane);
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
        FXMLLoader fxmlLoader = new FXMLLoader(StartPaneController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
	}
    
}
