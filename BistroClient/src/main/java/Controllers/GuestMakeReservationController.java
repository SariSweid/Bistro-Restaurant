package Controllers;

import java.time.LocalDate;
import java.time.LocalTime;

import Entities.Reservation;
import enums.ReservationStatus;
import enums.UserRole;
import handlers.ClientHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import messages.RegisterRequest;
import util.SceneManager;

/**
 * Controller for guests to make a reservation without registering as a permanent user.
 * Extends {@link BaseReservationController} to provide reservation functionality.
 * Handles input validation for email or phone and submits reservations for guests.
 */
public class GuestMakeReservationController extends BaseReservationController {

    @FXML
    private TextField numberOfDiners, emailOrPhone;

    /**
     * ID of the current guest making the reservation.
     */
    private int currentGuestId;

    /**
     * Initializes the controller.
     * Sets up the date picker limits and assigns the number of diners field.
     */
    @FXML
    public void initialize() {
        super.initialize();
        this.numberOfDinersField = numberOfDiners;
        this.currentGuestId = 0;
        setupDatePickerLimits();
    }

    /**
     * Handles the "Check Availability" action.
     * Validates the email or phone input before checking available reservation times.
     */
    @FXML
    private void onCheckAvailability() {
        if (emailOrPhone.getText().isBlank()) {
            showError("Please enter your email or phone");
            return;
        }
        String input = emailOrPhone.getText().trim();
        if (!isEmail(input) && !isPhone(input)) {
            showError("Please enter a valid email or phone number.");
            return;
        }
        checkAvailability();
    }

    /**
     * Handles the submission of the guest reservation.
     * Validates email or phone input and submits the reservation.
     */
    @FXML
    private void onSubmitOrder() {
        String input = emailOrPhone.getText().trim();
        String guestEmail = "";
        String guestPhone = "";

        if (isEmail(input)) guestEmail = input;
        else if (isPhone(input)) guestPhone = input;
        else {
            showError("Please enter a valid email or phone number.");
            return;
        }

        submitGuestReservation(guestEmail, guestPhone);
    }

    /**
     * Navigates back to the guest reservation UI.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("GuestReservationUI.fxml");
    }

    /**
     * Validates whether the input string is an email.
     *
     * @param input the string to validate
     * @return true if the input matches an email pattern, false otherwise
     */
    private boolean isEmail(String input) {
        return input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    /**
     * Validates whether the input string is a phone number.
     *
     * @param input the string to validate
     * @return true if the input matches a phone number pattern, false otherwise
     */
    private boolean isPhone(String input) {
        return input.matches("^\\+?[0-9\\-\\s()]{7,20}$");
    }

    /**
     * Generates a random guest ID between 10000 and 20000.
     *
     * @return a randomly generated guest ID
     */
    private int generateGuestId() {
        return 10000 + (int)(Math.random() * 10001);
    }

    /**
     * Generates a unique guest ID.
     *
     * @return a unique guest ID
     */
    private int generateUniqueGuestId() {
        return generateGuestId();
    }

    /**
     * Creates a guest user with the provided email and phone.
     * Sends a registration request to the server.
     *
     * @param email the guest's email address
     * @param phone the guest's phone number
     * @return the guest ID assigned by the system
     */
    private int createGuestUser(String email, String phone) {
        int guestId = generateUniqueGuestId();
        RegisterRequest request = new RegisterRequest(
            guestId,
            null,
            email,
            phone,
            null,
            0,
            UserRole.GUEST
        );

        ClientHandler.getClient().register(request);
        return guestId;
    }

    /**
     * Submits a reservation for a guest with the specified email and phone.
     * Sets the current guest ID and sends the reservation to the server.
     *
     * @param email the guest's email address
     * @param phone the guest's phone number
     */
    private void submitGuestReservation(String email, String phone) {
        currentGuestId = createGuestUser(email, phone);
        ClientHandler.getClient().setCurrentUserId(currentGuestId);
        if (currentGuestId == -1) return;

        LocalTime selectedTime = timeComboBox.getValue();
        if (selectedTime == null) {
            showError("Please select a time.");
            return;
        }

        int diners = Integer.parseInt(numberOfDinersField.getText());
        LocalDate resDate = datePicker.getValue();

        Reservation r = new Reservation(
            0,
            currentGuestId,
            diners,
            0,
            resDate,
            selectedTime,
            ReservationStatus.CONFIRMED,
            true
        );

        ClientHandler.getClient().addReservation(r);
        resetForm();
    }

    /**
     * Submits an alternative reservation for a guest when the original date/time is unavailable.
     * Creates a new guest user if necessary and sends the reservation to the server.
     *
     * @param date the alternative reservation date
     * @param time the alternative reservation time
     * @param diners the number of diners for the reservation
     */
    @Override
    protected void submitAlternativeReservation(LocalDate date, LocalTime time, int diners) {
        String input = emailOrPhone.getText().trim();
        String guestEmail = "";
        String guestPhone = "";

        if (isEmail(input)) guestEmail = input;
        else if (isPhone(input)) guestPhone = input;
        else {
            showError("Cannot create guest: invalid email or phone.");
            return;
        }

        int newGuestId = createGuestUser(guestEmail, guestPhone);
        ClientHandler.getClient().setCurrentUserId(newGuestId);

        Reservation r = new Reservation(
                0,
                newGuestId,
                diners,
                0,
                date,
                time,
                ReservationStatus.CONFIRMED,
                true
        );

        ClientHandler.getClient().addReservation(r);
        resetForm();
    }
}
