package game.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import game.WrongTextFieldInputException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import network.Client;
import general.AppController;
import general.Parameter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * 
 * @author pmalamov the class handles the join server event
 *
 */
public class MultiplayerJoinWindowController implements Initializable {
	private Stage stage;
	private AnchorPane anchorPane;
	private double w = MainApp.screenWidth;
	private double h = MainApp.screenHeight;
	private GameSound gameSound = new GameSound();

	@FXML
	private Button joinServerButton;
	@FXML
	private Button backButton;
	@FXML
	private Label ipAddressLabel;
	@FXML
	private Label portNumberLabel;

	@FXML
	private TextField ipAddressTF;
	@FXML
	private TextField portNumberTF;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		/* Setting the size of the elements */

		backButton.setPrefSize(w * 0.091, h * 0.058);
		joinServerButton.setPrefSize(w * 0.163, h * 0.058);

		ipAddressLabel.setPrefSize(w * 0.163, h * 0.058);
		portNumberLabel.setPrefSize(w * 0.163, h * 0.058);

		ipAddressTF.setPrefSize(w * 0.156, h * 0.035);
		portNumberTF.setPrefSize(w * 0.156, h * 0.035);

		/* Setting the x and y coordinates of the elements */

		backButton.setLayoutX(w * 0.026);
		backButton.setLayoutY(h * 0.046);
		joinServerButton.setLayoutX(w * 0.505 - w * 0.135 / 2);
		joinServerButton.setLayoutY(h * 0.521);
		ipAddressLabel.setLayoutX(0.234 * w);
		ipAddressLabel.setLayoutY(0.359 * h);
		portNumberLabel.setLayoutX(0.234 * w);
		portNumberLabel.setLayoutY(0.428 * h);
		ipAddressTF.setLayoutX(0.439 * w);
		ipAddressTF.setLayoutY(0.37 * h);
		portNumberTF.setLayoutX(0.439 * w);
		portNumberTF.setLayoutY(0.44 * h);

		/* Font options */
		double fontSize = 0.135
				* Math.sqrt(Math.pow(backButton.getPrefWidth(), 2.0) + Math.pow(backButton.getPrefHeight(), 2.0));
		backButton.setStyle("-fx-font-size: " + fontSize + "px;");
		joinServerButton.setStyle("-fx-font-size: " + fontSize + "px;");
		fontSize = 0.137 * Math
				.sqrt(Math.pow(ipAddressLabel.getPrefWidth(), 2.0) + Math.pow(ipAddressLabel.getPrefHeight(), 2.0));
		ipAddressLabel.setStyle("-fx-font-size: " + fontSize + "px;");
		portNumberLabel.setStyle("-fx-font-size: " + fontSize + "px;");
	}

	/**
	 * The method handles the event, when the user presses on the "Join Server"
	 * button
	 * 
	 * @param e
	 * @throws IOException
	 */

	public void clickJoinServer(ActionEvent e) throws IOException {

		gameSound.buttonClickForwardSound();
		// to be used as server data inputs
		String ipAddress = ipAddressTF.getText();
		String portNumber = portNumberTF.getText();

		try {

			if (!ipAddress.isBlank() && ipAddress.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b|localhost\\b\r\n" + "")) {
				AppController.setHost(ipAddress);
			}

			if (!portNumber.isBlank() && portNumber.matches("\\b\\d{1,5}\\b\r\n" + "")) {
				AppController.setPortNumber(Integer.parseInt(portNumber));
			}

			AppController.setClient(Client.createClient(AppController.getHost(), AppController.getPortNumber()));
		} catch (IOException e1) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setContentText(e1.getMessage());
			alert.setHeaderText("ERROR");
			alert.setTitle("");
			Stage tmp = (Stage) alert.getDialogPane().getScene().getWindow();
			tmp.getIcons().add(new Image(Parameter.errorIcon));
			alert.showAndWait();
			e1.printStackTrace();
			return;
		}

		Node node = (Node) e.getSource();
		// Getting the Stage where the event is happened
		stage = (Stage) node.getScene().getWindow();
		// changing the AnchorPane from the main file
		anchorPane = (AnchorPane) loadFXML("JoinClientMessengerFrame");
		// Setting the size of the anchorPane
		anchorPane.setPrefSize(w, h);
		// Setting the AnchorPane as a root of the main scene
		stage.getScene().setRoot(anchorPane);
		// Showing the Stage
		stage.show();
	}

	public void clickBack(ActionEvent e) throws IOException {

		gameSound.buttonClickBackwardSound();

		Node node = (Node) e.getSource();
		// Getting the Stage where the event is happened
		stage = (Stage) node.getScene().getWindow();
		// changing the AnchorPane from the main file
		anchorPane = (AnchorPane) loadFXML("MultiplayerHostJoinFrame");
		// Setting the size of the anchorPane
		anchorPane.setPrefSize(w, h);
		// Setting the AnchorPane as a root of the main scene
		stage.getScene().setRoot(anchorPane);
		// Showing the Stage
		stage.show();
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(StartPaneController.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

}
