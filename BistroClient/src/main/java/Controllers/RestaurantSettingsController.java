package Controllers;

import Entities.RestaurantSettings;
import Entities.SpecialDates;
import Entities.WeeklyOpeningHours;
import enums.Day;
import handlers.ClientHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
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

    @FXML private ComboBox<Day> dayComboBox;

    @FXML private TextField specialDateField;
    @FXML private TextField specialNoteField;
    @FXML private TextField specialOpenField;
    @FXML private TextField specialCloseField;

    private ObservableList<SpecialDates> specialDatesList = FXCollections.observableArrayList();
    private ObservableList<WeeklyOpeningHours> weeklyHoursList = FXCollections.observableArrayList();
    private boolean loadedOnce = false;

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

        dayComboBox.setItems(FXCollections.observableArrayList(Day.values()));

        weeklyHoursTable.getSelectionModel().selectedItemProperty().addListener((_, _, row) -> {
            if (row != null) {
                openingTimeField.setText(row.getOpeningTime().toString());
                closingTimeField.setText(row.getClosingTime().toString());
                dayComboBox.setValue(row.getDay());
            } else {
                openingTimeField.clear();
                closingTimeField.clear();
                dayComboBox.setValue(null);
            }
        });

        ClientHandler.getClient().setActiveRestaurantSettingsController(this);

        if (!loadedOnce) {
            ClientHandler.getClient().getRestaurantSettings();
            loadedOnce = true;
        }
    }

    public void loadRestaurantSettings(RestaurantSettings settings) {
        if (settings == null) return;
        specialDatesList.clear();
        weeklyHoursList.clear();

        if (settings.getSpecialDates() != null) specialDatesList.addAll(settings.getSpecialDates());
        if (settings.getWeeklyOpeningHours() != null) weeklyHoursList.addAll(settings.getWeeklyOpeningHours());

        specialDatesTable.refresh();
        weeklyHoursTable.refresh();
    }

    @FXML
    public void removeDay() {
        Day selectedDay = dayComboBox.getValue();
        if (selectedDay == null) return;

        WeeklyOpeningHours wh = weeklyHoursList.stream()
                .filter(w -> w.getDay() == selectedDay)
                .findFirst()
                .orElse(null);
        if (wh == null) return;

        weeklyHoursList.remove(wh);
        ClientHandler.getClient().deleteRegularOpeningHours(wh);

        openingTimeField.clear();
        closingTimeField.clear();
        dayComboBox.setValue(null);
    }

    @FXML
    private void addDay() {
        Day selectedDay = dayComboBox.getValue();
        if (selectedDay == null) return;

        boolean exists = weeklyHoursList.stream().anyMatch(w -> w.getDay() == selectedDay);
        if (exists) return;

        try {
            LocalTime openingTime = LocalTime.parse(openingTimeField.getText().trim());
            LocalTime closingTime = LocalTime.parse(closingTimeField.getText().trim());

            WeeklyOpeningHours wh = new WeeklyOpeningHours(openingTime, closingTime, selectedDay);
            weeklyHoursList.add(wh);
            weeklyHoursTable.refresh();
            ClientHandler.getClient().createRegularOpeningHours(wh);

            openingTimeField.clear();
            closingTimeField.clear();
            dayComboBox.setValue(null);
        } catch (Exception ignored) {}
    }

    @FXML
    public void updateOpeningHours() {
        Day selectedDay = dayComboBox.getValue();
        if (selectedDay == null) return;

        WeeklyOpeningHours wh = weeklyHoursList.stream()
                .filter(w -> w.getDay() == selectedDay)
                .findFirst()
                .orElse(null);
        if (wh == null) return;

        try {
            String openText = openingTimeField.getText().trim();
            String closeText = closingTimeField.getText().trim();

            if (!openText.isEmpty()) {
                LocalTime openingTime = LocalTime.parse(openText);
                wh.setOpeningTime(openingTime);
                ClientHandler.getClient().updateRegularOpeningTime(wh);
            }

            if (!closeText.isEmpty()) {
                LocalTime closingTime = LocalTime.parse(closeText);
                wh.setClosingTime(closingTime);
                ClientHandler.getClient().updateRegularClosingTime(wh);
            }

            weeklyHoursTable.refresh();
            openingTimeField.clear();
            closingTimeField.clear();
        } catch (Exception ignored) {}
    }

    @FXML
    public void updateClosingHours() {
        Day selectedDay = dayComboBox.getValue();
        if (selectedDay == null) return;

        WeeklyOpeningHours wh = weeklyHoursList.stream()
                .filter(w -> w.getDay() == selectedDay)
                .findFirst()
                .orElse(null);
        if (wh == null) return;

        try {
            String closeText = closingTimeField.getText().trim();
            if (!closeText.isEmpty()) {
                LocalTime closingTime = LocalTime.parse(closeText);
                wh.setClosingTime(closingTime);
                ClientHandler.getClient().updateRegularClosingTime(wh);
            }

            weeklyHoursTable.refresh();
            openingTimeField.clear();
            closingTimeField.clear();
        } catch (Exception ignored) {}
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
        } catch (Exception ignored) {}
    }

    @FXML
    public void updateSpecialDates() {
        SpecialDates selected = specialDatesTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

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
        } catch (Exception ignored) {}
    }

    @FXML
    public void deleteSpecialDate() {
        SpecialDates selected = specialDatesTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        specialDatesList.remove(selected);
        specialDatesTable.refresh();
        ClientHandler.getClient().deleteSpecialDate(selected.getDate());
    }

    @FXML
    public void previousPage() {
        SceneManager.switchTo("SupervisorUI.fxml");
    }
}
