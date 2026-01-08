package handlers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import Controllers.ReportController;
import Entities.Report;
import Entities.TimeData;
import Entities.WeekData;
import common.ServerResponse;
import enums.ReportType;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;

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

        if (report.getReportType() == ReportType.SCHEDULE) {

            List<String> slots = new ArrayList<>();
            int hour = 8;
            int min = 0;
            while (hour != 2 || min != 0) {
                slots.add(String.format("%02d:%02d", hour, min));
                min += 30;
                if (min == 60) { hour++; min = 0; }
                if (hour == 24) hour = 0;
            }
            slots.add("02:00");

            Map<String,Integer> arrivals = new LinkedHashMap<>();
            Map<String,Integer> departures = new LinkedHashMap<>();
            Map<String,Integer> lateArrivals = new LinkedHashMap<>();
            Map<String,Integer> delays = new LinkedHashMap<>();
            for (String s : slots) {
                arrivals.put(s, 0);
                departures.put(s, 0);
                lateArrivals.put(s, 0);
                delays.put(s, 0);
            }

            for (TimeData w : report.getTimeData()) {
                String arrivalSlot = mapToSlot(w.getArrivalTime());
                arrivals.put(arrivalSlot, arrivals.get(arrivalSlot) + 1);

                if (!isOnHalfHour(w.getArrivalTime())) {
                    lateArrivals.put(arrivalSlot, lateArrivals.get(arrivalSlot) + 1);
                    delays.put(arrivalSlot, delays.get(arrivalSlot) + 1);
                }

                if (w.getDepartureTime() != null) {
                    String departureSlot = mapToSlot(w.getDepartureTime());
                    departures.put(departureSlot, departures.get(departureSlot) + 1);
                }
            }

            XYChart.Series<String, Number> arrivalsSeries = new XYChart.Series<>();
            arrivalsSeries.setName("Arrivals");
            arrivals.forEach((k,v) -> arrivalsSeries.getData().add(new XYChart.Data<>(k,v)));

            XYChart.Series<String, Number> departuresSeries = new XYChart.Series<>();
            departuresSeries.setName("Departures");
            departures.forEach((k,v) -> departuresSeries.getData().add(new XYChart.Data<>(k,v)));

            XYChart.Series<String, Number> lateSeries = new XYChart.Series<>();
            lateSeries.setName("Late Arrivals");
            lateArrivals.forEach((k,v) -> lateSeries.getData().add(new XYChart.Data<>(k,v)));

            XYChart.Series<String, Number> delaysSeriesObj = new XYChart.Series<>();
            delaysSeriesObj.setName("Delays");
            delays.forEach((k,v) -> delaysSeriesObj.getData().add(new XYChart.Data<>(k,v)));

            Platform.runLater(() -> {

                controller.getArrivalsChart().getData().clear();
                controller.getDeparturesChart().getData().clear();
                controller.getLateArrivalsChart().getData().clear();
                controller.getDelaysChart().getData().clear();

                controller.getArrivalsChart().getData().add(arrivalsSeries);
                controller.getDeparturesChart().getData().add(departuresSeries);
                controller.getLateArrivalsChart().getData().add(lateSeries);
                controller.getDelaysChart().getData().add(delaysSeriesObj);

                controller.getArrivalsChart().applyCss(); controller.getArrivalsChart().layout();
                controller.getDeparturesChart().applyCss(); controller.getDeparturesChart().layout();
                controller.getLateArrivalsChart().applyCss(); controller.getLateArrivalsChart().layout();
                controller.getDelaysChart().applyCss(); controller.getDelaysChart().layout();

                controller.getArrivalsChart().lookupAll(".default-color0.chart-bar")
                        .forEach(n -> n.setStyle("-fx-bar-fill: green;"));
                controller.getDeparturesChart().lookupAll(".default-color0.chart-bar")
                        .forEach(n -> n.setStyle("-fx-bar-fill: red;"));
                controller.getLateArrivalsChart().lookupAll(".default-color0.chart-bar")
                        .forEach(n -> n.setStyle("-fx-bar-fill: orange;"));
                controller.getDelaysChart().lookupAll(".default-color0.chart-bar")
                        .forEach(n -> n.setStyle("-fx-bar-fill: purple;"));

                controller.reportTitle.setText("Daily Time Report");
            });

        } else if (report.getReportType() == ReportType.SUBSCRIBERS) {

            XYChart.Series<String, Number> completedSeries = new XYChart.Series<>();
            completedSeries.setName("Completed Reservations");

            XYChart.Series<String, Number> waitlistSeries = new XYChart.Series<>();
            waitlistSeries.setName("Waitlist");

            report.getWeekData().stream().limit(6).forEach((WeekData w) -> {
                completedSeries.getData().add(new XYChart.Data<>(w.getWeekName(), w.getCompleted()));
                waitlistSeries.getData().add(new XYChart.Data<>(w.getWeekName(), w.getWaitlist()));
            });

            Platform.runLater(() -> {
                controller.getBarChart().getData().clear();
                controller.getBarChart().getData().addAll(completedSeries, waitlistSeries);
                controller.getBarChart().setVisible(true);

                controller.getBarChart().applyCss();
                controller.getBarChart().layout();

                controller.getBarChart().lookupAll(".default-color0.chart-bar")
                        .forEach(n -> n.setStyle("-fx-bar-fill: green;"));
                controller.getBarChart().lookupAll(".default-color1.chart-bar")
                        .forEach(n -> n.setStyle("-fx-bar-fill: blue;"));

                controller.reportTitle.setText("Monthly Subscribers Report");
            });

        }
    }

    private String mapToSlot(LocalTime time) {
        int hour = time.getHour();
        int min = time.getMinute();
        if (min < 30) min = 0; else min = 30;
        return String.format("%02d:%02d", hour, min);
    }

    private boolean isOnHalfHour(LocalTime time) {
        int min = time.getMinute();
        return min == 0 || min == 30;
    }
}
