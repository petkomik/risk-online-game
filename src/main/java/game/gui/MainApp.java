package game.gui;

import general.AppController;
import general.GameSound;
import general.Parameter;
import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import network.Client;
import network.messages.MessageDisconnect;
import network.messages.MessageServerCloseConnection;

/**
 * This class shows the very first page.
 *
 * @author malaji
 */
public class MainApp extends Application {

  public static double screenWidth;
  public static double screenHeight;
  private AnchorPane anchorPane;
  private Scene scene;
  public static GameSound gameSound;
  public static AppController appController;

  /**
   * This method returns the GameController instance
   * 
   * @return gameController
   */
  public static AppController getAppController() {
    MainApp.appController = AppController.getInstance();
    return MainApp.appController;
  }

  /**
   * This method initializes the required elements
   */
  @Override
  public void start(Stage stage) throws IOException {
    gameSound = AppController.getGameSound();
    screenWidth = Screen.getPrimary().getVisualBounds().getWidth();
    screenHeight = Screen.getPrimary().getVisualBounds().getHeight();
    StartPaneController start = new StartPaneController();
    start.setPrefSize(MainApp.screenWidth, MainApp.screenHeight);
    scene = new Scene(start);
    Image image = new Image(Parameter.dicesIcon);
    stage.getIcons().add(image);
    stage.setScene(scene);
    stage.setMaximized(true);
    stage.setResizable(true);
    stage.setMinHeight(600);
    stage.setMinWidth(900);
    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

      @Override
      public void handle(WindowEvent event) {

        Client client = AppController.getClient();
        if (client != null) {
          if (client.isHost()) {
            client.sendMessage(new MessageServerCloseConnection());
          } else {
            client.sendMessage(new MessageDisconnect(client.getProfile()));
          }
        }
        Platform.exit();
        System.exit(0);
      }
    });
    stage.show();


    gameSound.startThemeSong();

  }

  /**
   * this class method enables the launching of the App outside the class
   * 
   * @param args
   */
  public static void runMainApp(String[] args) {
    launch(args);
  }

}
