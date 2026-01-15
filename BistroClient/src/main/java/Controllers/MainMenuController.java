package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.SceneManager;
import java.net.URL;
import java.util.ResourceBundle;
import handlers.ClientHandler;

public class MainMenuController implements Initializable {

    @FXML
    private ImageView logoImageView;

    @SuppressWarnings("unused")
	private ClientHandler client;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Image image = new Image(getClass().getResourceAsStream("/images/BistroLogo.png"));
        if (image != null) {
            logoImageView.setImage(image);
        }
    }

    public void setClient(ClientHandler client) {
        this.client = client;
    }

    @FXML
    private void onLogin() {
        SceneManager.switchTo("LoginUI.fxml");
    }

//    @FXML
//    private void onRegister() { // it can made only by Supervisor
//        SceneManager.switchTo("RegistrationUI.fxml");
//    }

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
    
    @FXML
    private void onwaitinglist() {
    	SceneManager.switchTo("GuestWaitingListUI.fxml");
    }
    
    @FXML
    private void onReceiveTable() {
    	SceneManager.switchTo("TableReceivingGuestUI.fxml");
    }

}
