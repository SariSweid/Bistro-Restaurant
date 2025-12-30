package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.SceneManager;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private ImageView logoImageView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Image image = new Image(getClass().getResourceAsStream("/images/BistroLogo.png"));
            if (image != null) {
                logoImageView.setImage(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
    }
}