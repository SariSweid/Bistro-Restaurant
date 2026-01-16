package Controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import Entities.Reservation;
import enums.ReservationStatus;
import handlers.ClientHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import util.SceneManager;
import javafx.beans.property.SimpleStringProperty;

/**
 * Controller for the Table Receiving UI for subscribers.
 * Handles displaying today's reservations, selecting a reservation,
 * entering confirmation codes, seating the customer, and handling forgotten codes.
 */
public class TableReceivingSubscriberController extends BaseDisplayController {

    /**
     * TableView displaying today's reservations for the subscriber.
     */
    @FXML private TableView<Reservation> reservationsTable;

    /**
     * TableColumn displaying the reservation time.
     */
    @FXML private TableColumn<Reservation, String> timeColumn;

    /**
     * TableColumn displaying the reservation confirmation code.
     */
    @FXML private TableColumn<Reservation, Integer> codeColumn;

    /**
     * TextField for the subscriber to enter their confirmation code manually.
     */
    @FXML private TextField confirmationCode;

    /**
     * Initializes the controller.
     * Sets up table columns, registers the controller, requests today's reservations,
     * and sets click listener for selecting a reservation row.
     */
    @FXML
    public void initialize() {
        timeColumn.setCellValueFactory(cell -> {
            if (cell.getValue().getReservationTime() != null)
                return new SimpleStringProperty(cell.getValue().getReservationTime().toString());
            else
                return new SimpleStringProperty("-");
        });

        codeColumn.setCellValueFactory(new PropertyValueFactory<>("confirmationCode"));

        // Register controller as active
        ClientHandler.getClient().setActiveDisplayController(this);

        // Request reservations for current subscriber
        int userId = ClientHandler.getClient().getCurrentUserId();
        ClientHandler.getClient().getUserReservations(userId);

        // Handle row clicks
        reservationsTable.setOnMouseClicked(this::onRowClick);
    }

    /**
     * Handles clicks on a reservation row in the table.
     * Populates the confirmation code field with the selected reservation's code.
     * 
     * @param event the MouseEvent triggered by clicking a table row
     */
    private void onRowClick(MouseEvent event) {
        Reservation selected = reservationsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            confirmationCode.setText(String.valueOf(selected.getConfirmationCode()));
        }
    }

    /**
     * Handles entering a confirmation code to seat the customer.
     * Validates input and sends a request to the server to seat the customer.
     */
    @FXML
    private void onEnter() {
        String text = confirmationCode.getText();

        if (text == null || text.isEmpty()) {
            SceneManager.showError("Please enter your confirmation code.");
            return;
        }

        int userId = ClientHandler.getClient().getCurrentUserId();

        try {
            int code = Integer.parseInt(text);
            LocalTime actualArrival = LocalTime.now();

            ClientHandler.getClient().seatCustomer(userId, code, actualArrival);

        } catch (NumberFormatException e) {
            SceneManager.showError("Confirmation code must be a number.");
        }
    }

    /**
     * Navigates back to the Subscriber main UI.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberUI.fxml");
    }

    /**
     * Displays today's confirmed reservations in the table.
     * Filters reservations to only show those for today with a grace period
     * and status CONFIRMED.
     * This method is called by ClientHandler when reservation data arrives from the server.
     * 
     * @param list the list of reservations received from the server
     */
    public void showReservations(List<Reservation> list) {
        LocalTime now = LocalTime.now();
        int gracePeriod = 15; // minutes

        List<Reservation> todayConfirmedReservations = list.stream()
                .filter(r -> r.getReservationDate().equals(LocalDate.now()))
                .filter(r -> r.getReservationTime() != null &&
                             !r.getReservationTime().plusMinutes(gracePeriod).isBefore(now))
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
                .collect(Collectors.toList());

        Platform.runLater(() -> reservationsTable.getItems().setAll(todayConfirmedReservations));
    }

    /**
     * Handles the "forgot my code" action.
     * Sends a request to the server to retrieve or reset the confirmation code for the subscriber.
     */
    @FXML
    private void onForgot() {
        int userId = ClientHandler.getClient().getCurrentUserId();
        ClientHandler.getClient().forgotCode(userId);
    }
}
