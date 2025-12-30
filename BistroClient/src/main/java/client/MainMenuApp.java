package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.SceneManager;

public class MainMenuApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //give SceneManager the stage
        SceneManager.setStage(primaryStage);
        
        Parent root = FXMLLoader.load(getClass().getResource("/gui/MainMenuUI.fxml"));
        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
