package game.gui;

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
	private double w = MainApp.screenWidth;
	private double h = MainApp.screenHeight;
	
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
		double btnW = w * 0.163;
		double btnH = h * 0.058;
		double btnX = w * 0.419;
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
		
		double fontSize = 0.078 * Math.sqrt(Math.pow(btnW, 2.0)+Math.pow(btnH, 2.0));
		playTutorialButton.setStyle("-fx-font-size: "+fontSize+"px;");
		profileSettingsButton.setStyle("-fx-font-size: "+fontSize+"px;");
		singleplayerButton.setStyle("-fx-font-size: "+fontSize+"px;");
		multiplayerButton.setStyle("-fx-font-size: "+fontSize+"px;");
	}
	
	/**
	 * The method handles the event, when the player clicks on the button 'Multiplayer'
	 * @param e
	 * @throws IOException
	 */
	public void showMultiplayerScene(ActionEvent e) throws IOException {
		
		(new GameSound()).buttonClickForwardSound();
		
		Node node = (Node)e.getSource();
		// Getting the Stage where the event is happened
		stage = (Stage)node.getScene().getWindow();
		// changing the AnchorPane from the main file
		anchorPane = (AnchorPane) loadFXML("MultiplayerHostJoinFrame");
		// Setting the size of the anchorPane
		anchorPane.setPrefSize(w, h);
		// Setting the AnchorPane as a root of the main scene
		stage.getScene().setRoot(anchorPane);
		// Showing the Stage
		stage.show();
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