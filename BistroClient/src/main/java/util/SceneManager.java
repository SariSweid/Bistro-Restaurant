package util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage stage;

    public static void setStage(Stage Base_Stage) {
        stage = Base_Stage;
    }

    public static void switchTo(String fxml) {
        try {
            Parent root = FXMLLoader.load(
                SceneManager.class.getResource("/gui/" + fxml)
            );
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
