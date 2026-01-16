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

/**
 * Controller for displaying the order history of a user.
 * Extends {@link BaseDisplayController} to show confirmed reservations in a table.
 * Provides functionality to refresh reservations and navigate back to the subscriber UI.
 */
public class OrderController extends BaseDisplayController {

    /**
     * Table view for displaying reservations.
     */
    @FXML
    private TableView<Reservation> reservationsTable;

    /**
     * Table column for the reservation ID.
     */
    @FXML
    private TableColumn<Reservation, Integer> idColumn;

    /**
     * Table column for the reservation date.
     */
    @FXML
    private TableColumn<Reservation, String> dateColumn;

    /**
     * Table column for the number of guests in the reservation.
     */
    @FXML
    private TableColumn<Reservation, Integer> guestsColumn;

    /**
     * Table column for the reservation confirmation code.
     */
    @FXML
    private TableColumn<Reservation, Integer> confirmationCodeColumn;

    /**
     * Initializes the controller.
     * Sets up table columns and registers this controller as the active display controller.
     * Refreshes the reservations list on initialization.
     */
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        guestsColumn.setCellValueFactory(new PropertyValueFactory<>("numOfGuests"));
        confirmationCodeColumn.setCellValueFactory(new PropertyValueFactory<>("confirmationCode"));

        ClientHandler.getClient().setActiveDisplayController(this);
        refreshReservations();
    }

    /**
     * Displays a list of confirmed reservations in the table.
     *
     * @param list the list of {@link Reservation} objects to display
     */
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

    /**
     * Refreshes the reservations table by requesting the current user's reservations
     * from the client handler.
     */
    public void refreshReservations() {
        int userId = ClientHandler.getClient().getCurrentUserId();
        if (userId != 0) {
            ClientHandler.getClient().getUserReservations(userId);
        }
    }

    /**
     * Navigates back to the subscriber UI.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberUI.fxml");
    }
}
