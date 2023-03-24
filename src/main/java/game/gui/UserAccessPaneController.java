package game.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class UserAccessPaneController implements Initializable{
	
	private Stage stage;
	private AnchorPane anchorPane;
	private double w = MainApp.screenWidth;
	private double h = MainApp.screenHeight;
	private GameSound gameSound = new GameSound();
	
	@FXML
	private Button logInButton;
	@FXML
	private Button signUpButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		double btnW = w * 0.163;
		double btnH = h * 0.058;
		double btnX = w * 0.419;
		
		logInButton.setPrefSize(btnW, btnH);
		signUpButton.setPrefSize(btnW, btnH);
		logInButton.setLayoutX(btnX);
		signUpButton.setLayoutX(btnX);
		logInButton.setLayoutY(h*0.382);
		signUpButton.setLayoutY(h*0.463);
		double fontSize = 0.078 * Math.sqrt(Math.pow(btnW, 2.0)+Math.pow(btnH, 2.0));
		logInButton.setStyle("-fx-font-size: "+fontSize+"px;");
		signUpButton.setStyle("-fx-font-size: "+fontSize+"px;");
	}
	
	public void clickLogIn(ActionEvent e) throws IOException {
		
		gameSound.buttonClickForwardSound();
		
		Node node = (Node)e.getSource();
		stage = (Stage)node.getScene().getWindow();
		anchorPane = (AnchorPane) loadFXML("logIn");
		anchorPane.setPrefSize(w, h);
		stage.getScene().setRoot(anchorPane);
		stage.show();
	}
	public void clickSignUp(ActionEvent e) throws IOException {
		
		gameSound.buttonClickForwardSound();
		
		Node node = (Node)e.getSource();
		stage = (Stage)node.getScene().getWindow();
		anchorPane = (AnchorPane) loadFXML("createProfile");
		anchorPane.setPrefSize(w, h);
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

	
