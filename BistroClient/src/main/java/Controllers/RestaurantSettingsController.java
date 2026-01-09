package Controllers;


import Entities.RestaurantSettings;
import Entities.SpecialDates;
import Entities.WeeklyOpeningHours;
import enums.Day;
import handlers.ClientHandler;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



import java.time.LocalDate;
import java.time.LocalTime;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import messages.UpdateSpecialDateRequest;
import util.SceneManager;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class RestaurantSettingsController {
	
	@FXML private TextField openingTimeField;
	@FXML private TextField closingTimeField;

	@FXML private TableView<SpecialDates> specialDatesTable;
	@FXML private TableColumn<SpecialDates, LocalDate> dateColumn;
	@FXML private TableColumn<SpecialDates, String> noteColumn;
	@FXML private TableColumn<SpecialDates, LocalTime> openColumn;
	@FXML private TableColumn<SpecialDates, LocalTime> closeColumn;
	
	@FXML private TableView<WeeklyOpeningHours> weeklyHoursTable;
	@FXML private TableColumn<WeeklyOpeningHours,Day> dayColumn;
	@FXML private TableColumn<WeeklyOpeningHours,LocalTime> openingTimeColumn;
	@FXML private TableColumn<WeeklyOpeningHours,LocalTime> closingTimeColumn;

	@FXML private TextField specialDateField;
	@FXML private TextField specialNoteField;
	@FXML private TextField specialOpenField;
	@FXML private TextField specialCloseField;

	
    private ObservableList<SpecialDates> specialDatesList = FXCollections.observableArrayList();
    private final ObservableList<WeeklyOpeningHours> weeklyHoursList = FXCollections.observableArrayList();
	
	@FXML
	public void initialize() {
		
		ClientHandler.getClient().setActiveRestaurantSettingsController(this);

		dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        noteColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        openColumn.setCellValueFactory(new PropertyValueFactory<>("openingTime"));
        closeColumn.setCellValueFactory(new PropertyValueFactory<>("closingTime"));
        specialDatesTable.setItems(specialDatesList);
        
        
        dayColumn.setCellValueFactory(new PropertyValueFactory<>("day"));
        openingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("openingTime"));
        closingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("closingTime"));

        
        for(enums.Day day : enums.Day.values()) {
        	weeklyHoursList.add(new WeeklyOpeningHours(LocalTime.parse("10:00"),LocalTime.parse("22:00"),day));
        }
        
        weeklyHoursTable.setItems(weeklyHoursList);
        
        //make table only show the selected day state.
        weeklyHoursTable.getSelectionModel().selectedItemProperty().addListener((obs, oldRow, row) -> {
            if (row == null) {
                openingTimeField.clear();
                closingTimeField.clear();
                return;
            }
            openingTimeField.setText(row.getOpeningTime().toString());
            closingTimeField.setText(row.getClosingTime().toString());
        });

        
        //ask server to send settings
        ClientHandler.getClient().getRestaurantSettings();
        
        
	}    

    

	@FXML
	public void updateOpeningHours() {
	    try {
	        WeeklyOpeningHours selected = weeklyHoursTable.getSelectionModel().getSelectedItem();
	        if (selected == null) {
	            SceneManager.showError("Select a day first");
	            return;
	        }

	        String text = openingTimeField.getText();
	        if (text == null || text.isBlank()) {
	            SceneManager.showError("Please enter opening time (HH:MM).");
	            return;
	        }

	        LocalTime openingTime = LocalTime.parse(openingTimeField.getText().trim());

	        selected.setOpeningTime(openingTime);
	        weeklyHoursTable.refresh();

	        ClientHandler.getClient().updateRegularOpeningTime(selected);
	        
	        openingTimeField.clear();


	    } catch (Exception e) {
	        SceneManager.showError("Invalid format ! Use HH:MM (10:00)");
	    }
	}


	@FXML
	public void updateClosingHours() {
	    try {
	        WeeklyOpeningHours selected = weeklyHoursTable.getSelectionModel().getSelectedItem();
	        if (selected == null) {
	            SceneManager.showError("Select a day first");
	            return;
	        }

	        String text = closingTimeField.getText();
	        if (text == null || text.isBlank()) {
	            SceneManager.showError("Please enter closing time (HH:MM)");
	            return;
	        }

	        LocalTime closingTime = LocalTime.parse(closingTimeField.getText().trim());

	        selected.setClosingTime(closingTime);
	        weeklyHoursTable.refresh();

	        ClientHandler.getClient().updateRegularClosingTime(selected);

	        closingTimeField.clear();

	    } catch (Exception e) {
	        SceneManager.showError("Invalid format ! Use HH:mm (23:00)");
	    }
	}


	@FXML
	public void addSpecialDates() {
	    try {
	        String dateText  = specialDateField.getText();
	        String noteText  = specialNoteField.getText();
	        String openText  = specialOpenField.getText();
	        String closeText = specialCloseField.getText();

	        if (dateText == null || dateText.isBlank() ||
	            noteText == null || noteText.isBlank() ||
	            openText == null || openText.isBlank() ||
	            closeText == null || closeText.isBlank()) {
	            SceneManager.showError("Please fill all fields");
	            return;
	        }

	        LocalDate date = LocalDate.parse(dateText.trim());      
	        LocalTime open = LocalTime.parse(openText.trim());      
	        LocalTime close = LocalTime.parse(closeText.trim());    

	        SpecialDates sDates = new SpecialDates(open, close, date, noteText.trim());

	        specialDatesList.add(sDates);
	        specialDatesTable.refresh();

	        ClientHandler.getClient().addSpecialDate(sDates);

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
    	try {
    		SpecialDates selectedSpecialDate = specialDatesTable.getSelectionModel().getSelectedItem();
    		if(selectedSpecialDate==null) {
    			SceneManager.showError("No special date was selected");
    			return;
    		}
    		
    		String dateText = specialDateField.getText();
            String noteText = specialNoteField.getText();
            String openText = specialOpenField.getText();
            String closeText = specialCloseField.getText();

            if (dateText == null || dateText.isBlank() || noteText == null || noteText.isBlank() || openText == null || openText.isBlank() ||closeText == null || closeText.isBlank()) {
                SceneManager.showError("Please fill all fields");
                return;
            }
            
            LocalDate newDate = LocalDate.parse(dateText.trim());     
            LocalTime newOpen = LocalTime.parse(openText.trim());     
            LocalTime newClose = LocalTime.parse(closeText.trim()); 
    		LocalDate oldDate = selectedSpecialDate.getDate();
    		
            //update selected date
            selectedSpecialDate.setDate(newDate);
            selectedSpecialDate.setDescription(noteText.trim());
            selectedSpecialDate.setOpeningTime(newOpen);
            selectedSpecialDate.setClosingTime(newClose);
            
            //database update
            UpdateSpecialDateRequest req =
                    new UpdateSpecialDateRequest(oldDate,noteText.trim(), newDate, newOpen, newClose);
            ClientHandler.getClient().updateSpecialDate(req);
            
            
            SceneManager.showInfo("Special date updates successfully");
    	}catch(Exception e) {
    		SceneManager.showError("Invalid input");
    	}
    }
    
    

    @FXML
    public void previousPage() {
    	SceneManager.switchTo("SupervisorUI.fxml");
    }
    
    public void loadRestaurantSettings(RestaurantSettings settings) {
    	if(settings==null)
    		return;
    	specialDatesList.setAll(settings.getSpecialDates());
    	specialDatesTable.refresh();
    	
    }//
}
