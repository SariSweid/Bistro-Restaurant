package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import util.SceneManager;

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
