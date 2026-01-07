package handlers;

import java.util.List;

import Controllers.BaseReservationController;
import common.ServerResponse;
import javafx.application.Platform;
import messages.AvailableDateTimes;

public class GetNearestAvailableTimesHandler implements ResponseHandler {

    @Override
    public void handle(Object data) {

        if (!(data instanceof ServerResponse res)) {
            System.out.println("Unexpected response for nearest times");
            return;
        }

        if (!res.isSuccess()) {
            System.out.println("Nearest times failed: " + res.getMessage());
            return;
        }

        List<AvailableDateTimes> options = (List<AvailableDateTimes>) res.getData();

        BaseReservationController controller = ClientHandler.getClient().getActiveReservationController();

        if (controller == null) return;

        Platform.runLater(() ->
            controller.showNearestAvailableTimes(options)
        );
    }
}

