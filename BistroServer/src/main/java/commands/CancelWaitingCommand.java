package commands;

import java.io.IOException;

import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.WaitingListController;
import messages.CancelWaitingRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class CancelWaitingCommand implements Command {

    private WaitingListController controller = new WaitingListController();

    @Override
    public void execute(Object data, ConnectionToClient client) {

        CancelWaitingRequest req = (CancelWaitingRequest) data;

        Integer userId =
                client.getInfo("userID") != null
                    ? (Integer) client.getInfo("userID")
                    : null;

        ServerResponse response =
                controller.cancelWaiting(
                        req.getConfirmationCode(),
                        userId
                );

        try {
            client.sendToClient(
                    new Message(ActionType.CANCEL_WAITING, response)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
