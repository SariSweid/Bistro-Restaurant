package client;

import java.util.List;

import handlers.ClientHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class GuestApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GuestReservationUI.fxml"));
        Parent root = loader.load();

        // Get UI controller
        GuestReservationUI ui = loader.getController();

        //get ip address from args[0]
        Parameters parms = getParameters();
        List<String> list = parms.getUnnamed();
        String host = "";
        
        // -- Create client --
        if (list != null && !list.isEmpty()) {
            host = list.get(0);
        }
        // Now create client safely
        ClientHandler client = new ClientHandler(host, 5555); // or your port
        client.setGuestUI(ui);

        client.openConnection();

        // Show UI
        primaryStage.setTitle("Guest Reservation UI");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

