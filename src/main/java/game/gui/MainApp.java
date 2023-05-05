package game.gui;

import java.io.IOException;

import general.AppController;
import general.Parameter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
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
        stage.setMinHeight(600);
		stage.setMinWidth(900);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			
		@Override
		public void handle(WindowEvent event) {
			
			Client client = AppController.getClient();
			if(client.isHost()){
				client.sendMessage(new MessageServerCloseConnection());
			}else{
				client.sendMessage(new MessageDisconnect(client.getProfile()));
			}
			Platform.exit();
			System.exit(0);
		}
		});
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
