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

public class TableReceivingSubscriberController extends BaseDisplayController {

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, String> timeColumn;
    @FXML private TableColumn<Reservation, Integer> codeColumn;
    @FXML private TextField confirmationCode;

    @FXML
    public void initialize() {
    	timeColumn.setCellValueFactory(cell -> {
    	    if (cell.getValue().getReservationTime() != null)
    	        return new SimpleStringProperty(cell.getValue().getReservationTime().toString());
    	    else
    	        return new SimpleStringProperty("-");
    	});
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("confirmationCode"));

        // Register controller
        ClientHandler.getClient().setActiveDisplayController(this);

        // Request reservations
        int userId = ClientHandler.getClient().getCurrentUserId();
        ClientHandler.getClient().getUserReservations(userId);

        reservationsTable.setOnMouseClicked(this::onRowClick);
    }
    private void onRowClick(MouseEvent event) {
        Reservation selected = reservationsTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            confirmationCode.setText(String.valueOf(selected.getConfirmationCode()));
        }
    }

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



    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SubscriberUI.fxml");
    }

    // This function will be called by ClientHandler when data arrives
    public void showReservations(List<Reservation> list) {
        LocalTime now = LocalTime.now();
        int gracePeriod = 15; 

        List<Reservation> todayConfirmedReservations = list.stream()
                .filter(r -> r.getOrderDate().equals(LocalDate.now()))             
                .filter(r -> r.getReservationTime() != null &&
                             !r.getReservationTime().plusMinutes(gracePeriod).isBefore(now)) 
                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)       
                .collect(Collectors.toList());

        Platform.runLater(() -> reservationsTable.getItems().setAll(todayConfirmedReservations));
    }
    
    /**
     * User clicks "forgot my code"
     */
    @FXML
    private void onForgot() {

        int userId = ClientHandler.getClient().getCurrentUserId();

        // Send request to server
        ClientHandler.getClient().forgotCode(userId);
    }


}
