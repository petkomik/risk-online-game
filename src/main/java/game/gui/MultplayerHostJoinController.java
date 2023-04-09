package game.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import general.Parameter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class MultplayerHostJoinController implements Initializable {
	private Stage stage;
	private AnchorPane anchorPane;
	private double w = MainApp.screenWidth;
	private double h = MainApp.screenHeight;

	@FXML
	private Button hostGameButton;
	@FXML
	private Button joinGameButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		double btnW = w * 0.163;
		double btnH = h * 0.058;
		double btnX = w * 0.419;
		hostGameButton.setPrefSize(btnW, btnH);
		joinGameButton.setPrefSize(btnW, btnH);

		hostGameButton.setLayoutX(btnX);
		joinGameButton.setLayoutX(btnX);

		hostGameButton.setLayoutY(h * 0.301);
		joinGameButton.setLayoutY(h * 0.382);

		double fontSize = 0.078 * Math.sqrt(Math.pow(btnW, 2.0) + Math.pow(btnH, 2.0));
		hostGameButton.setStyle("-fx-font-size: " + fontSize + "px;");
		joinGameButton.setStyle("-fx-font-size: " + fontSize + "px;");

	}

	public void startServer(ActionEvent e) throws IOException {


		Node node = (Node) e.getSource();
		// Getting the Stage where the event is happened
		stage = (Stage) node.getScene().getWindow();
		// changing the AnchorPane from the main file
		anchorPane = (AnchorPane) loadFXML("HostServerMessengerFrame");
		// Setting the size of the anchorPane
		anchorPane.setPrefSize(w, h);
		// Setting the AnchorPane as a root of the main scene
		stage.getScene().setRoot(anchorPane);
		// Showing the Stage
		stage.show();
		
		(new GameSound()).buttonClickForwardSound();

	}
	
	public void joinServer(ActionEvent e) throws IOException {
		Node node = (Node) e.getSource();
		// Getting the Stage where the event is happened
		stage = (Stage) node.getScene().getWindow();
		// changing the AnchorPane from the main file
		anchorPane = (AnchorPane) loadFXML("MultiplayerJoinWindowFrame");
		// Setting the size of the anchorPane
		anchorPane.setPrefSize(w, h);
		// Setting the AnchorPane as a root of the main scene
		stage.getScene().setRoot(anchorPane);
		// Showing the Stage
		stage.show();
		
		(new GameSound()).buttonClickForwardSound();

	}
	
	private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(StartPaneController.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
	}

}
