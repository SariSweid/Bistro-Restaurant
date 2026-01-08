package Controllers;

import Entities.Report;
import enums.ReportType;
import handlers.ClientHandler;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import util.SceneManager;

public abstract class ReportController {
    @FXML protected LineChart<String, Number> lineChart;
    @FXML protected BarChart<String, Number> barChart;
    @FXML protected Label reportTitle;

    protected abstract ReportType getReportType();

    @FXML
    public void initialize() {
        ReportType reportType = getReportType();
        if (reportType != null)
            ClientHandler.getClient().requestReport(reportType, this);
    }

    public void showLineChart(XYChart.Series<String, Number> series, String title) {
        resetView();
        reportTitle.setText(title);
        lineChart.setVisible(true);
        Platform.runLater(() -> lineChart.getData().add(series));
    }

    public void showBarChart(XYChart.Series<String, Number> series, String title) {
        resetView();
        reportTitle.setText(title);
        barChart.setVisible(true);
        Platform.runLater(() -> barChart.getData().add(series));
    }

    private void resetView() {
        lineChart.setVisible(false);
        lineChart.getData().clear();
        barChart.setVisible(false);
        barChart.getData().clear();
    }

    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("ManagerUI.fxml");
    }

    public void showError(String msg) { }
    public void showInfo(String msg) {  }
}
