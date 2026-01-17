package Controllers;

import handlers.ClientHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.SceneManager;

/**
 * Controller for the Supervisor main interface.
 * Provides navigation to various supervisor functionalities such as managing reservations,
 * current diners, waiting list, tables, restaurant settings, subscriber information, and user registration.
 * Handles logout and navigation to higher/lower role screens.
 */
public class SupervisorController {
    
    /**
     * Button to navigate back to the previous screen.
     * Visible only if the user came from a higher role.
     */
    @FXML
    private Button prevButton;

    /**
     * ImageView displaying the application logo.
     */
    @FXML
    private ImageView logoImageView;

    /**
     * Initializes the supervisor controller.
     * Configures the visibility of the back button based on the user's role
     * and loads the application logo.
     */
    @FXML
    public void initialize() {
        boolean showBack = ClientHandler.getClient().cameFromHigherRole();
        prevButton.setVisible(showBack);
        prevButton.setManaged(showBack);

        Image image = new Image(getClass().getResourceAsStream("/images/BistroLogo.png"));
        if (image != null) {
            logoImageView.setImage(image);
        }
    }

    /**
     * Navigates to the waiting list UI.
     */
    @FXML
    private void onShowWaitingList() {
        SceneManager.switchTo("WaitingListUI.fxml");
    }

    /**
     * Navigates to the reservations UI.
     */
    @FXML
    private void onShowReservations() {
        SceneManager.switchTo("ReservationsUI.fxml");
    }

    /**
     * Navigates to the current diners UI.
     */
    @FXML
    private void onShowDiners() {
        SceneManager.switchTo("CurrentDinersUI.fxml");
    }

    /**
     * Navigates to the table editing UI.
     */
    @FXML
    private void onEditTables() {
        SceneManager.switchTo("TablesUI.fxml");
    }

    /**
     * Navigates to the restaurant opening hours editing UI.
     */
    @FXML
    private void onEditOpeningHours() {
        SceneManager.switchTo("RestaurantSettingsUI.fxml");
    }

    /**
     * Navigates to the subscribers information UI to view all users.
     */
    @FXML
    private void onGetAllUsers() {
        SceneManager.switchTo("SubscribersInformationUI.fxml"); 
    }

    /**
     * Navigates to the registration UI to register a new user.
     * This action can only be performed by a supervisor.
     */
    @FXML
    private void onRegister() {
        SceneManager.switchTo("RegistrationUI.fxml");
    }

    /**
     * Navigates to the subscriber options UI, indicating navigation from a higher role.
     */
    @FXML
    private void onsuboptions() {
        ClientHandler.getClient().setCameFromHigherRole(true);
        SceneManager.switchTo("SubscriberUI.fxml");
    }

    /**
     * Logs out the current supervisor, resets the higher role flag,
     * and navigates back to the main menu.
     */
    @FXML
    private void onLogOut() {
        ClientHandler.getClient().logout();
        ClientHandler.getClient().setCameFromHigherRole(false);
        SceneManager.switchTo("MainMenuUI.fxml");
    }

    /**
     * Navigates back to the manager UI screen and resets the higher role flag.
     */
    @FXML
    private void onPrevious() {
        ClientHandler.getClient().setCameFromHigherRole(false);
        SceneManager.switchTo("ManagerUI.fxml");
    }
}
