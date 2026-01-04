package handlers;

import java.util.List;

import Controllers.BaseReservationController;
import Controllers.GuestMakeReservationController;
import javafx.application.Platform;
import messages.AvailableDateTimes;

public class GetNearestAvailableTimesHandler
        implements ResponseHandler {

    @Override
    public void handle(Object data) {

    		// Get the active reservation controller (guest or subscriber)
        BaseReservationController controller = ClientHandler.getClient().getActiveReservationController();

        if (controller == null) return;

        List<AvailableDateTimes> options = (List<AvailableDateTimes>) data;

        Platform.runLater(() ->
            controller.showNearestAvailableTimes(options)
        );
    }
}
