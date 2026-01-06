package commands;

import Entities.User;
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

            // Grab User object from client session
            User currentUser = (User) client.getInfo("user");

            boolean success = controller.cancelReservation(
                    currentUser,
                    req.getReservationId(),
                    req.getConfirmationCode(),
                    req.getGuestId()
                );
            
            client.sendToClient(
                new Message(
                    ActionType.CANCEL_RESERVATION,
                    new ServerResponse(
                        success,
                        null,
                        success ? "Reservation cancelled successfully" : "Failed to cancel reservation"
                    )
                )
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
