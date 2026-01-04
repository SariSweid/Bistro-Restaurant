package Controllers;

import java.time.LocalTime;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class GuestMakeReservationController extends BaseReservationController {

	@FXML
    private TextField NumberOfDiners, emailOrPhone;

    @FXML
    private DatePicker datePicker;

    @FXML
    private ComboBox<LocalTime> timeComboBox;

    @FXML
    private Button confirmButton;

    @FXML
    private void initialize() {
        this.customerId = 1; // dummy guest ID
        this.datePicker = datePicker;
        this.timeComboBox = timeComboBox;
        this.confirmButton = confirmButton;
        this.numberOfDinersField = NumberOfDiners;
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

}
