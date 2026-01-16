package Controllers;

import handlers.ClientHandler;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import util.SceneManager;

/**
 * Controller for the login screen.
 * Handles user login input, validates credentials, and communicates with the server via {@link ClientHandler}.
 * Also manages navigation between the main menu and login screen.
 */
public class LoginController implements Initializable {

    /**
     * ImageView for displaying the application logo.
     */
    @FXML
    private ImageView logoImageView;

    /**
     * Text field for the user to enter their ID.
     */
    @FXML
    private javafx.scene.control.TextField userID;

    /**
     * Password field for the user to enter their membership code.
     */
    @FXML
    private javafx.scene.control.PasswordField membershipCode;

    /**
     * Initializes the login controller.
     * Loads the application logo into the {@link #logoImageView}.
     *
     * @param url the location used to resolve relative paths for the root object
     * @param rb  the resources used to localize the root object
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
     * Handles navigation back to the main menu.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }

    /**
     * Handles the login action when the user clicks "Enter".
     * Validates that the ID and membership code fields are not empty and contain valid numbers.
     * Sends the login request to the server via {@link ClientHandler#login(int, int)}.
     */
    @FXML
    private void onEnter() {
        if (userID.getText().isBlank() || membershipCode.getText().isBlank()) {
            SceneManager.showError("please fill all fields!");
            return;
        }

        int id, code;
        try {
            id = Integer.parseInt(userID.getText());
            code = Integer.parseInt(membershipCode.getText());
        } catch (NumberFormatException e) {
            SceneManager.showError("Login input not valid numbers");
            return;
        }

        ClientHandler.getClient().login(id, code);
    }
}
