package risk5openjfx.openjfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import general.Parameter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 
 * @author majda
 * This class handles the events on the main
 */
public class MainMenuPaneController implements Initializable{
	private Stage stage;
	private AnchorPane anchorPane;
	private double w = Parameter.screenWidth;
	private double h = Parameter.screenHeight;
	
	@FXML
	private Button playTutorialButton;
	@FXML
	private Button profileSettingsButton;
	@FXML
	private Button singleplayerButton;
	@FXML
	private Button multiplayerButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		double btnW = w * 0.143;
		double btnH = h * 0.058;
		double btnX = w * 0.428;
		playTutorialButton.setPrefSize(btnW, btnH);
		profileSettingsButton.setPrefSize(btnW, btnH);
		singleplayerButton.setPrefSize(btnW, btnH);
		multiplayerButton.setPrefSize(btnW, btnH);
		
		playTutorialButton.setLayoutX(btnX);
		profileSettingsButton.setLayoutX(btnX);
		singleplayerButton.setLayoutX(btnX);
		multiplayerButton.setLayoutX(btnX);

		playTutorialButton.setLayoutY(h*0.301);
		profileSettingsButton.setLayoutY(h*0.382);
		singleplayerButton.setLayoutY(h*0.463);
		multiplayerButton.setLayoutY(h*0.544);
	}
	
//	public void clickDisplayStatistics(ActionEvent e) throws IOException {
//		Node node = (Node)e.getSource();
//		// Getting the Stage where the event is happened
//		stage = (Stage)node.getScene().getWindow();
//		// changing the AnchorPane from the main file
//		anchorPane = (AnchorPane) loadFXML("displayStatistics");
//		// Setting the AnchorPane as a root of the main scene
//		stage.getScene().setRoot(anchorPane);
//		// Showing the Stage
//		stage.show();
//	}
	
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
