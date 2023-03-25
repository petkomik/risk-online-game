package game.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import general.AppController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LogInPaneController implements Initializable{
	private Stage stage;
	private AnchorPane anchorPane;

	private double w = MainApp.screenWidth;
	private double h = MainApp.screenHeight;
	
	private GameSound gameSound = new GameSound();

	@FXML
	private Button backButton;
	@FXML
	private Label usernameLabel;
	@FXML
	private Label passwordLabel;
	
	@FXML
	private TextField usernameTF;
	@FXML
	private PasswordField passwordField;

	@FXML
	private Button logInButton;
	
	private void setXYof(double relativeX, double relativeY, Node node) {
		node.setLayoutX(w * relativeX);
		node.setLayoutY(h * relativeY);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/* Setting the size of each element */
		backButton.setPrefSize(w * 0.091, h * 0.058);
		
		usernameLabel.setPrefSize(w * 0.163, h * 0.058);
		passwordLabel.setPrefSize(w * 0.163, h * 0.058);
		
		usernameTF.setPrefSize(w * 0.156, h * 0.035);
		passwordField.setPrefSize(w * 0.156, h * 0.035);

		logInButton.setPrefSize(w * 0.091, h * 0.058);

		/* Setting the x and y coordinates of each element */
		this.setXYof(0.026, 0.046, backButton);
		this.setXYof(0.234, 0.359, usernameLabel);
		this.setXYof(0.234, 0.428, passwordLabel);
		this.setXYof(0.439, 0.37, usernameTF);
		this.setXYof(0.439, 0.44, passwordField);
		this.setXYof(0.505, 0.521, logInButton );
		
		/* Setting the font size */
		double fontSize = 0.135 * Math.sqrt(Math.pow(backButton.getPrefWidth(), 2.0)+Math.pow(backButton.getPrefHeight(), 2.0));
		backButton.setStyle("-fx-font-size: "+fontSize+"px;");
		logInButton.setStyle("-fx-font-size: "+fontSize+"px;");
		fontSize = 0.137 * Math.sqrt(Math.pow(usernameLabel.getPrefWidth(), 2.0)+Math.pow(usernameLabel.getPrefHeight(), 2.0));
		usernameLabel.setStyle("-fx-font-size: "+fontSize+"px;");
		passwordLabel.setStyle("-fx-font-size: "+fontSize+"px;");
		
	}
	
	/**
	 * The method handles the event, when the player clicks on the button 'log in'
	 * 
	 * @param e
	 * @throws IOException
	 */
	public void clickLogIn(ActionEvent e) throws IOException {

		gameSound.buttonClickForwardSound();
		
//		try {
			boolean loginSuccess = AppController.logIntoProfile(usernameTF.getText().trim(), passwordField.getText().trim());

			Node node = (Node) e.getSource();
			// Getting the Stage where the event is happened
			stage = (Stage) node.getScene().getWindow();
			// changing the AnchorPane from the main file
			anchorPane = loginSuccess ? (AnchorPane) loadFXML("mainMenu"):(AnchorPane) loadFXML("logIn");
			// Setting the size of the anchorPane
			anchorPane.setPrefSize(w, h);
			// Setting the AnchorPane as a root of the main scene
			stage.getScene().setRoot(anchorPane);
			// Showing the Stage
			stage.show();
//		} catch (WrongTextFieldInputException e1) {
//			Alert alert = new Alert(AlertType.ERROR);
//			alert.setContentText(e1.getMessage());
//			alert.show();
//		}
	

	}

	/**
	 * The method handles the event, when the player clicks on the button 'back'
	 * 
	 * @param e
	 * @throws IOException
	 */
	public void clickBack(ActionEvent e) throws IOException {

		gameSound.buttonClickBackwardSound();

		Node node = (Node) e.getSource();
		// Getting the Stage where the event is happened
		stage = (Stage) node.getScene().getWindow();
		// changing the AnchorPane from the main file
		anchorPane = (AnchorPane) loadFXML("userAccess");
		// Setting the size of the anchorPane
		anchorPane.setPrefSize(w, h);
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
	 *                     This method is responsible for loading a fxml file
	 */
	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(CreateProfilePaneController.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}
}
