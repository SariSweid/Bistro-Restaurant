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

public class ReservationsController {

    @FXML
    private TableView<Reservation> ReservationsTable;
    
    @FXML private TableColumn<Reservation, Number> idColumn;
    @FXML private TableColumn<Reservation, String> dateColumn;
    @FXML private TableColumn<Reservation, String> dayColumn;
    @FXML private TableColumn<Reservation, String> TimeColumn;
    @FXML private TableColumn<Reservation, Number> guestsColumn;
    @FXML private TableColumn<Reservation, Number> confirmationCodeColumn;

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
            delay.setOnFinished(e -> loadReservations());
            delay.play();
        });

    }

    private void loadReservations() {
        List<Reservation> reservations = ClientHandler.getClient().getAllReservationsList();

        if (reservations == null) {
            System.out.println("Reservations not loaded yet.");
            return;
        }

        List<Reservation> active = reservations.stream()
            .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED
                      || r.getStatus() == ReservationStatus.SEATED)
            .toList();

        ReservationsTable.getItems().setAll(active);
    }
    
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SupervisorUI.fxml");
    }

}
