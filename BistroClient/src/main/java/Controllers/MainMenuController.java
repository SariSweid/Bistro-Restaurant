package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.SceneManager;
import java.net.URL;
import java.util.ResourceBundle;
import handlers.ClientHandler;

/**
 * Controller for the main menu UI.
 * Handles navigation to login, guest reservations, payments, waiting list, 
 * table receiving, and exiting the application.
 * Also initializes the main logo image.
 */
public class MainMenuController implements Initializable {

    /**
     * ImageView for displaying the restaurant logo.
     */
    @FXML
    private ImageView logoImageView;

    /**
     * Client handler instance, can be set externally.
     */
    @SuppressWarnings("unused")
    private ClientHandler client;

    /**
     * Initializes the controller.
     * Sets the logo image in the ImageView.
     *
     * @param url the location used to resolve relative paths for the root object
     * @param rb  the resources used to localize the root object
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image image = new Image(getClass().getResourceAsStream("/images/BistroLogo.png"));
        if (image != null) {
            logoImageView.setImage(image);
        }
    }

    /**
     * Sets the ClientHandler instance for this controller.
     *
     * @param client the ClientHandler instance
     */
    public void setClient(ClientHandler client) {
        this.client = client;
    }

    /**
     * Navigates to the login UI.
     */
    @FXML
    private void onLogin() {
        SceneManager.switchTo("LoginUI.fxml");
    }

    /**
     * Navigates to the guest reservation UI.
     */
    @FXML
    private void onGuestReservation() {
        SceneManager.switchTo("GuestReservationUI.fxml");
    }

    /**
     * Navigates to the payment UI.
     */
    @FXML
    private void onPayment() {
        SceneManager.switchTo("PaymentUI.fxml");
    }

    /**
     * Exits the application.
     */
    @FXML
    private void onExit() {
        System.exit(0);
    }

    /**
     * Navigates to the guest waiting list UI.
     */
    @FXML
    private void onwaitinglist() {
        SceneManager.switchTo("GuestWaitingListUI.fxml");
    }

    /**
     * Navigates to the table receiving UI for guests.
     */
    @FXML
    private void onReceiveTable() {
        SceneManager.switchTo("TableReceivingGuestUI.fxml");
    }

}
