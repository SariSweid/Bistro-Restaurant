package Controllers;

import java.util.List;

import Entities.Reservation;
import enums.ReservationStatus;
import handlers.ClientHandler;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Duration;
import util.SceneManager;

/**
 * Controller for displaying all reservations in a table view.
 * Handles loading reservation data from the server and filtering for confirmed reservations.
 */
public class ReservationsController {

    /**
     * Table view for displaying reservations.
     */
    @FXML
    private TableView<Reservation> ReservationsTable;

    /**
     * Table column for reservation ID.
     */
    @FXML
    private TableColumn<Reservation, Number> idColumn;

    /**
     * Table column for reservation date.
     */
    @FXML
    private TableColumn<Reservation, String> dateColumn;

    /**
     * Table column for day of the week of the reservation.
     */
    @FXML
    private TableColumn<Reservation, String> dayColumn;

    /**
     * Table column for reservation time.
     */
    @FXML
    private TableColumn<Reservation, String> TimeColumn;

    /**
     * Table column for number of guests.
     */
    @FXML
    private TableColumn<Reservation, Number> guestsColumn;

    /**
     * Table column for the reservation confirmation code.
     */
    @FXML
    private TableColumn<Reservation, Number> confirmationCodeColumn;

    /**
     * Initializes the controller.
     * Sets up table columns, requests reservation data from the server, and loads it into the table.
     */
    @FXML
    public void initialize() {

        // --- SETUP TABLE COLUMNS ---
        idColumn.setCellValueFactory(cell ->
            new SimpleIntegerProperty(cell.getValue().getReservationID()));

        dateColumn.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getReservationDate().toString()));

        dayColumn.setCellValueFactory(cell ->
            new SimpleStringProperty(
                cell.getValue().getReservationDate().getDayOfWeek().toString()
            ));

        TimeColumn.setCellValueFactory(cell ->
            new SimpleStringProperty(cell.getValue().getReservationTime().toString()));

        guestsColumn.setCellValueFactory(cell ->
            new SimpleIntegerProperty(cell.getValue().getNumOfGuests()));

        confirmationCodeColumn.setCellValueFactory(cell ->
            new SimpleIntegerProperty(cell.getValue().getConfirmationCode()));

        // --- REQUEST DATA (RESERVATIONS) FROM SERVER ---
        ClientHandler.getClient().getAllReservations();

        // --- LOAD DATA AFTER HANDLER STORES IT ---
        Platform.runLater(() -> {
            PauseTransition delay = new PauseTransition(Duration.millis(5));
            delay.setOnFinished(_ -> loadReservations());
            delay.play();
        });
    }

    /**
     * Loads all reservations from the client handler and filters only confirmed reservations
     * to display them in the table.
     */
    private void loadReservations() {
        List<Reservation> reservations = ClientHandler.getClient().getAllReservationsList();

        if (reservations == null) {
            System.out.println("Reservations not loaded yet.");
            return;
        }

        List<Reservation> active = reservations.stream()
            .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
            .toList();

        ReservationsTable.getItems().setAll(active);
    }

    /**
     * Navigates back to the supervisor UI screen.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SupervisorUI.fxml");
    }
}
