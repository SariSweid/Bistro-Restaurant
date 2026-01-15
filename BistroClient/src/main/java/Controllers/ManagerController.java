package Controllers;

import handlers.ClientHandler;
import javafx.fxml.FXML;
import util.SceneManager;

public class ManagerController {

    @FXML
    private void onReports() {
        SceneManager.switchTo("MonthYearSelector.fxml");
    }

    @FXML
    private void onLogOut() {
    	ClientHandler.getClient().logout();
    		ClientHandler.getClient().setCameFromHigherRole(false);
        SceneManager.switchTo("MainMenuUI.fxml");
    }

    @FXML
    private void onsupervisorscreen() {
    		// indicate that we are navigating from a higher role
    		ClientHandler.getClient().setCameFromHigherRole(true);
        SceneManager.switchTo("SupervisorUI.fxml");
    }

    @FXML
    private void onsubscriberscreen() {
    		// indicate that we are navigating from a higher role
  	  	ClientHandler.getClient().setCameFromHigherRole(true);
        SceneManager.switchTo("SubscriberUI.fxml");
    }
}

