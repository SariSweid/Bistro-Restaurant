package commands;

import java.util.List;

import common.Message;
import enums.ActionType;
import logicControllers.ReservationController;
import messages.AvailableDateTimes;
import messages.GetNearestAvailableTimesRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class GetNearestAvailableTimesCommand implements Command {

    private final ReservationController controller = new ReservationController();

    @Override
    public void execute(Object data, ConnectionToClient client) {

        try {
            GetNearestAvailableTimesRequest req =
                (GetNearestAvailableTimesRequest) data;

            List<AvailableDateTimes> result =
                controller.getNearestAvailableDates(
                    req.getDate(),
                    req.getGuests()
                );

            client.sendToClient(
                new Message(
                    ActionType.GET_NEAREST_TIMES,
                    result
                )
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
