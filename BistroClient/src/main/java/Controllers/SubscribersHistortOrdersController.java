package Controllers;

import java.util.List;
import Entities.Reservation;
import Entities.User;
import enums.ReservationStatus;
import handlers.ClientHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import util.SceneManager;

public class SubscribersHistortOrdersController extends BaseDisplayController {

    private static int selectedUserId = 0;
    public static void setSelectedUserId(int id) { selectedUserId = id; }

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, Integer> idColumn;
    @FXML private TableColumn<Reservation, String> dateColumn;
    @FXML private TableColumn<Reservation, Integer> guestsColumn;
    @FXML private TableColumn<Reservation, Integer> confirmationCodeColumn;
    @FXML private TableColumn<Reservation, String> statusColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        guestsColumn.setCellValueFactory(new PropertyValueFactory<>("numOfGuests"));
        confirmationCodeColumn.setCellValueFactory(new PropertyValueFactory<>("confirmationCode"));
        statusColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus().toString()));
        statusColumn.setCellFactory(column -> new TableCell<Reservation, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) { setText(null); setStyle(""); }
                else {
                    setText(status);
                    switch (status) {
                        case "COMPLETED" -> setTextFill(Color.GREEN);
                        case "CANCELLED" -> setTextFill(Color.RED);
                        case "NOT_SHOWED" -> setTextFill(Color.ORANGE);
                        default -> setTextFill(Color.BLACK);
                    }
                }
            }
        });
        ClientHandler.getClient().setActiveDisplayController(this);
        refreshReservations();
    }

    @Override
    public void showReservations(List<Reservation> list) {
        if (list == null) {
            reservationsTable.setItems(FXCollections.observableArrayList());
            return;
        }
        List<Reservation> filtered = list.stream()
            .filter(r -> r.getStatus() == ReservationStatus.COMPLETED
                      || r.getStatus() == ReservationStatus.NOT_SHOWED
                      || r.getStatus() == ReservationStatus.CANCELLED)
            .toList();
        reservationsTable.setItems(FXCollections.observableArrayList(filtered));
    }

    public void refreshReservations() {
        if (selectedUserId != 0) {
            ClientHandler.getClient().getUserReservations(selectedUserId);
        }
    }

    @FXML
    private void onPreviousPage() {
        User currentUser = ClientHandler.getClient().getCurrentUser();
        switch (currentUser.getRole()) {
            case SUBSCRIBER -> SceneManager.switchTo("SubscriberUI.fxml");
            default -> SceneManager.switchTo("SubscribersInformationUI.fxml");
        }
    }
}
