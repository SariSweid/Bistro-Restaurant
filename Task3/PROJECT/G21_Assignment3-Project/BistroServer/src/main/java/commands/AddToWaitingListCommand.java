package commands;

import java.io.IOException;

import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.WaitingListController;
import messages.AddToWaitingListRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for adding a user to the restaurant's waiting list.
 * This class handles the extraction of waitlist parameters from the client's request
 * and interacts with the WaitingListController to register the entry.
 */
public class AddToWaitingListCommand implements Command {

    /**
     * Controller responsible for managing the waiting list records and logic.
     */
    private WaitingListController controller = new WaitingListController();

    /**
     * Executes the logic to add a customer to the waiting list.
     * Extracts user details, contact information, and desired reservation timing 
     * from the AddToWaitingListRequest and sends a ServerResponse 
     * back to the client upon completion.
     *
     * @param data   the AddToWaitingListRequest containing the user's waitlist details
     * @param client the connection instance to the client that issued the request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        // Cast the incoming data to the specific request type
        AddToWaitingListRequest req = (AddToWaitingListRequest) data;

        // Delegate the registration process to the waiting list controller
        ServerResponse response = controller.addToWaitingList(
                req.getUserID(),
                req.getEmail(),
                req.getPhone(),
                req.getNumOfGuests(),
                req.getDate(),
                req.getTime()
        );

        try {
            // Send the resulting status and data back to the client
            client.sendToClient(new Message(ActionType.ADD_TO_WAITING_LIST, response));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}