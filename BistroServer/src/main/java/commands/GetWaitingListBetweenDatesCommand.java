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

public class GetWaitingListBetweenDatesCommand implements Command {

    private WaitingListController controller = new WaitingListController();

    @Override
    public void execute(Object data, ConnectionToClient client) {

        GetWaitingListBetweenDatesRequest req =
                (GetWaitingListBetweenDatesRequest) data;

        List<WaitingListEntry> list =
                controller.getWaitingListBetweenDates(
                        req.getStartDate(),
                        req.getEndDate()
                );

        try {
			client.sendToClient(
			        new Message(ActionType.GET_WAITING_LIST_BETWEEN_DATES, list)
			);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
