package Controllers;

import javafx.fxml.FXML;
import util.SceneManager;

public class SupervisorController {
	@FXML
    private void onShowWaitingList() {
        SceneManager.switchTo("WaitingListUI.fxml");
    }

    @FXML
    private void onShowReservations() {
        SceneManager.switchTo("ReservationsUI.fxml");
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
        SceneManager.switchTo("SubscribersInformationUI.fxml"); // not exist
    }
    
  @FXML
  private void onRegister() { // it can made only by Supervisor
      SceneManager.switchTo("RegistrationUI.fxml");
  }

    @FXML
    private void onLogOut() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }

}
