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

public class GuestMakeReservationController extends BaseReservationController {

    @FXML
    private TextField numberOfDiners, emailOrPhone;

    private int currentGuestId;

    @FXML
    public void initialize() {
    		super.initialize();
        this.numberOfDinersField = numberOfDiners;
        this.currentGuestId = 0;
        setupDatePickerLimits();
    }

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
        
        System.out.println("num or mail is" + guestEmail + guestPhone);

        submitGuestReservation(guestEmail, guestPhone);
    }

    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("GuestReservationUI.fxml");
    }

    private boolean isEmail(String input) {
        return input.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }

    private boolean isPhone(String input) {
    	return input.matches("^\\+?[0-9\\-\\s()]{7,20}$");
    }

    private int generateGuestId() {
        return 10000 + (int)(Math.random() * 10001);
    }

    private int generateUniqueGuestId() {
        return generateGuestId();
    }

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

        // Send to server
        ClientHandler.getClient().register(request);
        return guestId;
    }

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
