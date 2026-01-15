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

public class SubscriberInformationController extends BaseDisplayController {

    @FXML private TableView<User> SubscribersTable;
    @FXML private TableColumn<User, Integer> IdColumn;
    @FXML private TableColumn<User, String> NameColumn;
    @FXML private TableColumn<User, String> PhoneColumn;
    @FXML private TableColumn<User, String> EmailColumn;
    @FXML private TableColumn<User, String> UserNameColumn;
    @FXML private TableColumn<User, String> CodeColumn;
    @FXML private Button showHistoryButton;

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
        SubscribersTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            showHistoryButton.setDisable(newSel == null);
        });
    }

    public void showUsers(List<? extends User> users) {
        SubscribersTable.getItems().setAll(users);
    }

    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SupervisorUI.fxml");
    }

    @FXML
    private void onShowHistory() {
        User selected = SubscribersTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        SubscribersHistortOrdersController.setSelectedUserId(selected.getUserId());
        SceneManager.switchTo("SubscribersHistoryOrdersUI.fxml");
    }

    @Override
    public void showReservations(List list) {}
}
