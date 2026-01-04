package util;

import Controllers.BaseReservationController;
import Controllers.GuestMakeReservationController;
import handlers.ClientHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
            			ClientHandler.getClient().setActiveReservationController(
                        (BaseReservationController) controller
                    );
            }

            stage.setScene(new Scene(root));
            stage.setTitle(fxml.replace("UI.fxml", ""));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    public static void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    public static void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
