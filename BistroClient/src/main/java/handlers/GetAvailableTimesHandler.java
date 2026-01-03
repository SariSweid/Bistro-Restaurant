package handlers;

import java.time.LocalTime;
import java.util.List;

import Controllers.GuestMakeReservationController;
import common.ServerResponse;
import javafx.application.Platform;

public class GetAvailableTimesHandler implements ResponseHandler {

    @Override
    public void handle(Object data) {
        ServerResponse res = (ServerResponse) data;

        if (!res.isSuccess()) {
            System.out.println(res.getMessage());
            return;
        }

        List<LocalTime> times = (List<LocalTime>) res.getData();

        GuestMakeReservationController controller =
                ClientHandler.getClient().getGuestMakeReservationController();

        if (controller == null) {
            System.out.println("ERROR: GuestMakeReservationController is null");
            return;
        }
        
        
        System.out.println("Success = " + res.isSuccess());
        System.out.println("Message = " + res.getMessage());
        System.out.println("Data = " + res.getData());
        Platform.runLater(() -> controller.updateAvailableTimes(times));
    }
}
