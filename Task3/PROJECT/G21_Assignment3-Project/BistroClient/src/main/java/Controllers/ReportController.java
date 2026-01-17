package Controllers;

import enums.ReportType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import util.SceneManager;

/**
 * Abstract base controller for generating and displaying reports.
 * Handles BarChart visualization, chart updates, report title management,
 * and navigation back to the Manager UI.
 */
public abstract class ReportController {

    @FXML protected BarChart<String, Number> barChart;
    @FXML public Label reportTitle;
    @FXML private BarChart<String, Number> arrivalsChart;
    @FXML private BarChart<String, Number> departuresChart;
    @FXML private BarChart<String, Number> lateArrivalsChart;
    @FXML private BarChart<String, Number> delaysChart;

    /**
     * Returns the type of report this controller represents.
     * Must be implemented by subclasses.
     * @return the report type
     */
    protected abstract ReportType getReportType();

    /**
     * Initializes the controller.
     * Clears the report title.
     */
    @FXML
    public void initialize() {
        reportTitle.setText("");
    }

    /**
     * Displays a line chart with the given data series and title.
     * Resets the view before showing the chart.
     * @param series the data series to display
     * @param title the title of the chart
     */
    public void showLineChart(XYChart.Series<String, Number> series, String title) {
        resetView();
        reportTitle.setText(title);
    }

    /**
     * Displays a bar chart with the given data series and title.
     * Resets the view before showing the chart.
     * @param series the data series to display
     * @param title the title of the chart
     */
    public void showBarChart(XYChart.Series<String, Number> series, String title) {
        resetView();
        reportTitle.setText(title);
        barChart.setVisible(true);
        Platform.runLater(() -> barChart.getData().add(series));
    }

    /**
     * Resets the main bar chart view by hiding it and clearing its data.
     */
    private void resetView() {
        barChart.setVisible(false);
        barChart.getData().clear();
    }

    /**
     * Returns the arrivals chart.
     * @return the arrivals chart
     */
    public BarChart<String, Number> getArrivalsChart() {
        return arrivalsChart;
    }

    /**
     * Sets the arrivals chart.
     * @param arrivalsChart the chart to set
     */
    public void setArrivalsChart(BarChart<String, Number> arrivalsChart) {
        this.arrivalsChart = arrivalsChart;
    }

    /**
     * Returns the departures chart.
     * @return the departures chart
     */
    public BarChart<String, Number> getDeparturesChart() {
        return departuresChart;
    }

    /**
     * Sets the departures chart.
     * @param departuresChart the chart to set
     */
    public void setDeparturesChart(BarChart<String, Number> departuresChart) {
        this.departuresChart = departuresChart;
    }

    /**
     * Returns the late arrivals chart.
     * @return the late arrivals chart
     */
    public BarChart<String, Number> getLateArrivalsChart() {
        return lateArrivalsChart;
    }

    /**
     * Sets the late arrivals chart.
     * @param lateArrivalsChart the chart to set
     */
    public void setLateArrivalsChart(BarChart<String, Number> lateArrivalsChart) {
        this.lateArrivalsChart = lateArrivalsChart;
    }

    /**
     * Returns the delays chart.
     * @return the delays chart
     */
    public BarChart<String, Number> getDelaysChart() {
        return delaysChart;
    }

    /**
     * Sets the delays chart.
     * @param delaysChart the chart to set
     */
    public void setDelaysChart(BarChart<String, Number> delaysChart) {
        this.delaysChart = delaysChart;
    }

    /**
     * Returns the main bar chart.
     * @return the bar chart
     */
    public BarChart<String, Number> getBarChart() {
        return barChart;
    }

    /**
     * Navigates back to the Manager UI.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("ManagerUI.fxml");
    }

    /**
     * Displays an error message to the user.
     * Empty implementation; can be overridden by subclasses.
     * @param msg the error message
     */
    public void showError(String msg) { }

    /**
     * Displays an informational message to the user.
     * Empty implementation; can be overridden by subclasses.
     * @param msg the information message
     */
    public void showInfo(String msg) {  }
}
