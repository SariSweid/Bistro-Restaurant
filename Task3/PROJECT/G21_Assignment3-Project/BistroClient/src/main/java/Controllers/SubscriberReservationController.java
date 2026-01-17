package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import util.SceneManager;

/**
 * Controller for handling subscriber reservations.
 * Extends BaseReservationController to provide reservation functionality
 * specific to subscribers, including entering the number of diners,
 * checking availability, submitting reservations, and navigation.
 */
public class SubscriberReservationController extends BaseReservationController {

    @FXML
    private TextField numberOfDiners;

    /**
     * Initializes the controller.
     * Sets up the number of diners field and applies date picker limits.
     */
    @FXML
    public void initialize() {
        super.initialize();
        this.numberOfDinersField = numberOfDiners;
        setupDatePickerLimits();
    }

    /**
     * Handles checking table availability for the reservation.
     * Calls the base controller's checkAvailability method.
     */
    @FXML
    private void onCheckAvailability() {
        checkAvailability();
    }

    /**
     * Handles submitting the reservation.
     * Calls the base controller's submitReservation method.
     */
    @FXML
    private void onSubmitOrder() {
        submitReservation();
    }

    /**
     * Navigates back to the Subscriber UI.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberUI.fxml");
    }
}
