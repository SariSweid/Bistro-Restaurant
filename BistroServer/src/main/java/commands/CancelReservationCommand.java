package commands;

import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.ReservationController;
import messages.CancelReservationRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class CancelReservationCommand implements Command {

    private final ReservationController controller = new ReservationController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            CancelReservationRequest req = (CancelReservationRequest) data;

            boolean success = controller.cancelReservation(req.getReservationId());

            client.sendToClient(
                new Message(
                    ActionType.CANCEL_RESERVATION,
                    new ServerResponse(
                        success,
                        null,
                        success ? "Reservation cancelled" : "Cancel failed"
                    )
                )
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
