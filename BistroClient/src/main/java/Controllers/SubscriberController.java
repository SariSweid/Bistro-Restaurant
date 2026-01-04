package Controllers;

import handlers.ClientHandler;
import javafx.fxml.FXML;

import util.SceneManager;

public class SubscriberController {
	
	private int userId;
	
    @FXML
    public void initialize() {
        
        this.userId = ClientHandler.getClient().getCurrentUserId();
        System.out.println("Subscriber logged in with userId: " + userId);
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
    private void onLogOut() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }

    
    @FXML
    private void onCancelOrder() {
    		SceneManager.switchTo("CancelOrderUI.fxml");
    }
    
    
    public int getUserId() {
        return userId;
    }
}
