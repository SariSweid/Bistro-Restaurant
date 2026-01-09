package Controllers;

import Entities.Manager;
import Entities.Subscriber;
import Entities.Supervisor;
import Entities.User;
import handlers.ClientHandler;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import util.SceneManager;

public class InformationController {

    @FXML private TextField name;
    @FXML private TextField phone;
    @FXML private TextField email;
    @FXML private TextField userName;

    private User currentUser;

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
        }
    }


    @FXML
    private void onSubmitChanges() {
        User user = ClientHandler.getClient().getCurrentUser();

        user.setEmail(email.getText());
        user.setPhone(phone.getText());

        switch (user.getRole()) {
            case SUBSCRIBER -> {
                Subscriber s = (Subscriber) user;
                s.setName(name.getText());
                s.setName(userName.getText());
            }
            case MANAGER -> {
                Manager m = (Manager) user;
                m.setName(name.getText());
                m.setName(userName.getText());
            }
            case SUPERVISOR -> {
                Supervisor s = (Supervisor) user;
                s.setName(name.getText());
                s.setName(userName.getText());
            }
        }

        ClientHandler.getClient().updateUser(user);
    }


    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberUI.fxml");
    }
}