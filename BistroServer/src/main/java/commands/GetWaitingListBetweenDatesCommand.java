package commands;

import java.io.IOException;
import java.util.List;

import common.Message;
import enums.ActionType;
import logicControllers.WaitingListController;
import messages.GetWaitingListBetweenDatesRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;
import Entities.WaitingListEntry;

/**
 * Command implementation for retrieving entries from the waiting list within a specific date range.
 * This class interfaces with the WaitingListController to fetch data for users or managers 
 * who need to review past or future waiting list records.
 */
public class GetWaitingListBetweenDatesCommand implements Command {

    /**
     * Controller responsible for managing waiting list logic and database queries.
     */
    private WaitingListController controller = new WaitingListController();

    /**
     * Executes the retrieval logic for waiting list entries.
     * Extracts the start and end dates from the GetWaitingListBetweenDatesRequest,
     * queries the controller for matching records, and sends the resulting list 
     * back to the client.
     *
     * @param data   the GetWaitingListBetweenDatesRequest containing the date range
     * @param client the connection to the client that requested the data
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {

        GetWaitingListBetweenDatesRequest req =
                (GetWaitingListBetweenDatesRequest) data;

        // Retrieve the list of entries from the controller
        List<WaitingListEntry> list =
                controller.getWaitingListBetweenDates(
                        req.getStartDate(),
                        req.getEndDate()
                );

        try {
            // Wrap the list in a Message and send it to the client
            client.sendToClient(
                    new Message(ActionType.GET_WAITING_LIST_BETWEEN_DATES, list)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}