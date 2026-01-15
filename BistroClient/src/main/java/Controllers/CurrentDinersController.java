package Controllers;

import java.util.List;
import java.util.stream.Collectors;

import Entities.Reservation;
import enums.ReservationStatus;
import handlers.ClientHandler;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import util.SceneManager;

public class CurrentDinersController extends BaseDisplayController {

    @FXML private TableView<Reservation> CurrentDinersTable;

    @FXML private TableColumn<Reservation, String> IdColumn;
    @FXML private TableColumn<Reservation, Integer> TableIdColumn;
    @FXML private TableColumn<Reservation, Integer> GuestsColumn;
    @FXML private TableColumn<Reservation, String> TimeColumn;

    @FXML
    public void initialize() {
        // Register controller
        ClientHandler.getClient().setActiveDisplayController(this);

        // Set up columns
        IdColumn.setCellValueFactory(cell -> 
        new SimpleStringProperty(String.valueOf(cell.getValue().getCustomerId()))
        		);
        TableIdColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("tableID"));
        GuestsColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("numOfGuests"));
        TimeColumn.setCellValueFactory(cell -> {
            if (cell.getValue().getReservationTime() != null)
                return new SimpleStringProperty(cell.getValue().getReservationTime().toString());
            else
                return new SimpleStringProperty("-");
        });

        // Load current diners
        ClientHandler.getClient().getAllReservations();
    }


    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SupervisorUI.fxml");
    }
    

    @Override
    public void showReservations(List<Reservation> list) {
        List<Reservation> seated = list.stream()
                .filter(r -> r.getStatus() == ReservationStatus.SEATED)
                .collect(Collectors.toList());

        Platform.runLater(() -> CurrentDinersTable.getItems().setAll(seated));
    }
}

