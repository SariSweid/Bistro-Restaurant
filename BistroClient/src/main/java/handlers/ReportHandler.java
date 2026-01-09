package handlers;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javafx.application.Platform;
import javafx.scene.chart.XYChart;
import Controllers.ReportController;
import Entities.Report;
import Entities.TimeData;
import Entities.WeekData;
import common.ServerResponse;
import enums.ReportType;

/**
 * Handles server responses for reports and updates JavaFX charts accordingly.
 */
public class ReportHandler implements ResponseHandler {

    private ReportController controller;

    /**
     * Sets the controller for the handler.
     * @param controller the ReportController instance
     */
    public void setController(ReportController controller) {
        this.controller = controller;
    }

    /**
     * Constructor.
     * @param controller the ReportController instance
     */
    public ReportHandler(ReportController controller) {
        this.controller = controller;
    }

    /**
     * Handles the server response, updates charts based on report type.
     * @param data the server response object
     */
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
                slots.add(String.format("%02d:%02d-%02d:%02d", hour, min, hour, (min + 30) % 60));
                min += 30;
                if (min == 60) { hour++; min = 0; }
                if (hour == 24) hour = 0;
            }
            slots.add("02:00-02:30");

            Map<String,Integer> arrivals = new LinkedHashMap<>();
            Map<String,Integer> departures = new LinkedHashMap<>();
            for (String s : slots) {
                arrivals.put(s, 0);
                departures.put(s, 0);
            }

            Map<String,Integer> lateArrivals = new LinkedHashMap<>();
            List<String> lateCategories = List.of("0-5","5-10","10-15");
            for (String cat : lateCategories) lateArrivals.put(cat, 0);

            Map<String,Integer> delays = new LinkedHashMap<>();
            List<String> delayCategories = List.of("0-10","10-20","20-30","30-40","40-50","50-60");
            for (String cat : delayCategories) delays.put(cat, 0);

            for (TimeData t : report.getTimeData()) {

                if (t.getActualArrivalTime() != null && t.getReservationTime() != null) {
                    long late = java.time.Duration.between(t.getReservationTime(), t.getActualArrivalTime()).toMinutes();
                    if (late > 0) {
                        if (late <= 5) lateArrivals.put("0-5", lateArrivals.get("0-5")+1);
                        else if (late <= 10) lateArrivals.put("5-10", lateArrivals.get("5-10")+1);
                        else if (late <= 15) lateArrivals.put("10-15", lateArrivals.get("10-15")+1);
                    } else {
                        String aSlot = mapToHalfHourSlot(t.getActualArrivalTime());
                        arrivals.put(aSlot, arrivals.getOrDefault(aSlot, 0) + 1);
                    }
                }

                if (t.getDepartureTime() != null && t.getReservationTime() != null) {
                    LocalTime expectedDeparture = t.getReservationTime().plusHours(2);
                    long delay = java.time.Duration.between(expectedDeparture, t.getDepartureTime()).toMinutes();
                    if (delay > 0) {
                        if (delay <= 10) delays.put("0-10", delays.get("0-10")+1);
                        else if (delay <= 20) delays.put("10-20", delays.get("10-20")+1);
                        else if (delay <= 30) delays.put("20-30", delays.get("20-30")+1);
                        else if (delay <= 40) delays.put("30-40", delays.get("30-40")+1);
                        else if (delay <= 50) delays.put("40-50", delays.get("40-50")+1);
                        else delays.put("50-60", delays.get("50-60")+1);
                    } else {
                        String dSlot = mapToHalfHourSlot(t.getDepartureTime());
                        departures.put(dSlot, departures.getOrDefault(dSlot, 0) + 1);
                    }
                }
            }

            XYChart.Series<String, Number> arrivalsSeries = new XYChart.Series<>();
            arrivalsSeries.setName("Arrivals");
            for (String s : slots) arrivalsSeries.getData().add(new XYChart.Data<>(s, arrivals.getOrDefault(s,0)));

            XYChart.Series<String, Number> departuresSeries = new XYChart.Series<>();
            departuresSeries.setName("Departures");
            for (String s : slots) departuresSeries.getData().add(new XYChart.Data<>(s, departures.getOrDefault(s,0)));

            XYChart.Series<String, Number> lateSeries = new XYChart.Series<>();
            lateSeries.setName("Late Arrivals");
            for (String cat : lateCategories) lateSeries.getData().add(new XYChart.Data<>(cat, lateArrivals.getOrDefault(cat,0)));

            XYChart.Series<String, Number> delaysSeriesObj = new XYChart.Series<>();
            delaysSeriesObj.setName("Delays");
            for (String cat : delayCategories) delaysSeriesObj.getData().add(new XYChart.Data<>(cat, delays.getOrDefault(cat,0)));

            Platform.runLater(() -> {
                controller.getArrivalsChart().getData().setAll(arrivalsSeries);
                controller.getDeparturesChart().getData().setAll(departuresSeries);
                controller.getLateArrivalsChart().getData().setAll(lateSeries);
                controller.getDelaysChart().getData().setAll(delaysSeriesObj);

                controller.getArrivalsChart().applyCss(); controller.getArrivalsChart().layout();
                controller.getDeparturesChart().applyCss(); controller.getDeparturesChart().layout();
                controller.getLateArrivalsChart().applyCss(); controller.getLateArrivalsChart().layout();
                controller.getDelaysChart().applyCss(); controller.getDelaysChart().layout();

                controller.getArrivalsChart().lookupAll(".default-color0.chart-bar").forEach(n -> n.setStyle("-fx-bar-fill: green;"));
                controller.getDeparturesChart().lookupAll(".default-color0.chart-bar").forEach(n -> n.setStyle("-fx-bar-fill: red;"));
                controller.getLateArrivalsChart().lookupAll(".default-color0.chart-bar").forEach(n -> n.setStyle("-fx-bar-fill: orange;"));
                controller.getDelaysChart().lookupAll(".default-color0.chart-bar").forEach(n -> n.setStyle("-fx-bar-fill: purple;"));

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
                controller.getBarChart().getData().setAll(completedSeries, waitlistSeries);
                controller.getBarChart().setVisible(true);
                controller.getBarChart().applyCss();
                controller.getBarChart().layout();
                controller.getBarChart().lookupAll(".default-color0.chart-bar").forEach(n -> n.setStyle("-fx-bar-fill: green;"));
                controller.getBarChart().lookupAll(".default-color1.chart-bar").forEach(n -> n.setStyle("-fx-bar-fill: blue;"));
                controller.reportTitle.setText("Monthly Subscribers Report");
            });
        }
    }

    /**
     * Maps a LocalTime to a half-hour slot string for charts.
     * @param time the LocalTime
     * @return the slot string in format "HH:mm-HH:mm"
     */
    private String mapToHalfHourSlot(LocalTime time) {
        int hour = time.getHour();
        int min = time.getMinute() < 30 ? 0 : 30;
        int endMin = min == 0 ? 30 : 0;
        int endHour = min == 0 ? hour : (hour + 1) % 24;
        return String.format("%02d:%02d-%02d:%02d", hour, min, endHour, endMin);
    }
}
