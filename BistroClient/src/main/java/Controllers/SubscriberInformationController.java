package Controllers;

import java.util.List;

import Entities.Reservation;
import Entities.User;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import util.SceneManager;
import handlers.ClientHandler;

public class SubscriberInformationController extends BaseDisplayController {

    @FXML private TableView<User> SubscribersTable;

    @FXML private TableColumn<User, Integer> IdColumn;
    @FXML private TableColumn<User, String> NameColumn;
    @FXML private TableColumn<User, String> PhoneColumn;
    @FXML private TableColumn<User, String> EmailColumn;
    @FXML private TableColumn<User, String> UserNameColumn;
    @FXML private TableColumn<User, String> CodeColumn;

    @FXML
    public void initialize() {

        // Register controller
        ClientHandler.getClient().setActiveDisplayController(this);

        // Setup columns
        IdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        EmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        UserNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        CodeColumn.setCellValueFactory(new PropertyValueFactory<>("membershipCode"));

        // --- REQUEST USERS FROM SERVER ---
        ClientHandler.getClient().getAllUsers();
    }

    public void showUsers(List<? extends User> users) {
        SubscribersTable.getItems().setAll(users);
    }

    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SupervisorUI.fxml");
    }

	@Override
	public void showReservations(List<Reservation> list) {
		// TODO Auto-generated method stub
		
	}
}
