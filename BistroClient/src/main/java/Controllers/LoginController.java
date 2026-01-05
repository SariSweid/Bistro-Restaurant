package Controllers;

import java.io.IOException;
import common.Message;
import enums.ActionType;
import handlers.ClientHandler;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import messages.LoginRequest;
import util.SceneManager;

public class LoginController implements Initializable {

    @FXML
    private ImageView logoImageView;
    
    @FXML
    private javafx.scene.control.TextField userID;

    @FXML
    private javafx.scene.control.TextField membershipCode;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Image image = new Image(getClass().getResourceAsStream("/images/BistroLogo.png"));
            if (image != null) {
                logoImageView.setImage(image);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	//return back to the main page
	@FXML
    private void onPreviousPage() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }
	
	//onward to the subscriber window
	@FXML
	private void onEnter() {
		if (userID.getText().isBlank() || membershipCode.getText().isBlank()) {
	        System.out.println("Login fields empty!");
	        return;
	    }

	    int id, code;
	    try {
	        id = Integer.parseInt(userID.getText());
	        code = Integer.parseInt(membershipCode.getText());
	    } catch (NumberFormatException e) {
	        System.out.println("Login input not valid numbers");
	        return;
	    }

	    // Send using existing login() method
	    ClientHandler.getClient().login(id, code);
		/*if (userID.getText().isBlank() || membershipCode.getText().isBlank()) {
	        errorLabel.setText("Enter username and password");
	        return;*/
		//SceneManager.switchTo("SupervisorUI.fxml");
	}/*should add request to server (search for the subscriber)*/
}
