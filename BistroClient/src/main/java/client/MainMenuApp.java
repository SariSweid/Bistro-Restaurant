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

        ClientHandler client = ClientHandler.getClient(host);
        client.setMainMenuController(controller);
        client.openConnection();

        controller.setClient(client);
        
        primaryStage.setOnCloseRequest(_ -> {
            try {
                if (client != null && client.isConnected()) {
                    client.closeConnection(); // Explicitly signal the server
                    System.out.println("Closing connection before exit...");
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                System.exit(0); // Ensure the process actually dies
            }
        });

        primaryStage.setTitle("Main Menu");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    
    @Override
    public void stop() throws Exception {
        super.stop();
        // Properly close the client connection when app exits
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

    public static void main(String[] args) {
        launch(args);
    }
}
