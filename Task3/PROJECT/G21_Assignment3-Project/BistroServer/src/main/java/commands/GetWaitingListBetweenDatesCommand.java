package commands;

import java.io.IOException;
import java.util.ArrayList;
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
 * Only entries with a null status are returned to the client.
 */
public class GetWaitingListBetweenDatesCommand implements Command {

    private WaitingListController controller = new WaitingListController();

    /**
     * Executes the retrieval of waiting list entries for a given date range.
     * Filters the results to only include entries where the status is null.
     *
     * @param data   the GetWaitingListBetweenDatesRequest containing start and end dates
     * @param client the connection to the client requesting the data
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {

        GetWaitingListBetweenDatesRequest req = (GetWaitingListBetweenDatesRequest) data;

        List<WaitingListEntry> list = controller.getWaitingListBetweenDates(
                req.getStartDate(),
                req.getEndDate()
        );

        List<WaitingListEntry> filteredList = new ArrayList<>();
        for (WaitingListEntry entry : list) {
            if (entry.getStatus() == null) {
                filteredList.add(entry);
            }
        }

        try {
            client.sendToClient(
                    new Message(ActionType.GET_WAITING_LIST_BETWEEN_DATES, filteredList)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
