package Controllers;

import Entities.RestaurantSettings;
import Entities.SpecialDates;
import Entities.WeeklyOpeningHours;
import enums.Day;
import handlers.ClientHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import messages.UpdateSpecialDateRequest;
import util.SceneManager;

import java.time.LocalDate;
import java.time.LocalTime;

public class RestaurantSettingsController {

    @FXML private TextField openingTimeField;
    @FXML private TextField closingTimeField;

    @FXML private TableView<SpecialDates> specialDatesTable;
    @FXML private TableColumn<SpecialDates, LocalDate> dateColumn;
    @FXML private TableColumn<SpecialDates, String> noteColumn;
    @FXML private TableColumn<SpecialDates, LocalTime> openColumn;
    @FXML private TableColumn<SpecialDates, LocalTime> closeColumn;

    @FXML private TableView<WeeklyOpeningHours> weeklyHoursTable;
    @FXML private TableColumn<WeeklyOpeningHours, Day> dayColumn;
    @FXML private TableColumn<WeeklyOpeningHours, LocalTime> openingTimeColumn;
    @FXML private TableColumn<WeeklyOpeningHours, LocalTime> closingTimeColumn;

    @FXML private TextField specialDateField;
    @FXML private TextField specialNoteField;
    @FXML private TextField specialOpenField;
    @FXML private TextField specialCloseField;

    private ObservableList<SpecialDates> specialDatesList = FXCollections.observableArrayList();
    private ObservableList<WeeklyOpeningHours> weeklyHoursList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        specialDatesTable.setItems(specialDatesList);
        weeklyHoursTable.setItems(weeklyHoursList);

        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        openColumn.setCellValueFactory(new PropertyValueFactory<>("openingTime"));
        closeColumn.setCellValueFactory(new PropertyValueFactory<>("closingTime"));

        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        openingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("openingTime"));
        closingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("closingTime"));

        weeklyHoursTable.getSelectionModel().selectedItemProperty().addListener((obs, oldRow, row) -> {
            if (row != null) {
                openingTimeField.setText(row.getOpeningTime().toString());
                closingTimeField.setText(row.getClosingTime().toString());
            } else {
                openingTimeField.clear();
                closingTimeField.clear();
            }
        });

        ClientHandler.getClient().setActiveRestaurantSettingsController(this);
        ClientHandler.getClient().getRestaurantSettings();
    }

    public void loadRestaurantSettings(RestaurantSettings settings) {
        if (settings == null) return;

        specialDatesList.clear();
        weeklyHoursList.clear();

        if (settings.getSpecialDates() != null && !settings.getSpecialDates().isEmpty()) {
            specialDatesList.addAll(settings.getSpecialDates());
        }

        if (settings.getWeeklyOpeningHours() != null && !settings.getWeeklyOpeningHours().isEmpty()) {
            weeklyHoursList.addAll(settings.getWeeklyOpeningHours());
        }

        specialDatesTable.refresh();
        weeklyHoursTable.refresh();
    }

    @FXML
    public void updateOpeningHours() {
        WeeklyOpeningHours selected = weeklyHoursTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            SceneManager.showError("Select a day first");
            return;
        }
        try {
            LocalTime openingTime = LocalTime.parse(openingTimeField.getText().trim());
            selected.setOpeningTime(openingTime);
            weeklyHoursTable.refresh();
            ClientHandler.getClient().updateRegularOpeningTime(selected);
            openingTimeField.clear();
        } catch (Exception e) {
            SceneManager.showError("Invalid format! Use HH:mm (10:00)");
        }
    }

    @FXML
    public void updateClosingHours() {
        WeeklyOpeningHours selected = weeklyHoursTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            SceneManager.showError("Select a day first");
            return;
        }
        try {
            LocalTime closingTime = LocalTime.parse(closingTimeField.getText().trim());
            selected.setClosingTime(closingTime);
            weeklyHoursTable.refresh();
            ClientHandler.getClient().updateRegularClosingTime(selected);
            closingTimeField.clear();
        } catch (Exception e) {
            SceneManager.showError("Invalid format! Use HH:mm (23:00)");
        }
    }

    @FXML
    public void addSpecialDates() {
        try {
            LocalDate date = LocalDate.parse(specialDateField.getText().trim());
            LocalTime open = LocalTime.parse(specialOpenField.getText().trim());
            LocalTime close = LocalTime.parse(specialCloseField.getText().trim());
            String note = specialNoteField.getText().trim();

            SpecialDates newDate = new SpecialDates(open, close, date, note);
            specialDatesList.add(newDate);
            specialDatesTable.refresh();
            ClientHandler.getClient().addSpecialDate(newDate);

            specialDateField.clear();
            specialOpenField.clear();
            specialCloseField.clear();
            specialNoteField.clear();
        } catch (Exception e) {
            SceneManager.showError("Invalid input");
        }
    }

    @FXML
    public void updateSpecialDates() {
        SpecialDates selected = specialDatesTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            SceneManager.showError("No special date selected");
            return;
        }
        try {
            LocalDate newDate = LocalDate.parse(specialDateField.getText().trim());
            LocalTime newOpen = LocalTime.parse(specialOpenField.getText().trim());
            LocalTime newClose = LocalTime.parse(specialCloseField.getText().trim());
            String newNote = specialNoteField.getText().trim();
            LocalDate oldDate = selected.getDate();

            selected.setDate(newDate);
            selected.setOpeningTime(newOpen);
            selected.setClosingTime(newClose);
            selected.setDescription(newNote);

            ClientHandler.getClient().updateSpecialDate(
                new UpdateSpecialDateRequest(oldDate, selected.getDescription(), newDate, newOpen, newClose)
            );

            specialDatesTable.refresh();
            SceneManager.showInfo("Special date updated successfully");
        } catch (Exception e) {
            SceneManager.showError("Invalid input");
        }
    }

    @FXML
    public void previousPage() {
        SceneManager.switchTo("SupervisorUI.fxml");
    }
}
