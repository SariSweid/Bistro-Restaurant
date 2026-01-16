package commands;

import Entities.Reservation;
import Entities.User;
import common.Message;
import common.ServerResponse;
import enums.ActionType;
import enums.ReservationStatus;
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
            Object responseData = null;
            String responseMessage = "";

            if (currentUser != null && 
               (currentUser.getRole() == UserRole.SUBSCRIBER || 
                currentUser.getRole() == UserRole.SUPERVISOR ||
                currentUser.getRole() == UserRole.MANAGER)) {
                
                // --- LOGGED IN USER ---
                success = controller.cancelReservation(currentUser, req.getReservationId(), null, null);
                responseMessage = success ? "Reservation cancelled successfully" : "Failed to cancel reservation";
                
            } else if (req.getGuestId() != null) {
                
                // --- GUEST USER (By Confirmation Code) ---
                Integer code = req.getConfirmationCode();
                
                // First, check the current status of this reservation
                Reservation existingRes = controller.getReservationByCode(code); // Ensure this method exists in your controller
                
                // TRIGGER: Is it already system-cancelled?
                if (existingRes != null && 
                    existingRes.getStatus() == ReservationStatus.CANCELLED && 
                    !existingRes.isNotified()) {
                    
                    // DO NOT cancel again. Return the object so the Client Handler triggers the popup.
                    success = false; // It's "false" because the user's action didn't cancel it (it was already done)
                    responseData = existingRes; 
                    responseMessage = "This reservation was already cancelled by the system.";
                    
                } else {
                    // Cancellation logic
                    success = controller.cancelReservation(null, null, code, req.getGuestId());
                    responseMessage = success ? "Reservation cancelled successfully" : "Failed to cancel reservation";
                }
                
            } else {
                success = false;
                responseMessage = "Invalid Request";
            }

            // Send the response
            client.sendToClient(
                new Message(
                    ActionType.CANCEL_RESERVATION,
                    new ServerResponse(
                        success,
                        responseData,
                        responseMessage
                    )
                )
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
