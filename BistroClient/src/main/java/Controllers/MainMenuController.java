package Controllers;

import javafx.fxml.FXML;
import util.SceneManager;

public class MainMenuController {

    @FXML
    private void onLogin() {
        SceneManager.switchTo("LoginUI.fxml");
    }

    @FXML
    private void onRegister() {
        SceneManager.switchTo("RegistrationUI.fxml");
    }

    @FXML
    private void onGuestReservation() {
        SceneManager.switchTo("GuestReservationUI.fxml");
    }

    @FXML
    private void onPayment() {
        SceneManager.switchTo("PaymentUI.fxml");
    }

    @FXML
    private void onExit() {
        System.exit(0);
    }//quit
}
