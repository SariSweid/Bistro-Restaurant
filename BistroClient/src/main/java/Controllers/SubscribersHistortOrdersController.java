package Controllers;

import java.util.List;

import Entities.Reservation;
import enums.ReservationStatus;
import handlers.ClientHandler;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import util.SceneManager;

public class SubscribersHistortOrdersController extends BaseDisplayController {

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, Integer> idColumn;
    @FXML private TableColumn<Reservation, String> dateColumn;
    @FXML private TableColumn<Reservation, Integer> guestsColumn;
    @FXML private TableColumn<Reservation, Integer> confirmationCodeColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        guestsColumn.setCellValueFactory(new PropertyValueFactory<>("numOfGuests"));
        confirmationCodeColumn.setCellValueFactory(new PropertyValueFactory<>("confirmationCode"));

        ClientHandler.getClient().setActiveDisplayController(this);
        refreshReservations();
    }

    @Override
    public void showReservations(List<Reservation> list) {
    	System.out.println("the list is equal to:" + list);
    	System.out.println();
        if (list == null) {
            reservationsTable.setItems(FXCollections.observableArrayList());
            return;
        }

        List<Reservation> completed = list.stream()
                .filter(r -> r.getStatus() == ReservationStatus.COMPLETED)
                .toList();

        reservationsTable.setItems(FXCollections.observableArrayList(completed));
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
