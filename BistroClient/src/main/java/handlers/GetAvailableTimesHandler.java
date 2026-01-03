package handlers;

import common.ServerResponse;
import javafx.application.Platform;

import java.time.LocalTime;
import java.util.List;

import Controllers.GuestMakeReservationController;

public class GetAvailableTimesHandler implements ResponseHandler {

    private final GuestMakeReservationController controller;

    public GetAvailableTimesHandler(GuestMakeReservationController controller) {
        this.controller = controller;
    }

    @Override
    public void handle(Object data) {
        ServerResponse res = (ServerResponse) data;

        if (!res.isSuccess()) {
        		System.out.println(res.getMessage());
            return;
        }

        List<LocalTime> times = (List<LocalTime>) res.getData();

        Platform.runLater(() -> controller.updateAvailableTimes(times));
    }
}
