package Controllers;

import java.time.LocalTime;

import Entities.Subscriber;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

public class SubscriberReservationController extends BaseReservationController {

    @FXML
    private TextField NumberOfDiners;

    @FXML
    private DatePicker date;

    @FXML
    private ComboBox<LocalTime> timeField;

    @FXML
    private Button confirmButton;

    private Subscriber currentSubscriber;

    @FXML
    private void initialize() {
        this.customerId = currentSubscriber.getUserId();
        this.datePicker = date;
        this.timeComboBox = timeField;
        this.confirmButton = confirmButton;
        this.numberOfDinersField = NumberOfDiners;
    }

    public void setSubscriber(Subscriber s) {
        this.currentSubscriber = s;
        this.customerId = s.getUserId(); // real subscriber ID
    }

    @FXML
    private void onCheckAvailability() {
        checkAvailability(); // no email/phone required
    }

    @FXML
    private void onSubmitOrder() {
        submitReservation();
    }
}
