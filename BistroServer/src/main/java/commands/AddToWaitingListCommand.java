package commands;

import java.io.IOException;

import common.Message;
import common.ServerResponse;
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


        ServerResponse response = controller.addToWaitingList(
                req.getUserID(),
                req.getEmail(),
                req.getPhone(),
                req.getNumOfGuests(),
                req.getDate(),
                req.getTime()
        );

        try {
            client.sendToClient(new Message(ActionType.ADD_TO_WAITING_LIST, response));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

