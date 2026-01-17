package Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import handlers.ClientHandler;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.SceneManager;

/**
 * Controller for the Manager UI.
 * Handles navigation to different screens (reports, supervisor, subscriber)
 * and manages the manager-specific functionality such as logout.
 */
public class ManagerController {
    
    /**
     * ImageView for displaying the restaurant logo in the Manager UI.
     */
    @FXML
    private ImageView logoImageView;
    
    /**
     * Initializes the controller.
     * Loads the restaurant logo image into the ImageView.
     * 
     * @param url the location used to resolve relative paths for the root object, or null
     * @param rb the resources used to localize the root object, or null
     */
    public void initialize(URL url, ResourceBundle rb) {
        Image image = new Image(getClass().getResourceAsStream("/images/BistroLogo.png"));
        if (image != null) {
            logoImageView.setImage(image);
        }
    }

    /**
     * Handles the "Reports" button action.
     * Navigates to the Month/Year selector screen for generating reports.
     */
    @FXML
    private void onReports() {
        SceneManager.switchTo("MonthYearSelector.fxml");
    }

    /**
     * Handles the "Log Out" button action.
     * Logs out the current user and returns to the main menu.
     */
    @FXML
    private void onLogOut() {
        ClientHandler.getClient().logout();
        ClientHandler.getClient().setCameFromHigherRole(false);
        SceneManager.switchTo("MainMenuUI.fxml");
    }

    /**
     * Handles navigation to the Supervisor screen.
     * Marks that the navigation is from a higher role and switches to Supervisor UI.
     */
    @FXML
    private void onsupervisorscreen() {
        ClientHandler.getClient().setCameFromHigherRole(true);
        SceneManager.switchTo("SupervisorUI.fxml");
    }

    /**
     * Handles navigation to the Subscriber screen.
     * Marks that the navigation is from a higher role and switches to Subscriber UI.
     */
    @FXML
    private void onsubscriberscreen() {
        ClientHandler.getClient().setCameFromHigherRole(true);
        SceneManager.switchTo("SubscriberUI.fxml");
    }
}
