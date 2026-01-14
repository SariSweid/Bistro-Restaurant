package Controllers;

import Entities.Reservation;
import enums.ReservationStatus;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import handlers.ClientHandler;
import util.SceneManager;

import java.util.List;

public class CancelReservationController extends BaseDisplayController {

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, Integer> idCol, guestsCol;
    @FXML private TableColumn<Reservation, String> dateCol, timeCol;

    @FXML private Button cancelButton, previousButton;

    @FXML
    public void initialize() {
        ClientHandler.getClient().setActiveDisplayController(this);

        idCol.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("reservationTime"));
        guestsCol.setCellValueFactory(new PropertyValueFactory<>("numOfGuests"));

        refreshReservations();
    }

    @Override
    public void showReservations(List<Reservation> list) {
        if (list == null || list.isEmpty()) {
            reservationsTable.setItems(FXCollections.observableArrayList());
            return;
        }

        List<Reservation> confirmed = list.stream()
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
                .toList();

        reservationsTable.setItems(FXCollections.observableArrayList(confirmed));
    }

    public void refreshReservations() {
        int userId = ClientHandler.getClient().getCurrentUserId();
        if (userId != 0) {
            ClientHandler.getClient().getUserReservations(userId);
        }
    }

    @FXML
    private void onCancelSelected() {
        Reservation selected = reservationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            SceneManager.showError("Please select a reservation to cancel.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to cancel reservation #" + selected.getReservationID() + "?",
                ButtonType.YES, ButtonType.NO);
        confirm.showAndWait();

        if (confirm.getResult() == ButtonType.YES) {
            ClientHandler.getClient().cancelReservation(selected.getReservationID(), null, null);
        }
    }

    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberUI.fxml");
        ClientHandler.getClient().getAllReservations();
    }
}
