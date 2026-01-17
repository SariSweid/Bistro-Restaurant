package Controllers;

import java.util.List;
import Entities.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import util.SceneManager;
import handlers.ClientHandler;

/**
 * Controller for displaying information about subscribers.
 * Handles initialization of the TableView, populating subscriber data,
 * enabling history viewing, and navigation between scenes.
 */
public class SubscriberInformationController extends BaseDisplayController {

    @FXML private TableView<User> SubscribersTable;
    @FXML private TableColumn<User, Integer> IdColumn;
    @FXML private TableColumn<User, String> NameColumn;
    @FXML private TableColumn<User, String> PhoneColumn;
    @FXML private TableColumn<User, String> EmailColumn;
    @FXML private TableColumn<User, String> UserNameColumn;
    @FXML private TableColumn<User, String> CodeColumn;
    @FXML private Button showHistoryButton;

    /**
     * Initializes the controller.
     * Sets up TableView columns, fetches all users from the client,
     * and configures the Show History button to be enabled only when a subscriber is selected.
     */
    @FXML
    public void initialize() {
        ClientHandler.getClient().setActiveDisplayController(this);

        IdColumn.setCellValueFactory(new PropertyValueFactory<>("userId"));
        NameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        EmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        UserNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        CodeColumn.setCellValueFactory(new PropertyValueFactory<>("membershipCode"));

        ClientHandler.getClient().getAllUsers();

        showHistoryButton.setDisable(true);
        SubscribersTable.getSelectionModel().selectedItemProperty().addListener((_, _, newSel) -> {
            showHistoryButton.setDisable(newSel == null);
        });
    }

    /**
     * Populates the TableView with a list of users.
     * @param users the list of users to display
     */
    public void showUsers(List<? extends User> users) {
        SubscribersTable.getItems().setAll(users);
    }

    /**
     * Navigates back to the Supervisor UI.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SupervisorUI.fxml");
    }

    /**
     * Handles showing the selected subscriber's history.
     * Sets the selected user ID in SubscribersHistortOrdersController
     * and navigates to the SubscribersHistoryOrdersUI scene.
     */
    @FXML
    private void onShowHistory() {
        User selected = SubscribersTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        SubscribersHistortOrdersController.setSelectedUserId(selected.getUserId());
        SceneManager.switchTo("SubscribersHistoryOrdersUI.fxml");
    }

    /**
     * Overrides the BaseDisplayController method to display reservations.
     * Empty implementation because this controller does not display reservations.
     * @param list the list of reservations (ignored)
     */
    @Override
    public void showReservations(@SuppressWarnings("rawtypes") List list) {}
}
