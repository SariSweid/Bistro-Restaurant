package Controllers;

import java.util.List;
import Entities.Reservation;
import Entities.User;
import enums.ReservationStatus;
import enums.UserRole;
import handlers.ClientHandler;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import util.SceneManager;

/**
 * Controller for displaying the historical orders of subscribers.
 * Shows a table of reservations filtered by COMPLETED, CANCELLED, or NOT_SHOWED statuses.
 * Handles highlighting reservation statuses with different colors and fetching data from the server.
 */
public class SubscribersHistortOrdersController extends BaseDisplayController {

    /**
     * The user ID for which to show reservations if the current user is not a subscriber.
     */
    private static int selectedUserId = 0;

    /**
     * Sets the selected user ID for displaying historical reservations.
     * @param id the user ID to display
     */
    public static void setSelectedUserId(int id) { selectedUserId = id; }

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, Integer> idColumn;
    @FXML private TableColumn<Reservation, String> dateColumn;
    @FXML private TableColumn<Reservation, Integer> guestsColumn;
    @FXML private TableColumn<Reservation, Integer> confirmationCodeColumn;
    @FXML private TableColumn<Reservation, String> statusColumn;

    /**
     * Initializes the controller.
     * Sets up table columns, cell formatting, color coding for reservation statuses,
     * registers this controller as active, and fetches the reservations.
     */
    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        guestsColumn.setCellValueFactory(new PropertyValueFactory<>("numOfGuests"));
        confirmationCodeColumn.setCellValueFactory(new PropertyValueFactory<>("confirmationCode"));
        statusColumn.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getStatus().toString()));

        // Color-code status cells
        statusColumn.setCellFactory(_ -> new TableCell<Reservation, String>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) { 
                    setText(null); 
                    setStyle(""); 
                } else {
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

    /**
     * Updates the table with a list of reservations.
     * Filters reservations to only include COMPLETED, CANCELLED, or NOT_SHOWED statuses.
     * 
     * @param list the list of reservations received from the server
     */
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

    /**
     * Refreshes reservations in the table.
     * If the current user is a subscriber, fetches their reservations.
     * If the current user is higher role and selectedUserId is set, fetches that user's reservations.
     */
    public void refreshReservations() {
        User currentUser = ClientHandler.getClient().getCurrentUser();
        if (currentUser.getRole() == UserRole.SUBSCRIBER) {
            ClientHandler.getClient().getUserReservations(currentUser.getUserId());
        } else if (selectedUserId != 0) {
            ClientHandler.getClient().getUserReservations(selectedUserId);
        }
    }

    /**
     * Navigates back to the previous page in the scene history.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchToPrevious();
    }
}
