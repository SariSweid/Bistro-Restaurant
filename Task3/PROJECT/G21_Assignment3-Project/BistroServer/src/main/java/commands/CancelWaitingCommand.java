package commands;

import java.io.IOException;

import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.WaitingListController;
import messages.CancelWaitingRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for removing a customer from the restaurant's waiting list.
 * This class processes cancellation requests by identifying the user either via a 
 * confirmation code or the session's active user ID.
 */
public class CancelWaitingCommand implements Command {

    /**
     * Controller responsible for managing waiting list entries and removal logic.
     */
    private WaitingListController controller = new WaitingListController();

    /**
     * Executes the cancellation of a waiting list entry.
     * It retrieves the user's session ID from the client metadata, combines it 
     * with the request data, and invokes the WaitingListController to perform 
     * the removal before notifying the client of the result.
     *
     * @param data   the CancelWaitingRequest containing the entry's confirmation code
     * @param client the connection to the client requesting the cancellation
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {

        CancelWaitingRequest req = (CancelWaitingRequest) data;

        // Retrieve the authenticated userID from the connection session if available
        Integer userId =
                client.getInfo("userID") != null
                    ? (Integer) client.getInfo("userID")
                    : null;

        // Process the cancellation through the logic controller
        ServerResponse response =
                controller.cancelWaiting(
                        req.getConfirmationCode(),
                        userId
                );

        try {
            // Send the result of the cancellation back to the client
            client.sendToClient(
                    new Message(ActionType.CANCEL_WAITING, response)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}