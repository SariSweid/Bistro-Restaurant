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

/**
 * Main JavaFX application class for the main menu.
 * Initializes the client connection and loads the main menu UI.
 */
public class MainMenuApp extends Application {

    /**
     * Starts the JavaFX application.
     * Sets up the main menu scene, initializes the client, and opens the connection.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if the FXML file cannot be loaded or other initialization fails
     */
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

        ClientHandler client = ClientHandler.getClient(host);
        client.setMainMenuController(controller);
        client.openConnection();

        controller.setClient(client);
        
        primaryStage.setOnCloseRequest(_ -> {
            try {
                if (client != null && client.isConnected()) {
                    client.closeConnection();
                    System.out.println("Closing connection before exit...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.exit(0);
            }
        });

        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    
    /**
     * Stops the JavaFX application.
     * Ensures that the client connection is properly closed when the application exits.
     *
     * @throws Exception if closing the client connection fails
     */
    @Override
    public void stop() throws Exception {
        super.stop();
        ClientHandler client = ClientHandler.getClient();
        if (client != null && client.isConnected()) {
            try {
                client.closeConnection();
                System.out.println("Client connection closed on app exit.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Main entry point for the application.
     *
     * @param args command-line arguments; the first argument can be the host to connect to
     */
    public static void main(String[] args) {
        launch(args);
    }
}
