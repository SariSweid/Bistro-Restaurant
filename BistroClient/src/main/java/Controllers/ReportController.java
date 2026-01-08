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
    @FXML protected BarChart<String, Number> barChart;
    @FXML public Label reportTitle;
    @FXML private BarChart<String, Number> arrivalsChart;
    @FXML private BarChart<String, Number> departuresChart;
    @FXML private BarChart<String, Number> lateArrivalsChart;
    @FXML private BarChart<String, Number> delaysChart;

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
    }

    public BarChart<String, Number> getArrivalsChart() {
		return arrivalsChart;
	}

	public void setArrivalsChart(BarChart<String, Number> arrivalsChart) {
		this.arrivalsChart = arrivalsChart;
	}

	public BarChart<String, Number> getDeparturesChart() {
		return departuresChart;
	}

	public void setDeparturesChart(BarChart<String, Number> departuresChart) {
		this.departuresChart = departuresChart;
	}

	public BarChart<String, Number> getLateArrivalsChart() {
		return lateArrivalsChart;
	}

	public void setLateArrivalsChart(BarChart<String, Number> lateArrivalsChart) {
		this.lateArrivalsChart = lateArrivalsChart;
	}

	public BarChart<String, Number> getDelaysChart() {
		return delaysChart;
	}

	public void setDelaysChart(BarChart<String, Number> delaysChart) {
		this.delaysChart = delaysChart;
	}

	public void showBarChart(XYChart.Series<String, Number> series, String title) {
        resetView();
        reportTitle.setText(title);
        barChart.setVisible(true);
        Platform.runLater(() -> barChart.getData().add(series));
    }

    private void resetView() {
        barChart.setVisible(false);
        barChart.getData().clear();
    }

    public BarChart<String, Number> getBarChart() {
        return barChart;
    }


    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("ManagerUI.fxml");
    }

    public void showError(String msg) { }
    public void showInfo(String msg) {  }
}
