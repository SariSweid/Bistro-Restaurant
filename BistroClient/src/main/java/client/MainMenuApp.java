package client;

import handlers.ClientHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.SceneManager;
import Controllers.MainMenuController;

import java.util.List;

public class MainMenuApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        SceneManager.setStage(primaryStage);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainMenuUI.fxml"));
        Parent root = loader.load();

        MainMenuController controller = loader.getController();

        Parameters parms = getParameters();
        List<String> args = parms.getUnnamed();
        String host = "localhost";
        if (args != null && !args.isEmpty()) {
            host = args.get(0);
        }

        ClientHandler client = ClientHandler.getClient();
        client.setMainMenuController(controller);
        client.openConnection();

        controller.setClient(client);

        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
