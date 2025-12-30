package handlers;

import javafx.fxml.FXML;
import util.SceneManager;

public class LoginController {
	
	//return back to the main page
	@FXML
    private void onPreviousPage() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }
	
	//onward to the subscriber window
	@FXML
	private void onEnter() {
		/*if (userID.getText().isBlank() || membershipCode.getText().isBlank()) {
	        errorLabel.setText("Enter username and password");
	        return;*/
		SceneManager.switchTo("SubscriberUI.fxml");
	}/*should add request to server (search for the subscriber)*/
}
