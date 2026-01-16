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
    
    /**
     * Handles reservations received from the server.
     * Checks for cancelled reservations that the subscriber has not been notified about
     * and shows a cancellation popup if needed.
     * 
     * @param reservations the list of reservations received from the server
     */
    public void onReservationsReceived(List<Reservation> reservations) {
        for (Reservation res : reservations) {
            if (res.getStatus() == enums.ReservationStatus.CANCELLED && !res.isNotified()) {
                showCancellationPopup(res);
                break;
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
}
