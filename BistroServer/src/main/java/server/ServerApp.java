package server;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logicControllers.DailyFunctionController;

public class ServerApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //load the FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ServerUI.fxml"));
        Parent root = loader.load();

        // get the controller created by JavaFX
        ServerUIController uiController = loader.getController();

        // create the server and connect it to the UI
        ServerController server = new ServerController(ServerController.DEFAULT_PORT);
        server.setUi(uiController);

        try {
            server.listen();
            uiController.setStatus("Status: Server listening on port " + ServerController.DEFAULT_PORT);
        } catch (IOException e) {
            uiController.setStatus("Status: Failed to start server");
            e.printStackTrace();
        }
        
        //  Start the DailyFunctionController timer
        DailyFunctionController DailyFunction = new DailyFunctionController();
        DailyFunction.start();   

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

