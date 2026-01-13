package Controllers;

import java.time.YearMonth;
import java.util.stream.IntStream;

import Entities.ReportSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import util.SceneManager;

public class MonthYearSelectorController {

    @FXML private ComboBox<String> monthCombo;
    @FXML private ComboBox<Integer> yearCombo;

    @FXML
    public void initialize() {
        monthCombo.getItems().addAll(
            "January","February","March","April","May","June",
            "July","August","September","October","November","December"
        );

        int currentYear = YearMonth.now().getYear();
        IntStream.rangeClosed(currentYear-2, currentYear).forEach(yearCombo.getItems()::add);
    }

    @FXML
    private void onTimeReport() {
        if (!validateSelection()) return;

        int month = monthCombo.getSelectionModel().getSelectedIndex() + 1;
        int year = yearCombo.getValue();

        ReportSession.setMonth(month);
        ReportSession.setYear(year);

        SceneManager.switchTo("TimesReportUI.fxml"); 
    }

    @FXML
    private void onSubscribersReport() {
        if (!validateSelection()) return;

        int month = monthCombo.getSelectionModel().getSelectedIndex() + 1;
        int year = yearCombo.getValue();

        ReportSession.setMonth(month);
        ReportSession.setYear(year);

        SceneManager.switchTo("SubscribersReportUI.fxml"); 
    }
    
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("ManagerUI.fxml");
    }


    private boolean validateSelection() {
        if (monthCombo.getValue() == null || yearCombo.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select both month and year");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
