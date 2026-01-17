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

/**
 * Command implementation for cancelling an existing reservation.
 * This class handles multiple cancellation flows, including authenticated users 
 * (Subscribers, Managers, Supervisors) and guest users identified by confirmation codes.
 * It also includes logic to detect if a reservation was already cancelled by the system.
 */
public class CancelReservationCommand implements Command {

    /**
     * Controller responsible for reservation management and status updates.
     */
    private final ReservationController controller = new ReservationController();

    /**
     * Executes the reservation cancellation logic.
     * The method determines the user type from the session or request data and performs 
     * the cancellation accordingly. It provides specific feedback if a reservation 
     * has already been processed as cancelled by the system.
     *
     * @param data   the CancelReservationRequest containing IDs or confirmation codes
     * @param client the connection to the client that issued the cancellation request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            CancelReservationRequest req = (CancelReservationRequest) data;

            // Retrieve the current user object from the client session info
            User currentUser = (User) client.getInfo("user");
            
            boolean success = false;
            Object responseData = null;
            String responseMessage = "";

            // Check if the user is a logged-in Subscriber or Staff member
            if (currentUser != null && 
               (currentUser.getRole() == UserRole.SUBSCRIBER || 
                currentUser.getRole() == UserRole.SUPERVISOR ||
                currentUser.getRole() == UserRole.MANAGER)) {
                
                // --- LOGGED IN USER FLOW ---
                success = controller.cancelReservation(currentUser, req.getReservationId(), null, null);
                responseMessage = success ? "Reservation cancelled successfully" : "Failed to cancel reservation";
                
            } else if (req.getGuestId() != null) {
                
                // --- GUEST USER FLOW (Using Confirmation Code) ---
                Integer code = req.getConfirmationCode();
                
                // Retrieve current status to check for prior system cancellation
                Reservation existingRes = controller.getReservationByCode(code); 
                
                // Check if the reservation was already system-cancelled but the user wasn't notified
                if (existingRes != null && 
                    existingRes.getStatus() == ReservationStatus.CANCELLED && 
                    !existingRes.isNotified()) {
                    
                    // Do not attempt double cancellation; return existing state to trigger client-side notification
                    success = false; 
                    responseData = existingRes; 
                    responseMessage = "This reservation was already cancelled by the system.";
                    
                } else {
                    // Standard guest cancellation logic
                    success = controller.cancelReservation(null, null, code, req.getGuestId());
                    responseMessage = success ? "Reservation cancelled successfully" : "Failed to cancel reservation";
                }
                
            } else {
                // Request does not meet logged-in or guest criteria
                success = false;
                responseMessage = "Invalid Request";
            }

            // Send the response message back to the client
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