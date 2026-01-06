package Controllers;

import Entities.Reservation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class CancelReservationController {

    @FXML
    private TableView<Reservation> reservationsTable;

    @FXML
    private TableColumn<Reservation, Integer> idCol, guestsCol;

    @FXML
    private TableColumn<Reservation, String> dateCol, timeCol;

    @FXML
    private Button cancelButton;
    
    @FXML
    private Button previousButton;
    
    private ObservableList<Reservation> reservations = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
    	
	    	ClientHandler client = ClientHandler.getClient();
	    	client.setActiveCancelController(this);
	        
        // Request this user's active reservations
       // ClientHandler.getClient().getUserReservations(currentUserId);

        // Setup table columns
        idCol.setCellValueFactory(new PropertyValueFactory<>("reservationID"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("reservationDate"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("reservationTime"));
        guestsCol.setCellValueFactory(new PropertyValueFactory<>("numOfGuests"));
        
        reservationsTable.setItems(reservations);

        if (client.getCurrentUserId() != 0) {
            refreshReservations();
        }
    }
    
    // Load reservations into table
    public void loadReservations(List<Reservation> list) {
    		if (list == null || list.isEmpty()) {
            reservations.clear();
            SceneManager.showInfo("You have no active reservations.");
        } else {
            reservations.setAll(list);
        }
    }
    
    // Refresh from server
    public void refreshReservations() {
        int currentUserId = ClientHandler.getClient().getCurrentUserId();
        if (currentUserId != 0) {
            ClientHandler.getClient().getUserReservations(currentUserId);
        }
    }

    // Called by GetUserReservationsHandler
    public void showUserReservations(List<Reservation> reservations) {
        reservationsTable.setItems(FXCollections.observableArrayList(reservations));
    }

    @FXML
    private void onCancelSelected() {
        Reservation selected = reservationsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            SceneManager.showError("Please select a reservation to cancel.");
            return;
        }

        // Confirm dialog
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
        SceneManager.switchTo("MainMenuUI.fxml"); // use correct main menu depending on user type
    }
}
