package handlers;

import Controllers.ReportController;
import Entities.Report;
import Entities.WeekData;
import enums.ReportType;
import common.ServerResponse;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.NumberAxis;

public class ReportHandler implements ResponseHandler {

    private ReportController controller;

    public ReportHandler(ReportController controller) {
        this.controller = controller;
    }
    
    public void setController(ReportController controller) {
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

//        if (report.getReportType() == ReportType.SCHEDULE) {
//            XYChart.Series<String, Number> series = new XYChart.Series<>();
//            series.setName("Reservations");
//
//            report.getWeekData().stream().limit(4).forEach(w ->
//                    series.getData().add(new XYChart.Data<>(w.getWeekName(), w.getReservations()))
//            );
//
//            Platform.runLater(() -> controller.showLineChart(series, "Monthly Time Report"));
//
        	if (report.getReportType() == ReportType.SUBSCRIBERS) {

            XYChart.Series<String, Number> completedSeries = new XYChart.Series<>();
            completedSeries.setName("Completed Reservations");

            XYChart.Series<String, Number> waitlistSeries = new XYChart.Series<>();
            waitlistSeries.setName("Waitlist");

            report.getWeekData().stream().limit(4).forEach((WeekData w) -> {
                completedSeries.getData().add(new XYChart.Data<>(w.getWeekName(), w.getCompleted()));
                waitlistSeries.getData().add(new XYChart.Data<>(w.getWeekName(), w.getWaitlist()));
            });

            Platform.runLater(() -> {
                controller.getBarChart().getData().clear();

                controller.getBarChart().getData().addAll(completedSeries, waitlistSeries);
                controller.getBarChart().setVisible(true);

                controller.getBarChart().applyCss();
                controller.getBarChart().layout();

                NumberAxis yAxis = (NumberAxis) controller.getBarChart().getYAxis();
                yAxis.setAutoRanging(true);
                yAxis.setTickUnit(1);
                yAxis.setMinorTickVisible(false);
                yAxis.setForceZeroInRange(true);

                
                controller.getBarChart().lookupAll(".default-color0.chart-bar")
                        .forEach(n -> n.setStyle("-fx-bar-fill: green;"));
                controller.getBarChart().lookupAll(".default-color1.chart-bar")
                        .forEach(n -> n.setStyle("-fx-bar-fill: blue;"));

                controller.reportTitle.setText("Monthly Subscribers Report");
            });

        	}
    }
}
       
   
