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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * 
 * @author majda
 * The class handles the event on the create profile frame
 */
public class CreateProfilePaneController implements Initializable{
	private Stage stage;
	private AnchorPane anchorPane;
	
	private double w = MainApp.screenWidth;
	private double h = MainApp.screenHeight;
	
	@FXML
	private Button backButton;
	@FXML
	private Label firstNameLabel;
	@FXML
	private Label lastNameLabel;
	@FXML
	private Label usernameLabel;
	@FXML
	private Label passwordLabel;
	
	@FXML
	private TextField firstNameTF;
	@FXML
	private TextField lastNameTF;
	@FXML
	private TextField usernameTF;
	@FXML
	private PasswordField passwordField;

	@FXML
	private Button createProfileButton;
	
	/*
	 * This private method is an help method to initialize the x and y coordinates
	 */
	private void setXYof(double relativeX, double relativeY, Node node) {
		node.setLayoutX(w*relativeX);
		node.setLayoutY(h*relativeY);
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		/* Setting the size of each element */
		
		backButton.setPrefSize(w*0.081, h*0.058);
		
		firstNameLabel.setPrefSize(w*0.134, h*0.058);
		lastNameLabel.setPrefSize(w*0.134, h*0.058);
		usernameLabel.setPrefSize(w*0.134, h*0.058);
		passwordLabel.setPrefSize(w*0.134, h*0.058);
		
		firstNameTF.setPrefSize(w*0.156, h*0.035);
		lastNameTF.setPrefSize(w*0.156, h*0.035);
		usernameTF.setPrefSize(w*0.156, h*0.035);
		passwordField.setPrefSize(w*0.156, h*0.035);
		
		createProfileButton.setPrefSize(w*0.081, h*0.058);
		
		/* Setting the x and y coordinates of each element */
		this.setXYof(0.026, 0.046, backButton);
		this.setXYof(0.234, 0.22, firstNameLabel);
		this.setXYof(0.234, 0.289, lastNameLabel);
		this.setXYof(0.234, 0.359, usernameLabel);
		this.setXYof(0.234, 0.428, passwordLabel);
		this.setXYof(0.439, 0.231, firstNameTF);
		this.setXYof(0.439, 0.301, lastNameTF);
		this.setXYof(0.439, 0.37, usernameTF);
		this.setXYof(0.439, 0.44, passwordField);
		this.setXYof(0.514, 0.521, createProfileButton);
	}
	/**
	 * The method handles the event, when the player clicks on the button 'create'
	 * @param e
	 * @throws IOException
	 */
    public void clickCreate(ActionEvent e) throws IOException {
    	
    	(new GameSound()).buttonClickForwardSound();
    	String firstName = firstNameTF.getText();
    	String lastName = lastNameTF.getText();
    	String username = usernameTF.getText();
    	String password = passwordField.getText();
    	if(!firstName.isBlank() && !lastName.isBlank() && !username.isBlank() && !password.isBlank()) {
    		MainApp.getGameController().createFirstProfile(firstName, lastName, username, password);
        	Node node = (Node)e.getSource();
    		// Getting the Stage where the event is happened
    		stage = (Stage)node.getScene().getWindow();
    		// changing the AnchorPane from the main file
    		anchorPane = (AnchorPane) loadFXML("mainMenu");
    		// Setting the size of the anchorPane
    		anchorPane.setPrefSize(w, h);
    		// Setting the AnchorPane as a root of the main scene
    		stage.getScene().setRoot(anchorPane);
    		// Showing the Stage
    		stage.show();
    	}
    	else {
    		
    	}
    	
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
     * This method is responsible for loading a fxml file
     */
	private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(CreateProfilePaneController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }
}
