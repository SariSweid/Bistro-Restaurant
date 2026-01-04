package handlers;

import java.time.LocalTime;
import java.util.List;

import Controllers.BaseReservationController;
import Controllers.GuestMakeReservationController;
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

        // Get the active reservation controller (guest or subscriber)
        BaseReservationController controller = ClientHandler.getClient().getActiveReservationController();
;

        if (controller == null) {
            System.out.println("ERROR: No active reservation controller found");
            return;
        }
        
        
        System.out.println("Success = " + res.isSuccess());
        System.out.println("Message = " + res.getMessage());
        System.out.println("Data = " + res.getData());
        
        Platform.runLater(() -> controller.updateAvailableTimes(times));
    }
}
