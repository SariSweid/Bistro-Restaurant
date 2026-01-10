package commands;

import Entities.User;
import common.Message;
import common.ServerResponse;
import enums.ActionType;
import enums.UserRole;
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

            // Subscriber user object from session
            User currentUser = (User) client.getInfo("user");

            boolean success = false;

            if (currentUser != null && 
            		(currentUser.getRole() == UserRole.SUBSCRIBER || 
            		currentUser.getRole() == UserRole.SUPERVISOR ||
            		currentUser.getRole() == UserRole.MANAGER)) {
                success = controller.cancelReservation(currentUser, req.getReservationId(), null, null);
                
            } else if (req.getGuestId() != null) {
                // Guest cancels by confirmationCode + guestId
                success = controller.cancelReservation(null, null, req.getConfirmationCode(), req.getGuestId());
                
            } else success = false;

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

