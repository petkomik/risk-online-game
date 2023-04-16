package game.gui;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import general.AppController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 
 * @author jorohr
 * This class handles the profile updating
 */
public class UpdateSettingsController implements Initializable{
	private Stage stage;
	private AnchorPane anchorPane;
	private double w = MainApp.screenWidth;
	private double h = MainApp.screenHeight;
	
	@FXML
	private Button uploadPhotoButton;
	@FXML
	private Button updateFirstNameButton;
	@FXML
	private Button updateLastNameButton;
	@FXML
	private Button updatePasswordButton;
	@FXML
	private Button backButton;
	@FXML
	private Button deleteProfileButton;
	
	GameSound gameSoundButton = new GameSound();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		double btnW = w * 0.163;
		double btnH = h * 0.058;
		double btnX = w * 0.419;
		uploadPhotoButton.setPrefSize(btnW, btnH);
		updateFirstNameButton.setPrefSize(btnW, btnH);
		updateLastNameButton.setPrefSize(btnW, btnH);
		updatePasswordButton.setPrefSize(btnW, btnH);
		
		uploadPhotoButton.setLayoutX(btnX);
		updateFirstNameButton.setLayoutX(btnX);
		updateLastNameButton.setLayoutX(btnX);
		updatePasswordButton.setLayoutX(btnX);

		uploadPhotoButton.setLayoutY(h*0.301);
		updateFirstNameButton.setLayoutY(h*0.382);
		updateLastNameButton.setLayoutY(h*0.463);
		updatePasswordButton.setLayoutY(h*0.544);
		
		double fontSize = 0.078 * Math.sqrt(Math.pow(btnW, 2.0)+Math.pow(btnH, 2.0));
		uploadPhotoButton.setStyle("-fx-font-size: "+fontSize+"px;");
		updateFirstNameButton.setStyle("-fx-font-size: "+fontSize+"px;");
		updateLastNameButton.setStyle("-fx-font-size: "+fontSize+"px;");
		updatePasswordButton.setStyle("-fx-font-size: "+fontSize+"px;");
		
		backButton.setPrefSize(w * 0.091, h * 0.058);
		backButton.setLayoutX(w * 0.026);
		backButton.setLayoutY(h * 0.046);
		backButton.setStyle("-fx-font-size: " + fontSize + "px;");
		
		deleteProfileButton.setPrefSize(btnW, btnH);
		deleteProfileButton.setLayoutX(w * 0.7);
		deleteProfileButton.setLayoutY(h * 0.7);
		deleteProfileButton.setStyle("-fx-font-size: " + fontSize + "px;");


	}
	
	/**
	 * The method handles the event, when the player clicks on the button 'UploadPhotoButton'
	 * @param e
	 * @throws IOException
	 */
	public void showUploadPhotoFrame(ActionEvent e) throws IOException {
		
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
	
	public void showUpdateFirstNameFrame(ActionEvent e) throws IOException {
		(new GameSound()).buttonClickForwardSound();
		Node node = (Node)e.getSource();
		// Getting the Stage where the event is happened
		stage = (Stage)node.getScene().getWindow();
		// changing the AnchorPane from the main file
		anchorPane = (AnchorPane) loadFXML("battleWindowFrame");
		// Setting the size of the anchorPane
		anchorPane.setPrefSize(w, h);
		// Setting the AnchorPane as a root of the main scene
		stage.getScene().setRoot(anchorPane);
		// Showing the Stage
		stage.show();
		
	}
	
	public void showUpdateLastNameFrame(ActionEvent e) throws IOException {
		(new GameSound()).buttonClickForwardSound();
		Node node = (Node)e.getSource();
		// Getting the Stage where the event is happened
		stage = (Stage)node.getScene().getWindow();
		// changing the AnchorPane from the main file
		anchorPane = (AnchorPane) loadFXML("gameFrame");
		// Setting the size of the anchorPane
		anchorPane.setPrefSize(w, h);
		// Setting the AnchorPane as a root of the main scene
		stage.getScene().setRoot(anchorPane);
		// Showing the Stage
		stage.show();
		
	}
	
	public void showUpdatePasswordFrame(ActionEvent e) throws IOException {
		(new GameSound()).buttonClickForwardSound();
		Node node = (Node)e.getSource();
		// Getting the Stage where the event is happened
		stage = (Stage)node.getScene().getWindow();
		// changing the AnchorPane from the main file
		anchorPane = (AnchorPane) loadFXML("gameFrame");
		// Setting the size of the anchorPane
		anchorPane.setPrefSize(w, h);
		// Setting the AnchorPane as a root of the main scene
		stage.getScene().setRoot(anchorPane);
		// Showing the Stage
		stage.show();
		
	}
	
	public void clickBack(ActionEvent e) throws IOException {

		gameSoundButton.buttonClickBackwardSound();

		Node node = (Node) e.getSource();
		// Getting the Stage where the event is happened
		stage = (Stage) node.getScene().getWindow();
		// changing the AnchorPane from the main file
		anchorPane = (AnchorPane) loadFXML("mainMenu");
		// Setting the size of the anchorPane
		anchorPane.setPrefSize(w, h);
		// Setting the AnchorPane as a root of the main scene
		stage.getScene().setRoot(anchorPane);
		// Showing the Stage
		stage.show();
	}
	
	public void clickDeleteProfileButton(ActionEvent e) throws IOException {
        gameSoundButton.buttonClickBackwardSound();

		 // Display confirmation dialog
	    Alert alert = new Alert(AlertType.CONFIRMATION);
	    alert.setTitle("Confirm Delete");
	    alert.setHeaderText("Are you sure you want to delete your profile?");
	    alert.setContentText("This includes deleting all the statistics \nconnected with this profile. \nThis action cannot be undone.");
	    // Set the focus to the cancel button
	    Platform.runLater(() -> alert.getDialogPane().lookupButton(ButtonType.CANCEL).requestFocus());
	    
	    Optional<ButtonType> result = alert.showAndWait();
	    if (result.isPresent() && result.get() == ButtonType.OK) {
	        // User clicked OK, proceed with delete
	        MainApp.getAppController();
	        AppController.deleteProfile();
	        Node node = (Node) e.getSource();
	        stage = (Stage) node.getScene().getWindow();
	        anchorPane = (AnchorPane) loadFXML("userAccess");
	        anchorPane.setPrefSize(w, h);
	        stage.getScene().setRoot(anchorPane);
	        stage.show();
	    } else {
	        // User clicked Cancel, do nothing
	    }
	}
	/**
     * 
     * @param fxml, file name without the ending .fxml
     * @return Parent object, to be set as a root in a Scene object
     * @throws IOException
     * 
     * This method is responsible for loading a fxml file
     */
	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(CreateProfilePaneController.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}
}
