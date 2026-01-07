package Controllers;

import javafx.fxml.FXML;
import util.SceneManager;

public class ManagerController {
	@FXML
    private void onShowSubscribersReport() {
        SceneManager.switchTo("SubscribersReportUI.fxml");
    }

    @FXML
    private void onShowTimesReport() {
        SceneManager.switchTo("TimesReportUI.fxml");
    }

    @FXML
    private void onLogOut() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }
    
    @FXML
    private void onsupervisorscreen() {
        SceneManager.switchTo("SupervisorUI.fxml");
    }
    
    @FXML
    private void onsubscriberscreen() {
        SceneManager.switchTo("SubscriberUI.fxml");
    }
    
}
