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

/**
 * Controller for the Registration UI.
 * Handles user registration for subscribers or supervisors.
 * Validates input fields and sends registration requests to the server.
 */
public class RegisterationController implements Initializable {

    /**
     * ImageView for displaying the restaurant logo.
     */
    @FXML
    private ImageView logoImageView;
    
    /**
     * Text fields for user input during registration:
     * - userID: the ID of the user
     * - name: full name
     * - phone: phone number
     * - email: email address
     * - userName: username for login
     * - membershipCode: optional membership code (for subscribers)
     */
    @FXML
    private TextField userID, name, phone, email, userName, membershipCode;

    /**
     * Initializes the controller.
     * Loads the restaurant logo into the ImageView.
     *
     * @param url  the location used to resolve relative paths for the root object, or null
     * @param rb   the resources used to localize the root object, or null
     */
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
    
    /**
     * Navigates back to the previous page (Supervisor UI).
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SupervisorUI.fxml");
    }
    
    /**
     * Handles the registration process when the "Enter" button is clicked.
     * Performs validation on all required fields, checks membership code format,
     * creates a RegisterRequest, and sends it to the server via ClientHandler.
     */
    @FXML
    private void onEnter() {
        // Validate required fields
        if (name.getText().isBlank() || phone.getText().isBlank() ||
            email.getText().isBlank() || userName.getText().isBlank()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Registration");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all required fields!");
            alert.showAndWait();
            return;
        }
        
        // Membership code (optional, only for subscribers)
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
        
        // Create registration request (assuming subscriber registration)
        RegisterRequest request = new RegisterRequest(
                Integer.parseInt(userID.getText()),
                name.getText(),
                email.getText(),
                phone.getText(),
                userName.getText(),
                memCode,
                UserRole.SUBSCRIBER
        );
        
        // Send request to the server
        ClientHandler.getClient().register(request);
    }
}
