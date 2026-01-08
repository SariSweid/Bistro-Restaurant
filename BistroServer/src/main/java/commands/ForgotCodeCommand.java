package commands;

import logicControllers.ReservationController;
import messages.ForgotCodeRequest;
import common.ServerResponse;
import common.Message;
import enums.ActionType;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class ForgotCodeCommand implements Command {

    private ReservationController reservationController = new ReservationController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        ForgotCodeRequest req = (ForgotCodeRequest) data;

        boolean hasReservation = reservationController.hasConfirmedReservation(req.getUserId());

        ServerResponse response = hasReservation
                ? new ServerResponse(true, null, "Success")
                : new ServerResponse(false, null, "Fail.");

        try {
            // Wrap inside Message
            client.sendToClient(new Message(ActionType.FORGOT_CODE, response));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
