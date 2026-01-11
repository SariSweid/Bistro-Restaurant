package handlers;

import java.time.LocalTime;
import java.util.List;

import Controllers.BaseReservationController;
import Controllers.GuestWaitingListController;
import Controllers.SubscriberWaitingListController;
import common.ServerResponse;
import javafx.application.Platform;

public class GetAvailableTimesHandler implements ResponseHandler {

    @Override
    public void handle(Object data) {

        if (!(data instanceof ServerResponse res)) {
            System.out.println("Unexpected server response for GetAvailableTimes.");
            return;
        }

        if (!res.isSuccess()) {
            System.out.println(res.getMessage());
            return;
        }

        List<LocalTime> times = (List<LocalTime>) res.getData();

        // Try reservation controllers first
        BaseReservationController reservationController =
                ClientHandler.getClient().getActiveReservationController();

        if (reservationController != null) {
            Platform.runLater(() -> reservationController.updateAvailableTimes(times));
            return;
        }

        // Try waiting list controllers
        Object displayController = ClientHandler.getClient().getActiveDisplayController();

        if (displayController instanceof GuestWaitingListController guestCtrl) {
            Platform.runLater(() -> guestCtrl.loadTimes(times));
            return;
        }

        if (displayController instanceof SubscriberWaitingListController subCtrl) {
        	System.out.println(">>> USING NEW GetAvailableTimesHandler <<<");
            Platform.runLater(() -> subCtrl.loadTimes(times));
            return;
        }

        System.out.println("ERROR: No active controller found for available times");
    }
}

