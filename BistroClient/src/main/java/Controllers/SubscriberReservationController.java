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
    private TextField numberOfDiners;

    @FXML
    public void initialize() {
    		super.initialize();
    		this.numberOfDinersField = numberOfDiners;
        setupDatePickerLimits();
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
        SceneManager.switchTo("SubscriberUI.fxml");
    }
}
