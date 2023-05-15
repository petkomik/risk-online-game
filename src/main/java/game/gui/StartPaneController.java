package game.gui;

import game.gui.GuiSupportClasses.DesignButton;
import game.gui.GuiSupportClasses.ImageViewPane;
import game.gui.GuiSupportClasses.SettingsButton;
import game.gui.GuiSupportClasses.SettingsPane;
import general.AppController;
import general.GameSound;
import general.Parameter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * First Window shown to the player on Application load.
 *
 * @author pmikov
 *
 */
public class StartPaneController extends StackPane {
  private Stage stage;
  private VBox vbox;
  private ImageView imgBackground;
  private ImageViewPane imgBackgroundPane;
  private VBox vboxColor;
  private VBox contentVbox;
  private ImageView riskLogo;
  private DesignButton playButton;
  private SettingsButton settingsButton;
  private StackPane settingsPane;
  private double ratio;

  private GameSound gameSound = AppController.getGameSound();

  /**
   * Default Constructor. Builds the Pane.
   *
   * @throws FileNotFoundException for background map image not found
   */

  public StartPaneController() throws FileNotFoundException {
    super();
    this.ratio = Screen.getPrimary().getVisualBounds().getWidth()
        * Screen.getPrimary().getVisualBounds().getHeight() / (1846 * 1080);
    this.ratio = Math.min(ratio + 0.3, 1);
    setup();
  }

  /**
   * Setup method for the pane. Creates and places nodes. Sets up button events.
   *
   * @throws FileNotFoundException for background map image not found
   */

  public void setup() throws FileNotFoundException {

    this.setAlignment(Pos.CENTER);

    /*
     * First layer of stack Background map image
     */

    vbox = new VBox();
    vbox.setAlignment(Pos.CENTER);
    vbox.setFillWidth(true);

    imgBackground = new ImageView();
    imgBackground.setImage(new Image(new FileInputStream(Parameter.imagesdir + "world-map.png")));
    imgBackground.setPreserveRatio(false);
    imgBackground.setSmooth(true);
    imgBackground.setCache(true);

    imgBackgroundPane = new ImageViewPane(imgBackground);
    VBox.setVgrow(imgBackgroundPane, Priority.ALWAYS);

    vbox.getChildren().add(imgBackgroundPane);

    /*
     * Second layer of stack Color mask
     */

    vboxColor = new VBox();
    vboxColor.setAlignment(Pos.CENTER);
    vboxColor.setFillWidth(true);
    vboxColor.setStyle("-fx-background-color: rgba(225, 211, 184, 0.7);");

    contentVbox = new VBox();
    contentVbox.setAlignment(Pos.CENTER);

    riskLogo = new ImageView();
    riskLogo.setImage(new Image(new FileInputStream(Parameter.logoImage)));
    riskLogo.setFitWidth(650 * ratio);
    riskLogo.setPreserveRatio(true);
    riskLogo.setSmooth(true);
    riskLogo.setCache(true);

    settingsPane = new StackPane();
    settingsButton = new SettingsButton(new Insets(10 * ratio, 20 * ratio, 10 * ratio, 20 * ratio),
        30, 28 * ratio, 170 * ratio, true);

    playButton =
        new DesignButton(new Insets(10 * ratio, 20, 10 * ratio, 20), 35, 40 * ratio, 300 * ratio);
    playButton.setText("Play");

    contentVbox.setSpacing(30 * ratio);

    contentVbox.getChildren().addAll(riskLogo, playButton, settingsButton);
    contentVbox.setPadding(new Insets(0, 0, 50 * ratio, 0));

    double w = MainApp.screenWidth;
    double h = MainApp.screenHeight;
    settingsButton.setLayoutX((40.0 / 1536.0) * w);
    settingsButton.setLayoutY((40.0 / 864.0) * h);
    settingsButton.setPickOnBounds(true);

    // maybe add vBoxColor
    this.getChildren().addAll(vbox, vboxColor, contentVbox);

    playButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();
        Node node = (Node) event.getSource();
        stage = (Stage) node.getScene().getWindow();

        try {
          UserAccessPaneController stp = new UserAccessPaneController();
          stage.getScene().setRoot(stp);

        } catch (FileNotFoundException e1) {
          e1.printStackTrace();
        }
        stage.show();

      }
    });


    Pane home = this;

    settingsButton.setOnAction(new EventHandler<ActionEvent>() {
      @Override
      public void handle(ActionEvent event) {
        gameSound.buttonClickForwardSound();
        settingsPane = SettingsPane.createMutePane(28 * ratio, settingsButton, ratio);
        home.getChildren().add(settingsPane);
        settingsPane.setVisible(true);
        settingsButton.setDisable(true);

      }
    });
    // For deleting the settingsPane imidiatly
    settingsButton.disabledProperty()
        .addListener((ChangeListener<? super Boolean>) new ChangeListener<Boolean>() {
          @Override
          public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
              Boolean newValue) {
            if (!settingsPane.isVisible()) {
              home.getChildren().removeAll(settingsPane);
            }
          }
        });

    contentVbox.setOnKeyPressed(new EventHandler<KeyEvent>() {
      @Override
      public void handle(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
          playButton.fire();

        }
      }
    });
  }
}
