package handlers;

import Controllers.ReportController;
import Entities.Report;
import enums.ReportType;
import common.ServerResponse;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;

public class ReportHandler implements ResponseHandler {

    private ReportController controller;

    public void setController(ReportController controller) {
        this.controller = controller;
    }

    public ReportHandler(ReportController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(Object data) {
        ServerResponse response = (ServerResponse) data;

        if (!response.isSuccess()) {
            controller.showError(response.getMessage());
            return;
        }

        Report report = (Report) response.getData();

        if (report.getReportType() == ReportType.SCHEDULE) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Reservations");

            report.getWeekData().forEach(w ->
                    series.getData().add(new XYChart.Data<>(w.getWeekName(), w.getReservations()))
            );

            Platform.runLater(() -> controller.showLineChart(series, "Monthly Time Report"));

        } else if (report.getReportType() == ReportType.SUBSCRIBERS) {
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.setName("Subscribers");

            report.getWeekData().forEach(w ->
                    series.getData().add(new XYChart.Data<>(w.getWeekName(), w.getSubscribers()))
            );

            Platform.runLater(() -> controller.showBarChart(series, "Monthly Subscribers Report"));
        }
    }
}
