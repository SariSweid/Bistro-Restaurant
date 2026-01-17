package Controllers;

import java.time.YearMonth;
import java.util.stream.IntStream;

import Entities.ReportSession;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import util.SceneManager;

/**
 * Controller for selecting a month and year for generating reports.
 * Handles initialization of ComboBoxes, validation of selections,
 * and navigation to specific report screens.
 */
public class MonthYearSelectorController {

    @FXML private ComboBox<String> monthCombo;
    @FXML private ComboBox<Integer> yearCombo;

    /**
     * Initializes the month and year ComboBoxes.
     * The month ComboBox contains all 12 months.
     * The year ComboBox contains the two previous years.
     */
    @FXML
    public void initialize() {
        monthCombo.getItems().addAll(
            "January","February","March","April","May","June",
            "July","August","September","October","November","December"
        );

        int currentYear = YearMonth.now().getYear();
        IntStream.rangeClosed(currentYear-2, currentYear-1).forEach(yearCombo.getItems()::add);
    }

    /**
     * Handles the action to generate a time report.
     * Validates the month and year selection, sets the report session values,
     * and navigates to the TimesReportUI scene.
     */
    @FXML
    private void onTimeReport() {
        if (!validateSelection()) return;

        int month = monthCombo.getSelectionModel().getSelectedIndex() + 1;
        int year = yearCombo.getValue();

        ReportSession.setMonth(month);
        ReportSession.setYear(year);

        SceneManager.switchTo("TimesReportUI.fxml"); 
    }

    /**
     * Handles the action to generate a subscribers report.
     * Validates the month and year selection, sets the report session values,
     * and navigates to the SubscribersReportUI scene.
     */
    @FXML
    private void onSubscribersReport() {
        if (!validateSelection()) return;

        int month = monthCombo.getSelectionModel().getSelectedIndex() + 1;
        int year = yearCombo.getValue();

        ReportSession.setMonth(month);
        ReportSession.setYear(year);

        SceneManager.switchTo("SubscribersReportUI.fxml"); 
    }

    /**
     * Navigates back to the previous page.
     * Switches the scene to the Manager UI.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("ManagerUI.fxml");
    }

    /**
     * Validates that both month and year are selected.
     * If either selection is missing, displays a warning alert.
     * @return true if both month and year are selected, false otherwise
     */
    private boolean validateSelection() {
        if (monthCombo.getValue() == null || yearCombo.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please select both month and year");
            alert.showAndWait();
            return false;
        }
        return true;
    }
}
