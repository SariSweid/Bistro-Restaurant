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
import java.util.List;

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

        weeklyHoursTable.getSelectionModel().selectedItemProperty().addListener((obs, oldRow, row) -> {
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
    public void removeDay() {
        Day selectedDay = dayComboBox.getValue();
        if (selectedDay == null) {
            SceneManager.showError("Select a day first");
            return;
        }

   
        WeeklyOpeningHours wh = weeklyHoursList.stream()
                .filter(w -> w.getDay() == selectedDay)
                .findFirst()
                .orElse(null);

        if (wh == null) {
            SceneManager.showError("Day not found in the table");
            return;
        }

        
        weeklyHoursList.remove(wh);

       
        try {
            ClientHandler.getClient().deleteRegularOpeningHours(wh);
            ClientHandler.getClient().getRestaurantSettings();
            
            openingTimeField.clear();
            closingTimeField.clear();
            dayComboBox.setValue(null);
        } catch (Exception e) {
            SceneManager.showError("Error removing day from database");
            e.printStackTrace();
        }

        
        openingTimeField.clear();
        closingTimeField.clear();
        dayComboBox.setValue(null);
    }

    
    

    @FXML
    public void updateOpeningHours() {
        Day selectedDay = dayComboBox.getValue();
        if (selectedDay == null) {
            SceneManager.showError("Select a day first");
            return;
        }

        WeeklyOpeningHours wh = weeklyHoursList.stream()
                .filter(w -> w.getDay() == selectedDay)
                .findFirst()
                .orElse(null);

        try {
            String openText = openingTimeField.getText().trim();
            String closeText = closingTimeField.getText().trim();
            LocalTime openingTime = openText.isEmpty() ? null : LocalTime.parse(openText);
            LocalTime closingTime = closeText.isEmpty() ? null : LocalTime.parse(closeText);

            if (wh == null) {
              
                if (openingTime == null || closingTime == null) {
                    SceneManager.showError("To create a new day, fill both opening and closing times!");
                    return;
                }

                wh = new WeeklyOpeningHours(openingTime, closingTime,selectedDay);
                weeklyHoursList.add(wh);
                ClientHandler.getClient().createRegularOpeningHours(wh); 
            } else {
               
                if (openingTime != null) {
                    wh.setOpeningTime(openingTime);
                    ClientHandler.getClient().updateRegularOpeningTime(wh);
                }
                if (closingTime != null) {
                    wh.setClosingTime(closingTime);
                    ClientHandler.getClient().updateRegularClosingTime(wh);
                }
            }

            weeklyHoursTable.refresh();
            openingTimeField.clear();
            closingTimeField.clear();
        } catch (Exception e) {
            SceneManager.showError("Invalid format! Use HH:mm (10:00)");
        }
    }


    @FXML
    public void updateClosingHours() {
        Day selectedDay = dayComboBox.getValue();
        if (selectedDay == null) {
            SceneManager.showError("Select a day first");
            return;
        }

        WeeklyOpeningHours wh = weeklyHoursList.stream()
                .filter(w -> w.getDay() == selectedDay)
                .findFirst()
                .orElse(null);

        try {
            String openText = openingTimeField.getText().trim();
            String closeText = closingTimeField.getText().trim();
            LocalTime closingTime = closeText.isEmpty() ? null : LocalTime.parse(closeText);

            if (wh == null) {
                
                if (openingTimeField.getText().trim().isEmpty() || closingTime == null) {
                    SceneManager.showError("To create a new day, fill both opening and closing times!");
                    return;
                }
                LocalTime openingTime = LocalTime.parse(openText);
                wh = new WeeklyOpeningHours(openingTime, closingTime, selectedDay);
                weeklyHoursList.add(wh);
                ClientHandler.getClient().createRegularOpeningHours(wh); 
            } else {
                
                if (closingTime != null) {
                    wh.setClosingTime(closingTime);
                    ClientHandler.getClient().updateRegularClosingTime(wh);
                }
            }

            weeklyHoursTable.refresh();
            openingTimeField.clear();
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

           
            ClientHandler.getClient().addSpecialDate(newDate);

            specialDatesTable.refresh();

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
            
            ClientHandler.getClient().getRestaurantSettings();
            specialDatesTable.refresh();
            
            SceneManager.showInfo("Special date updated successfully");
            ClientHandler.getClient().getRestaurantSettings();
        } catch (Exception e) {
            SceneManager.showError("Invalid input");
        }
    }
    
<<<<<<< Upstream, based on branch 'main' of https://github.com/yarin8294/Project
=======
    @FXML
    public void deleteSpecialDate() {
    	SpecialDates selected = specialDatesTable.getSelectionModel().getSelectedItem();
    	if(selected==null) {
    		SceneManager.showError("No special date selected");
    		return;
    	}
    	
    	specialDatesList.remove(selected);
    	specialDatesTable.refresh();
    	
    	ClientHandler.getClient().deleteSpecialDate(selected.getDate());
    	
    	SceneManager.showInfo("Special date deleted successfully");
    	
    	
    }
    
    
>>>>>>> 49ac2d1 fix + added delete special date button
    

    @FXML
    public void previousPage() {
        SceneManager.switchTo("SupervisorUI.fxml");
    }
}
