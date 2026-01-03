package util;

import Controllers.GuestMakeReservationController;
import handlers.ClientHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage stage;

    public static void setStage(Stage baseStage) {
        stage = baseStage;
    }

    public static void switchTo(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(
                SceneManager.class.getResource("/gui/" + fxml)
            );
            Parent root = loader.load();

            Object controller = loader.getController();

            if (controller instanceof GuestMakeReservationController) {
                ClientHandler.getClient()
                        .setGuestMakeReservationController(
                                (GuestMakeReservationController) controller
                        );
            }

            stage.setScene(new Scene(root));
            stage.setTitle(fxml.replace("UI.fxml", ""));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
