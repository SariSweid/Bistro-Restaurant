package util;

import Controllers.BaseReservationController;
import Controllers.GuestMakeReservationController;
import handlers.ClientHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 * Utility class for managing scene navigation in the application.
 * Handles switching between FXML screens, storing the previous scene,
 * and showing information or error alerts.
 */
public class SceneManager {

    /**
     * The primary stage of the application.
     */
    private static Stage stage;

    /**
     * Stores the FXML filename of the previous scene for navigation purposes.
     */
    private static String previousScene = null;

    /**
     * Sets the primary stage of the application.
     *
     * @param baseStage the main Stage of the JavaFX application
     */
    public static void setStage(Stage baseStage) {
        stage = baseStage;
    }

    /**
     * Switches to the specified FXML scene.
     * Updates the previousScene field to allow going back.
     * If the controller of the new scene is a GuestMakeReservationController,
     * it registers it as the active reservation controller in ClientHandler.
     *
     * @param fxml the name of the FXML file to switch to (e.g., "MainMenuUI.fxml")
     */
    public static void switchTo(String fxml) {
        try {
            if (stage.getScene() != null) {
                previousScene = stage.getScene().getUserData() != null 
                                ? stage.getScene().getUserData().toString() 
                                : null;
            }

            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource("/gui/" + fxml));
            Parent root = loader.load();

            Object controller = loader.getController();

            if (controller instanceof GuestMakeReservationController) {
                ClientHandler.getClient().setActiveReservationController((BaseReservationController) controller);
            }

            Scene scene = new Scene(root);
            scene.setUserData(fxml); // Store current FXML for reference
            stage.setScene(scene);
            stage.setTitle(fxml.replace("UI.fxml", ""));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Switches to the previously displayed scene, if any.
     */
    public static void switchToPrevious() {
        if (previousScene != null) {
            switchTo(previousScene);
        }
    }

    /**
     * Shows an information alert with the given message.
     *
     * @param msg the message to display
     */
    public static void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * Shows an error alert with the given message.
     *
     * @param msg the error message to display
     */
    public static void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
