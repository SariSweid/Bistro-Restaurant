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
 * Handles navigation for subscribers, including making new reservations,
 * viewing orders, editing personal info, accessing waiting list, and logging out.
 * Also handles reservation notifications.
 */
public class SubscriberController extends BaseReservationController {
    
    /**
     * The ID of the currently logged-in subscriber.
     */
    private int userId;
    
    /**
     * Button to navigate back to a higher role UI if accessed from there.
     */
    @FXML
    private Button prevButton;
    
    /**
     * ImageView to display the restaurant logo.
     */
    @FXML
    private ImageView logoImageView;
    
    /**
     * Initializes the subscriber controller.
     * Loads the logo image, sets this controller as active, retrieves the subscriber ID,
     * manages visibility of the back button if accessed from higher role,
     * and requests user reservations from the server for notifications.
     */
    @FXML
    public void initialize() {
        Image image = new Image(getClass().getResourceAsStream("/images/BistroLogo.png"));
        if (image != null) {
            logoImageView.setImage(image);
        }
        
        ClientHandler.getClient().setActiveReservationController(this);
        this.userId = ClientHandler.getClient().getCurrentUserId();
        System.out.println("Subscriber logged in with userId: " + userId);
        
        // Show the back button only if came from higher role
        boolean showBack = ClientHandler.getClient().cameFromHigherRole();
        prevButton.setVisible(showBack);
        prevButton.setManaged(showBack);
        
        // Request reservations from the server to check for notifications
        ClientHandler.getClient().getUserReservations(userId);
        
        // Start a background thread to check for the 2-hour limit every 2 minutes
        Thread refreshThread = new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(120000); // Wait 2 minutes
                    // This call triggers onReservationsReceived when the server responds
                    ClientHandler.getClient().getUserReservations(userId);
                }
            } catch (InterruptedException e) {
                System.out.println("Refresh thread stopped.");
            }
        });
        refreshThread.setDaemon(true); // Thread dies the app closed
        refreshThread.start();
    }
    
    /**
     * Navigates to the Orders UI.
     */
    @FXML
    private void onDisplayOrders() {
        SceneManager.switchTo("OrdersUI.fxml");
    }

    /**
     * Navigates to the Subscriber Reservation UI to create a new reservation.
     */
    @FXML
    private void onNewReservation() {
        SceneManager.switchTo("SubscriberReservationUI.fxml");
    }

    /**
     * Navigates to the Information UI for editing personal subscriber info.
     */
    @FXML
    private void onEditPersonalInfo() {
        SceneManager.switchTo("InformationUI.fxml");
    }
    
    /**
     * Navigates to the Table Receiving UI for subscribers.
     */
    @FXML
    private void onReceiveTable() {
        SceneManager.switchTo("TableReceivingSubscriberUI.fxml");
    }

    /**
     * Navigates to the Cancel Reservation UI.
     */
    @FXML
    private void onCancelOrder() {
        SceneManager.switchTo("CancelReservationUI.fxml");
    }
    
    /**
     * Navigates to the Subscriber Waiting List UI.
     */
    @FXML
    private void onwaitinglist() {
        SceneManager.switchTo("SubscriberWaitingListUI.fxml");
    }
    
    /**
     * Navigates to the history of subscriber's orders.
     */
    @FXML
    private void onDisplayOrdersHistory() {     
        SceneManager.switchTo("SubscribersHistoryOrdersUI.fxml");
    }
    
    /**
     * Logs out the current subscriber and navigates to the Main Menu.
     */
    @FXML
    private void onLogOut() {
        ClientHandler.getClient().logout();
        SceneManager.switchTo("MainMenuUI.fxml");
    }

    /**
     * Navigates back to the UI of the previous higher role (Supervisor or Manager),
     * if the subscriber came from a higher role.
     */
    @FXML
    private void onPrevious() {
        switch (ClientHandler.getClient().getCurrentUserRole()) {
            case SUPERVISOR -> SceneManager.switchTo("SupervisorUI.fxml");
            case MANAGER -> SceneManager.switchTo("ManagerUI.fxml");
            default -> System.out.println("No higher role to go back to");
        }
    }
    
    /**
     * Returns the subscriber's user ID.
     * @return the user ID
     */
    public int getUserId() {
        return userId;
    }
    
    
    private boolean paymentAlertShown = false;
    /**
     * Handles reservations received from the server.
     * Checks for cancelled reservations that the subscriber has not been notified about
     * and shows a cancellation popup if needed.
     * also, checks if payed, or show reminder popup
     * 
     * @param reservations the list of reservations received from the server
     */
    public void onReservationsReceived(List<Reservation> reservations) {
    		// Logic for system cancellations
        for (Reservation res : reservations) {
        	
        		// Logic for system cancellations
            if (res.getStatus() == enums.ReservationStatus.CANCELLED && !res.isNotified()) {
                showCancellationPopup(res);
                break;
            }
            
            // Logic for reservation time > 2 hours
            if (res.getStatus() == enums.ReservationStatus.SEATED && !paymentAlertShown) {
                java.time.LocalTime now = java.time.LocalTime.now();
                // Use Arrival Time if available, otherwise use Reservation Time
                java.time.LocalTime startTime = (res.getActualArrivalTime() != null) 
                                                 ? res.getActualArrivalTime() 
                                                 : res.getReservationTime();

                // If current time is after (Start Time + 2 Hours)
                if (now.isAfter(startTime.plusHours(2))) {
                		paymentAlertShown = true; // Prevents the popup from showing again
                    showPaymentReminderPopup(res);
                }
            }
            
            // Reset the flag if reservation paid
            if (res.getStatus() == enums.ReservationStatus.COMPLETED) {
                paymentAlertShown = false;
            }
        }
    }

    /**
     * Displays a popup alert for a cancelled reservation and marks it as notified.
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
            
            // Notify server that the user has been informed
            ClientHandler.getClient().markReservationAsNotified(res.getReservationID());
        });
    }
    
    /**
     * Displays a reminder popup for reservation that haven't been payed
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
                    onDisplayOrders(); // Navigates them to their orders/payment screen
                }
            });
        });
    }
}
