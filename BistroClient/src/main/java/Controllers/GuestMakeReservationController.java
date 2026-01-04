package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import util.SceneManager;

public class GuestMakeReservationController extends BaseReservationController {

    @FXML
    private TextField NumberOfDiners, emailOrPhone;
	private int customerId;

    @FXML
	public void initialize() {
      
        this.numberOfDinersField = NumberOfDiners;

        // Set dummy ID for guest
        this.customerId = 1;
        
        setupDatePickerLimits();
    }

    @FXML
    private void onCheckAvailability() {
        if (emailOrPhone.getText().isBlank()) {
            showError("Please enter your email or phone");
            return;
        }
        checkAvailability();
    }

    @FXML
    private void onSubmitOrder() {
        submitReservation();
    }

    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("GuestReservationUI.fxml");
    }
}
