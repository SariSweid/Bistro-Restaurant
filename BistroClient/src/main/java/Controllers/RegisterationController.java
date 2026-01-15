package Controllers;

import java.net.URL;
import java.util.ResourceBundle;

import enums.UserRole;
import handlers.ClientHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import messages.RegisterRequest;
import util.SceneManager;

public class RegisterationController implements Initializable {

    @FXML
    private ImageView logoImageView;
    
    @FXML
    private TextField userID, name, phone, email, userName, membershipCode;

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
	    SceneManager.switchTo("SupervisorUI.fxml");
	}
	
	@FXML
	private void onEnter() {
		// Validation
		if (name.getText().isBlank() || phone.getText().isBlank() ||
	            email.getText().isBlank() || userName.getText().isBlank()) {

	            Alert alert = new Alert(Alert.AlertType.ERROR);
	            alert.setTitle("Registration");
	            alert.setHeaderText(null);
	            alert.setContentText("Please fill all required fields!");
	            alert.showAndWait();
	            return;
	        }
		
		// Membership code (only relevant if Subscriber)
		int memCode = 0;
		String text = membershipCode.getText().trim();

		if (!text.isBlank()) {
		    try {
		        memCode = Integer.parseInt(text);

		        
		        if (memCode > 999999 || memCode < 0) {
		            Alert alert = new Alert(Alert.AlertType.ERROR);
		            alert.setTitle("Registration");
		            alert.setHeaderText(null);
		            alert.setContentText("Membership code must be a number with at most 6 digits");
		            alert.showAndWait();
		            return;
		        }

		    } catch (NumberFormatException e) {
		        Alert alert = new Alert(Alert.AlertType.ERROR);
		        alert.setTitle("Registration");
		        alert.setHeaderText(null);
		        alert.setContentText("Membership code must be a number");
		        alert.showAndWait();
		        return;
		    }
		}
        
        // Create RegisterRequest
        // Here we assume Supervisor registration
        RegisterRequest request = new RegisterRequest(
                Integer.parseInt(userID.getText()),
                name.getText(),
                email.getText(),
                phone.getText(),
                userName.getText(),
                memCode,
                UserRole.SUBSCRIBER
        );
        
        // Send to server
        ClientHandler.getClient().register(request);
        
		/*if (userID.getText().isBlank() || membershipCode.getText().isBlank() || 
		 * name.getText().isBlank() || phone.getText().isBlank() || email.getText().isBlank()
		 * || userName.getText().isBlank()) {
	        errorLabel.setText("Fill all Fields");
	        return;*/

		    
//		    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
//		    alert.setTitle("Registration");
//		    alert.setHeaderText(null);
//		    alert.setContentText("Registration was successful!");
//		    alert.showAndWait();
//
//		    
//		    SceneManager.switchTo("SupervisorUI.fxml");
		}
 // not sure if need to switch to this scene after register
	}/*we should make sure new user is added to the DB  */

