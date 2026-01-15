package Controllers;

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
}

