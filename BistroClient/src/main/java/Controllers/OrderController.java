package Controllers;

import java.util.List;

import Entities.Reservation;
import handlers.ClientHandler;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import util.SceneManager;

public class OrderController {

    

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, Integer> idColumn;
    @FXML private TableColumn<Reservation, String> dateColumn;
    @FXML private TableColumn<Reservation, Integer> guestsColumn;
    @FXML private TableColumn<Reservation, Integer> confirmationCodeColumn;

    @FXML
    public void initialize() {
        // Setup columns
        idColumn.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        guestsColumn.setCellValueFactory(new PropertyValueFactory<>("numOfGuests"));
        confirmationCodeColumn.setCellValueFactory(new PropertyValueFactory<>("confirmationCode"));

        ClientHandler.getClient().setOrderController(this);

        // Load current user reservations automatically
        int userId = ClientHandler.getClient().getCurrentUserId();
        if (userId != 0) {
            ClientHandler.getClient().getUserReservations(userId);
        }
    }

    // This will be called by handler
    public void showUserReservations(List<Reservation> list) {
        if (list == null || list.isEmpty()) {
            reservationsTable.setItems(FXCollections.observableArrayList());
            SceneManager.showInfo("You have no active reservations.");
        } else {
            reservationsTable.setItems(FXCollections.observableArrayList(list));
        }
    }
    
    public void refreshReservations() {
        int userId = ClientHandler.getClient().getCurrentUserId();
        if (userId != 0) {
            ClientHandler.getClient().getUserReservations(userId);
        }
    }

    
    
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberUI.fxml");
    }
}

