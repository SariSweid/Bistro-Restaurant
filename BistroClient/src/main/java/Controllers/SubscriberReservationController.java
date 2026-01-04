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


    private Subscriber currentSubscriber;



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
    
    
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberUI.fxml");
    }
}
