package Controllers;

import java.time.format.DateTimeFormatter;
import java.util.List;

import Entities.Reservation;
import Entities.WaitingListEntry;
import enums.ActionType;
import handlers.ClientHandler;
import handlers.WeeklyWaitingListHandler;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import util.SceneManager;

/**
 * Controller for the Waiting List UI.
 * Displays the current week's waiting list in a table with columns for user info,
 * reservation details, and exit reasons.
 * Handles loading the waiting list from the server and navigation.
 */
public class WaitingListController extends BaseDisplayController {

    @FXML
    private TableColumn<WaitingListEntry, Integer> idColumn;
    @FXML
    private TableColumn<WaitingListEntry, String> emailColumn;
    @FXML
    private TableColumn<WaitingListEntry, String> phoneColumn;
    @FXML
    private TableColumn<WaitingListEntry, String> dateColumn;
    @FXML
    private TableColumn<WaitingListEntry, String> timeColumn;
    @FXML
    private TableColumn<WaitingListEntry, Integer> guestsColumn;
    @FXML
    private TableColumn<WaitingListEntry, Integer> confirmationCodeColumn;
    @FXML
    private TableColumn<WaitingListEntry, String> exitReasonColumn;
    @FXML
    private TableView<WaitingListEntry> waitingListTable;

    /**
     * Initializes the controller.
     * Sets up the table columns, formats dates and times, registers a handler for
     * weekly waiting list updates, and loads the current week's waiting list.
     */
    @FXML
    public void initialize() {
        ClientHandler.getClient().setActiveDisplayController(this);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        idColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getUserID()).asObject());
        emailColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEmail()));
        phoneColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPhone()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getWaitDate().format(dateFormatter)));
        timeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getWaitTime().format(timeFormatter)));
        guestsColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getNumOfGuests()).asObject());
        confirmationCodeColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getConfirmationCode()).asObject());
        exitReasonColumn.setCellValueFactory(data -> new SimpleStringProperty(
            data.getValue().getExitReason() == null ? "Active" : data.getValue().getExitReason().name())
        );

        // Register handler for server response
        ClientHandler.getClient().setHandler(ActionType.GET_WAITING_LIST_BETWEEN_DATES,
                new WeeklyWaitingListHandler(this));

        // Load current week's waiting list
        loadCurrentWeekWaitingList();
    }

    /**
     * Requests the current week's waiting list from the server.
     */
    private void loadCurrentWeekWaitingList() {
        ClientHandler.getClient().getCurrentWeekWaitingList();
    }

    /**
     * Returns the TableView containing waiting list entries.
     * 
     * @return the TableView of WaitingListEntry
     */
    public TableView<WaitingListEntry> getWaitingListTable() {
        return waitingListTable;
    }

    /**
     * Handles navigation back to the previous page (Supervisor UI).
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("SupervisorUI.fxml");
    }

    /**
     * Overrides the method from BaseDisplayController.
     * Currently not used for waiting list but required by superclass.
     * 
     * @param list the list of reservations (unused)
     */
    @Override
    public void showReservations(List<Reservation> list) {
        // No implementation needed for waiting list
    }
}
