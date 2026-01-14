package commands;

import logicControllers.ReservationController;
import messages.GetAvailableTimesRequest;
import server.Command;
import common.*;
import enums.ActionType;
import src.ocsf.server.ConnectionToClient;

import java.time.LocalTime;
import java.util.List;

public class GetAvailableTimesCommand implements Command {

    private final ReservationController controller = new ReservationController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            GetAvailableTimesRequest req = (GetAvailableTimesRequest) data;

            List<LocalTime> times;

            if (req.isForWaitingList()) {
                // Use the waiting list version
                times = controller.getAllTimesForWaitingList(req.getDate(), req.getGuests());
            } else {
                // Normal reservation
                times = controller.getAvailableTimes(req.getDate(), req.getGuests());
            }

            client.sendToClient(
                new Message(
                    ActionType.GET_AVAILABLE_TIMES,
                    new ServerResponse(true, times, "Available times")
                )
            );

        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.sendToClient(
                    new Message(
                        ActionType.GET_AVAILABLE_TIMES,
                        new ServerResponse(false, null, "Server error")
                    )
                );
            } catch (Exception ignored) {}
        }
    }

}
