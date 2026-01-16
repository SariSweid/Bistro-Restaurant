package Controllers;

import java.util.List;

import Entities.Reservation;
import handlers.ClientHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import util.SceneManager;

public class SubscriberController extends BaseReservationController {
	
	private int userId;
	
	 @FXML
	 private Button prevButton;
	
    @FXML
    public void initialize() {
        
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
    private void onDisplayOrdersHistory(){ 	
    	SceneManager.switchTo("SubscribersHistoryOrdersUI.fxml");
    }
    
    @FXML
    private void onLogOut() {
    		ClientHandler.getClient().logout(); // send logout request to server
        SceneManager.switchTo("MainMenuUI.fxml");
    }

    @FXML
    private void onPrevious() {
        // Navigate back to the previous role
    		switch (ClientHandler.getClient().getCurrentUserRole()) {
            case SUPERVISOR -> SceneManager.switchTo("SupervisorUI.fxml");
            case MANAGER -> SceneManager.switchTo("ManagerUI.fxml");
            default -> System.out.println("No higher role to go back to");
        }
    }
    
    
    public int getUserId() {
        return userId;
    }
    
    // Method to handle the list when it comes back from the Server
    public void onReservationsReceived(List<Reservation> reservations) {
        for (Reservation res : reservations) {
            // TRIGGER: If status is CANCELLED and the user hasn't been notified yet
            if (res.getStatus() == enums.ReservationStatus.CANCELLED && !res.isNotified()) {
                showCancellationPopup(res);
                break;
            }
        }
    }

    private void showCancellationPopup(Reservation res) {
        javafx.application.Platform.runLater(() -> {
            javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alert.setTitle("Reservation Cancelled");
            alert.setHeaderText("Important Update regarding your Booking");
            alert.setContentText("Your reservation for " + res.getReservationDate() + " at " + res.getReservationTime() + 
                                 " was cancelled due to restaurant schedule changes.\n\nPlease check your email for details.");
            
            alert.showAndWait();
            
            // Tell the server to set isNotified = 1
            ClientHandler.getClient().markReservationAsNotified(res.getReservationID());
        });
    }
}

