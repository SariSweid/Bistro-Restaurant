package server;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import logicControllers.DailyFunctionController;

/**
 * The main entry point for the Server application.
 * This class initializes the JavaFX environment, sets up the server's communication 
 * layer, and starts background maintenance tasks.
 * * It follows the JavaFX Application lifecycle (start, stop).
 */
public class ServerApp extends Application {

    /**
     * Initializes and launches the server-side infrastructure.
     * 1. Loads the Server GUI using FXML.
     * 2. Starts the network server to listen for client connections.
     * 3. Triggers background automation (DailyFunctionController).
     * * @param primaryStage The main stage for the Server UI.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Step 1: Initialize the UI layer
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ServerUI.fxml"));
        Parent root = loader.load();

        // Get the UI controller to update server status visually
        ServerUIController uiController = loader.getController();

        // Step 2: Initialize the networking layer
        // Create the server instance and bind it to the UI for status updates
        ServerController server = new ServerController(ServerController.DEFAULT_PORT);
        server.setUi(uiController);

        try {
            // Start listening for incoming client requests
            server.listen();
            uiController.setStatus("Status: Server listening on port " + ServerController.DEFAULT_PORT);
        } catch (IOException e) {
            uiController.setStatus("Status: Failed to start server");
            e.printStackTrace();
        }
        
        // Step 3: Initialize background logic
        // Starts the DailyFunctionController timer for periodic tasks 
        // (e.g., automated cancellations or report generations)
        DailyFunctionController dailyFunction = new DailyFunctionController();
        dailyFunction.start();   

        // Finalize and display the UI
        Scene scene = new Scene(root);
        primaryStage.setTitle("RestoManager Server Terminal");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Standard main method to launch the JavaFX application.
     */
    public static void main(String[] args)  {
        launch(args);
    }
}