package Controllers;

import Entities.Manager;
import Entities.Subscriber;
import Entities.Supervisor;
import Entities.User;
import handlers.ClientHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import util.SceneManager;

/**
 * Controller for managing and displaying user information.
 * Handles the initialization of user data in the form fields,
 * submission of changes, and navigation back to the previous page.
 */
public class InformationController {

    @FXML private TextField name;
    @FXML private TextField phone;
    @FXML private TextField email;
    @FXML private TextField userName;

    @SuppressWarnings("unused")
    private User currentUser;

    /**
     * Initializes the form with the current user's information.
     * Populates the text fields based on the user's role.
     */
    @FXML
    public void initialize() {
        User user = ClientHandler.getClient().getCurrentUser();

        email.setText(user.getEmail());
        phone.setText(user.getPhone());

        switch (user.getRole()) {
            case SUBSCRIBER -> {
                Subscriber s = (Subscriber) user;
                name.setText(s.getName());
                userName.setText(s.getUserName());
            }
            case MANAGER -> {
                Manager m = (Manager) user;
                name.setText(m.getName());
                userName.setText(m.getUserName());
            }
            case SUPERVISOR -> {
                Supervisor s = (Supervisor) user;
                name.setText(s.getName());
                userName.setText(s.getUserName());
            }
            default -> throw new IllegalArgumentException("Unexpected value: " + user.getRole());
        }
    }

    /**
     * Submits any changes made to the user's information.
     * Updates the current user's email, phone, name, and username.
     * Calls the client handler to persist the updated user data.
     */
    @FXML
    private void onSubmitChanges() {
        User user = ClientHandler.getClient().getCurrentUser();

        user.setEmail(email.getText());
        user.setPhone(phone.getText());

        switch (user.getRole()) {
            case SUBSCRIBER -> {
                Subscriber s = (Subscriber) user;
                s.setName(name.getText());
                s.setUserName(userName.getText());
            }
            case MANAGER -> {
                Manager m = (Manager) user;
                m.setName(name.getText());
                m.setUserName(userName.getText());
            }
            case SUPERVISOR -> {
                Supervisor s = (Supervisor) user;
                s.setName(name.getText());
                s.setUserName(userName.getText());
            }
            case GUEST -> throw new IllegalArgumentException("Unexpected value: " + user.getRole());
            default -> throw new IllegalArgumentException("Unexpected value: " + user.getRole());
        }

        ClientHandler.getClient().updateUser(user);
    }

    /**
     * Navigates back to the previous page.
     * Switches the scene to the Subscriber UI.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberUI.fxml");
    }
}
