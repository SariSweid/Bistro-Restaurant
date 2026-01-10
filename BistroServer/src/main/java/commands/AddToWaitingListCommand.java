package commands;

import java.io.IOException;

import common.Message;
import enums.ActionType;
import logicControllers.WaitingListController;
import messages.AddToWaitingListRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class AddToWaitingListCommand implements Command {

    private WaitingListController controller = new WaitingListController();

    @Override
    public void execute(Object data, ConnectionToClient client) {

        AddToWaitingListRequest req = (AddToWaitingListRequest) data;

        int confirmationCode = controller.addToWaitingList(
                req.getUserID(),
                req.getEmail(),
                req.getPhone(),
                req.getNumOfGuests()
        );

        try {
			client.sendToClient(
			        new Message(ActionType.ADD_TO_WAITING_LIST, confirmationCode)
			);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
