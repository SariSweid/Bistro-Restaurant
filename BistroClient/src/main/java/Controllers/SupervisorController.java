package Controllers;

import handlers.ClientHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import util.SceneManager;

public class SupervisorController {
	
	@FXML
    private Button prevButton;
	
	@FXML
    public void initialize() {
		// Show the back button only if came from higher role
        boolean showBack = ClientHandler.getClient().cameFromHigherRole();
        prevButton.setVisible(showBack);
        prevButton.setManaged(showBack);
    }
	
	@FXML
    private void onShowWaitingList() {
        SceneManager.switchTo("WaitingListUI.fxml");
    }

    @FXML
    private void onShowReservations() {
        SceneManager.switchTo("ReservationsUI.fxml");
    }
    
    @FXML
    private void onShowDiners() {
    		SceneManager.switchTo("CurrentDinersUI.fxml");
    }

    @FXML
    private void onEditTables() {
        SceneManager.switchTo("TablesUI.fxml");
    }
    
    @FXML
    private void onEditOpeningHours() {
        SceneManager.switchTo("RestaurantSettingsUI.fxml");
    }
    
    @FXML
    private void onGetAllUsers() {
        SceneManager.switchTo("SubscribersInformationUI.fxml"); 
    }
    
  @FXML
  private void onRegister() { // it can made only by Supervisor
      SceneManager.switchTo("RegistrationUI.fxml");
  }
  

  @FXML
  private void onsuboptions() { // 
	  // indicate that we are navigating from a higher role
	  ClientHandler.getClient().setCameFromHigherRole(true);
      SceneManager.switchTo("SubscriberUI.fxml");
  }
  
  @FXML
  private void onLogOut() {
	  ClientHandler.getClient().setCameFromHigherRole(false);
      SceneManager.switchTo("MainMenuUI.fxml");
  }
    
  @FXML
  private void onPrevious() {
	  ClientHandler.getClient().setCameFromHigherRole(false);
      SceneManager.switchTo("ManagerUI.fxml");
  }
}
  

