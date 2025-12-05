package server;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 1. Load the FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ServerUI.fxml"));
        Parent root = loader.load();

        // 2. Get the controller created by JavaFX
        ServerUIController uiController = loader.getController();

        // 3. Create the server and connect it to the UI
        ServerController server = new ServerController(ServerController.DEFAULT_PORT);
        server.setUi(uiController);

        try {
            server.listen();
            uiController.setStatus("Status: Server listening on port " + ServerController.DEFAULT_PORT);
        } catch (IOException e) {
            uiController.setStatus("Status: Failed to start server");
            e.printStackTrace();
        }

        // 4. Show the window
        Scene scene = new Scene(root);
        primaryStage.setTitle("Server UI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args)  {
        launch(args);
    }
}

