package game.gui;

import java.io.IOException;

import general.AppController;
import general.Parameter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * 
 * @author malaji
 * This class shows the very first page
 */
public class MainApp extends Application{
	
	public static double screenWidth;
	public static double screenHeight;
	private AnchorPane anchorPane;
    private Scene scene;
    public static GameSound themeSound;
    public static AppController appController;
    /**
     * This method returns the GameController instance
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
        stage.setMinHeight(800);
		stage.setMinWidth(1300);
        stage.show();
        
        /**************** Sound *************************//**
        File file = new File(Parameter.themeSong);
		Media media = new Media(file.toURI().toString());
		System.out.println(file.toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();*/
        
        themeSound = new GameSound();
        themeSound.startThemeSong(); 
		
    }

    /**
     * this class method enables the launching of the App outside the class
     * @param args
     */
	public static void runMainApp(String[] args) {
		launch(args);
	}

}
