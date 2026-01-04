package Controllers;

import java.time.LocalTime;

import Entities.Subscriber;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import util.SceneManager;

public class SubscriberReservationController extends BaseReservationController {

	@FXML
	public void initialize() {
		setupDatePickerLimits();
	}

    @FXML
    private void onCheckAvailability() {
        checkAvailability(); // no email/phone required
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
