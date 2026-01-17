package Controllers;

import java.util.List;

import Entities.Reservation;
import handlers.ClientHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.SceneManager;

/**
 * Controller for the Subscriber UI.
 * Handles navigation, reservations, cancellations, and payment reminders.
 */
public class SubscriberController extends BaseReservationController {

    private int userId;

    @FXML
    private Button prevButton;

    @FXML
    private ImageView logoImageView;

    private boolean paymentAlertShown = false;

    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/images/BistroLogo.png"));
        if (image != null) {
            logoImageView.setImage(image);
        }

        ClientHandler.getClient().setActiveReservationController(this);
        this.userId = ClientHandler.getClient().getCurrentUserId();

        boolean showBack = ClientHandler.getClient().cameFromHigherRole();
        prevButton.setVisible(showBack);
        prevButton.setManaged(showBack);

        ClientHandler.getClient().getUserReservations(userId);
    }

    @FXML
    private void onDisplayOrders() {
        SceneManager.switchTo("OrdersUI.fxml");
    }

    @FXML
    private void onNewReservation() {
        SceneManager.switchTo("SubscriberReservationUI.fxml");
    }

    @FXML
    private void onEditPersonalInfo() {
        SceneManager.switchTo("InformationUI.fxml");
    }

    @FXML
    private void onReceiveTable() {
        SceneManager.switchTo("TableReceivingSubscriberUI.fxml");
    }

    @FXML
    private void onCancelOrder() {
        SceneManager.switchTo("CancelReservationUI.fxml");
    }

    @FXML
    private void onwaitinglist() {
        SceneManager.switchTo("SubscriberWaitingListUI.fxml");
    }

    @FXML
    private void onDisplayOrdersHistory() {
        SceneManager.switchTo("SubscribersHistoryOrdersUI.fxml");
    }

    @FXML
    private void onLogOut() {
        ClientHandler.getClient().logout();
        SceneManager.switchTo("MainMenuUI.fxml");
    }

    @FXML
    private void onPrevious() {
        switch (ClientHandler.getClient().getCurrentUserRole()) {
            case SUPERVISOR -> SceneManager.switchTo("SupervisorUI.fxml");
            case MANAGER -> SceneManager.switchTo("ManagerUI.fxml");
            default -> System.out.println("No higher role to go back to");
        }
    }

    public int getUserId() {
        return userId;
    }

    /**
     * Handles reservations received from the server.
     * Shows cancellation popups for cancelled reservations.
     * Shows payment reminder popups for reservations that are completed
     * but had a payment reminder flag set.
     *
     * @param reservations list of reservations received from the server
     */
    public void onReservationsReceived(List<Reservation> reservations) {
        System.out.println("[onReservationsReceived] Received " + reservations.size() + " reservations");

        for (Reservation res : reservations) {
            System.out.println("[onReservationsReceived] Checking reservation ID: " + res.getReservationID() +
                               ", status: " + res.getStatus() +
                               ", paymentReminderSent: " + res.isPaymentReminderSent());

            // Show cancellation popup if needed.
            if (res.getStatus() == enums.ReservationStatus.CANCELLED && !res.isNotified()) {
                System.out.println("[onReservationsReceived] Reservation CANCELLED and not notified, showing popup");
                showCancellationPopup(res);
                break;
            }

            System.out.println("[onReservationsReceived] Before payment reminder check: paymentReminderSent=" 
                    + res.isPaymentReminderSent() + ", paymentAlertShown=" + paymentAlertShown);

            // Show payment reminder only if the reminder was sent and popup not already shown
            if (res.isPaymentReminderSent() && !paymentAlertShown) {
                System.out.println("[onReservationsReceived] Payment reminder conditions met, showing popup");
                paymentAlertShown = true;
                showPaymentReminderPopup(res);
            }

            // Reset the flag if the reservation is COMPLETED and reminder was shown
            if (res.getStatus() == enums.ReservationStatus.COMPLETED && paymentAlertShown) {
                System.out.println("[onReservationsReceived] Reservation COMPLETED and reminder was shown, resetting flag");
                paymentAlertShown = false;
            }
        }
    }



    /**
     * Displays a popup alert for a cancelled reservation
     * and notifies the server that the subscriber has been informed.
     *
     * @param res the cancelled reservation
     */
    private void showCancellationPopup(Reservation res) {
        javafx.application.Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Reservation Cancelled");
            alert.setHeaderText("Important Update regarding your Booking");
            alert.setContentText("Your reservation for " + res.getReservationDate() + " at " + res.getReservationTime() +
                                 " was cancelled due to restaurant schedule changes.\n\nPlease check your email for details.");
            alert.showAndWait();
            ClientHandler.getClient().markReservationAsNotified(res.getReservationID());
        });
    }

    /**
     * Displays a reminder popup for a reservation that has been seated for over 2 hours
     * and hasn't been paid.
     *
     * @param res the seated reservation
     */
    private void showPaymentReminderPopup(Reservation res) {
        javafx.application.Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
            alert.setTitle("Dining Update");
            alert.setHeaderText("Time to Settle the Bill");
            alert.setContentText("You have been seated for over 2 hours. Would you like to pay now through the application?");

            javafx.scene.control.ButtonType payNow = new javafx.scene.control.ButtonType("Pay Now");
            javafx.scene.control.ButtonType later = new javafx.scene.control.ButtonType("Later", javafx.scene.control.ButtonBar.ButtonData.CANCEL_CLOSE);
            
            alert.getButtonTypes().setAll(payNow, later);

            alert.showAndWait().ifPresent(response -> {
                if (response == payNow) {
                	SceneManager.switchTo("PaymentUI.fxml");
                }
            });
        });
    }
}
