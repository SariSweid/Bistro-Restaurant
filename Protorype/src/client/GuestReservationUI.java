package client;

import java.sql.Date;
import java.util.List;

import Entities.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class GuestReservationUI {

    @FXML
    private TableView<Reservation> reservationsTable;

    @FXML
    private TableColumn<Reservation, Integer> idColumn;

    @FXML
    private TableColumn<Reservation, Date> dateColumn;

    @FXML
    private TableColumn<Reservation, Integer> guestsColumn;
    
    
    @FXML
    private TableColumn<Reservation, String> confirmationCodeColumn;

    @FXML
    private TableColumn<Reservation, Integer> subscriberIdColumn;

    @FXML
    private TableColumn<Reservation, Date> orderDateColumn;


    @FXML
    private TextField reservationIdField;

    @FXML
    private TextField dateField;   

    @FXML
    private TextField guestsField;
    
    @FXML
    private TextField confirmationCodeField;

    @FXML
    private TextField subscriberIdField;

    @FXML
    private TextField orderDateField;

    @FXML
    private Label statusLabel;

    // Backing list for the table
    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    //empty constructor
    public GuestReservationUI() {
    }

    
    @FXML
    private void initialize() {
        // table columns 
        if (idColumn != null) {
            idColumn.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        }
        if (dateColumn != null) {
            dateColumn.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        }
        if (guestsColumn != null) {
            guestsColumn.setCellValueFactory(new PropertyValueFactory<>("numOfGuests"));
        }
        
        if (confirmationCodeColumn != null) {
            confirmationCodeColumn.setCellValueFactory(new PropertyValueFactory<>("confirmationCode"));
        }
        if (subscriberIdColumn != null) {
            subscriberIdColumn.setCellValueFactory(new PropertyValueFactory<>("subscriberId"));
        }
        if (orderDateColumn != null) {
            orderDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        }
        
        

        if (reservationsTable != null) {
            reservationsTable.setItems(reservations);

            // When user selects a row, copy data to the edit fields
            reservationsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
                        if (newSel != null) {
                            reservationIdField.setText(String.valueOf(newSel.getReservationID()));
                            dateField.setText(String.valueOf(newSel.getReservationDate()));
                            guestsField.setText(String.valueOf(newSel.getNumOfGuests()));
                            confirmationCodeField.setText(String.valueOf(newSel.getConfirmationCode()));
                            subscriberIdField.setText(String.valueOf(newSel.getCustomerID()));
                            orderDateField.setText(String.valueOf(newSel.getrReservationPlacedDate()));
                        }
                    });
        }

        // Register this UI in the client if it exists
        ClientHandler client = ClientHandler.getClient();
        if (client != null) {
            client.setGuestUI(this);
        }
    }


    public void displayAllReservations(List<Reservation> list) {
        reservations.setAll(list);
        if (statusLabel != null) {
            statusLabel.setText("Loaded " + list.size() + " reservations.");
        }
    }

    public void showMessage(String msg) {
        if (statusLabel != null) {
            statusLabel.setText(msg);
        }
    }

    //Button handlers (onAction)

    @FXML
    private void onLoadReservations() {
        ClientHandler client = ClientHandler.getClient();
        if (client == null) {
            showMessage("Client is not connected to server.");
            return;
        }

        showMessage("Loading reservations...");
        client.getAllReservations();
    }

    @FXML
    private void onUpdateReservation() {
        ClientHandler client = ClientHandler.getClient();
        if (client == null) {
            showMessage("Client is not connected to server.");
            return;
        }

        try {
            int id = Integer.parseInt(reservationIdField.getText().trim()); //check for valid id
            Date date = Date.valueOf(dateField.getText().trim()); //check for valid date
            int guests = Integer.parseInt(guestsField.getText().trim()); //check for valid guests num

            client.updateReservation(id, date, guests);
            showMessage("Sending update to server...");
        } catch (IllegalArgumentException e) {
            showMessage("Invalid ID / date / guests.");
        }
    }
    
    
}
