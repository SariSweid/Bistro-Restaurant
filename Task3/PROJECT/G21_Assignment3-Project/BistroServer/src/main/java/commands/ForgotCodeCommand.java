package commands;

import logicControllers.ReservationController;
import messages.ForgotCodeRequest;
import common.ServerResponse;
import common.Message;
import enums.ActionType;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for handling "forgot reservation code" requests.
 * This class verifies if a user has an existing confirmed reservation to 
 * determine if a recovery action (like resending a code) can be performed.
 */
public class ForgotCodeCommand implements Command {

    /**
     * Controller responsible for managing reservation status and verification.
     */
    private ReservationController reservationController = new ReservationController();

    /**
     * Executes the logic to check for a user's confirmed reservation.
     * It extracts the user ID from the ForgotCodeRequest, queries the 
     * ReservationController, and sends a ServerResponse indicating 
     * whether the reservation was found.
     *
     * @param data   the ForgotCodeRequest object containing user identification
     * @param client the connection to the client requesting the code recovery
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        ForgotCodeRequest req = (ForgotCodeRequest) data;

        // Check the database/logic layer for a confirmed reservation associated with the user
        boolean hasReservation = reservationController.hasConfirmedReservation(req.getUserId());

        // Prepare the response based on the search result
        ServerResponse response = hasReservation
                ? new ServerResponse(true, null, "Success")
                : new ServerResponse(false, null, "Fail.");

        try {
            // Wrap the response in a Message object and send to client
            client.sendToClient(new Message(ActionType.FORGOT_CODE, response));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}