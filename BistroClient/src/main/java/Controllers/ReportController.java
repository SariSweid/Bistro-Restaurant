package Controllers;

import Entities.Report;
import enums.ReportType;
import handlers.ClientHandler;
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
        System.out.println("repo:" + reportType);
        if (reportType != null)
            ClientHandler.getClient().requestReport(reportType, this);
    }

    public void showLineChart(XYChart.Series<String, Number> series, String title) {
        resetView();
        reportTitle.setText(title);
        lineChart.getData().add(series);
        lineChart.setVisible(true);
    }

    public void showBarChart(XYChart.Series<String, Number> series, String title) {
        resetView();
        reportTitle.setText(title);
        barChart.getData().add(series);
        barChart.setVisible(true);
    }

    private void resetView() {
        lineChart.setVisible(false);
        barChart.setVisible(false);
        lineChart.getData().clear();
        barChart.getData().clear();
    }

    @FXML
    private void onPreviousPage() { 
        SceneManager.switchTo("ManagerUI.fxml"); 
    }

    public void showError(String msg) { }
    public void showInfo(String msg) {  }
}
