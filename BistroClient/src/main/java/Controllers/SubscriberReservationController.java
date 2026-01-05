package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import util.SceneManager;
import javafx.scene.control.Button;
import java.time.LocalTime;
import handlers.ClientHandler;

public class SubscriberReservationController extends BaseReservationController {

    @FXML
    private TextField numberOfDinersField;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<LocalTime> timeComboBox;

    @FXML
    private Button confirmButton;

    @FXML
    public void initialize() {
        super.initialize();
        ClientHandler.getClient().setActiveReservationController(this);
    }

    @FXML
    private void onCheckAvailability() {
        checkAvailability();
    }

    @FXML
    private void onSubmitOrder() {
        submitReservation();
    }
    
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberControllerUI.fxml");
    }
}
