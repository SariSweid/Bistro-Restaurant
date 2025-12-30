package handlers;

import javafx.fxml.FXML;
import util.SceneManager;

public class ManagerController {
	@FXML
    private void onShowSubscribersReport() {
        SceneManager.switchTo("ReportUI.fxml");
    }

    @FXML
    private void onShowTimesReport() {
        SceneManager.switchTo("ReportUI.fxml");
    }

    @FXML
    private void onLogOut() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }
}
