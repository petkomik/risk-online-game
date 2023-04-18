package game.gui;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import general.AppController;
import general.Parameter;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import network.Client;
import network.Server;
import network.messages.MessageDisconnect;
import network.messages.MessageSend;
import network.messages.MessageServerCloseConnection;
import network.messages.MessageToPerson;

public class JoinClientMessengerController implements Initializable {

	private double w = MainApp.screenWidth;
	private double h = MainApp.screenHeight;

	@FXML
	private Button sendButton;
	@FXML
	private TextField textFieldMessage;
	@FXML
	private Button disconnectButton = new Button();
	@FXML
	private VBox vBoxMessages;
	@FXML
	private ScrollPane scrollPaneMain;

	private Client client = AppController.getClient();
	private static Socket socket;
	private int port = AppController.getPortNumber();
	private String host = AppController.getHost();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sendButton.setPrefSize(w * 0.091, h * 0.058);
		disconnectButton.setPrefSize(w * 0.091, h * 0.058);
		disconnectButton.setMinSize(w * 0.091, h * 0.058);
		textFieldMessage.setPrefSize(w * 0.400, h * 0.100);
		vBoxMessages.setPrefSize(w * 0.391, h * 0.358);
		scrollPaneMain.setPrefSize(w * 0.400, h * 0.370);

		this.setXYof(0.430, 0.200, sendButton);
		this.setXYof(0.026, 0.046, disconnectButton);
		this.setXYof(0.439, 0.800, textFieldMessage);
		this.setXYof(0.430, 0.301, scrollPaneMain);

		client.listenForMessage(vBoxMessages);

		vBoxMessages.heightProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				scrollPaneMain.setVvalue((Double) newValue);
			}
		});

		sendButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				String messageToSend = textFieldMessage.getText();
				if (!messageToSend.isBlank()) {
					HBox hBox = new HBox();
					hBox.setAlignment(Pos.CENTER_RIGHT);
					hBox.setPadding(new Insets(5, 5, 5, 10));
					Text text = new Text(messageToSend);
					TextFlow textFlow = new TextFlow(text);
					textFlow.setStyle("-fx-color: rgb(239,242,255); " + "-fx-background-color: rgb(15,125,242); "
							+ "-fx-background-radius: 20px;");
					textFlow.setPadding(new Insets(5, 10, 5, 10));
					text.setFill(Color.color(0.934, 0.945, 0.996));

					hBox.getChildren().add(textFlow);

					vBoxMessages.getChildren().add(hBox);

					Platform.runLater(new Runnable() {
						@Override
						public void run() {
							System.out.println("Run of join sworks, this is what it starts with ");
							if (messageToSend.contains(":")) {

								System.out.println(messageToSend.substring(0, messageToSend.indexOf(":")));
								String username = messageToSend.substring(0, messageToSend.indexOf(":"));
								// send the message to the specified user
								client.sendMessage(new MessageToPerson(messageToSend, username));

							} else if (!messageToSend.equals(null)) {
								// send the message to the general chat
								client.sendMessage(messageToSend);

							}
						}
					});

					textFieldMessage.clear();

				}
			}
		});

	}

	public void disconnectB(ActionEvent e) throws IOException {
		Node node = (Node) e.getSource();
		// Getting the Stage where the event is happened
		Stage stage = (Stage) node.getScene().getWindow();
		// changing the AnchorPane from the main file
		AnchorPane anchorPane = (AnchorPane) loadFXML("main");
		// Setting the size of the anchorPane
		anchorPane.setPrefSize(w, h);
		// Setting the AnchorPane as a root of the main scene
		stage.getScene().setRoot(anchorPane);
		// Showing the Stage
		stage.show();
		this.client.sendMessage(new MessageDisconnect(client.getProfile()));
		client.closeEverything();
	}

	private static Parent loadFXML(String fxml) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(StartPaneController.class.getResource(fxml + ".fxml"));
		return fxmlLoader.load();
	}

	public static void addLabel(String messageFromCLient, VBox vBox) {
		HBox hBox = new HBox();
		hBox.setAlignment(Pos.CENTER_LEFT);
		hBox.setPadding(new Insets(5, 5, 5, 10));

		Text text = new Text(messageFromCLient);
		TextFlow textFlow = new TextFlow(text);
		textFlow.setStyle("-fx-color: rgb(239,242,255); " + "-fx-background-color: rgb(233,233,235); "
				+ "-fx-background-radius: 20px;");
		textFlow.setPadding(new Insets(5, 10, 5, 10));
		hBox.getChildren().add(textFlow);

		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				vBox.getChildren().add(hBox);
			}
		});

	}

	private void setXYof(double relativeX, double relativeY, Node node) {
		node.setLayoutX(w * relativeX);
		node.setLayoutY(h * relativeY);
	}

}
