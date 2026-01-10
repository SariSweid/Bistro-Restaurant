package commands;

import java.io.IOException;

import common.Message;
import enums.ActionType;
import logicControllers.WaitingListController;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class CancelWaitingCommand implements Command {

    private WaitingListController controller = new WaitingListController();

    @Override
    public void execute(Object data, ConnectionToClient client) {

        int confirmationCode = (int) data;

        boolean success = controller.cancelWaiting(confirmationCode);

        try {
			client.sendToClient(
			        new Message(ActionType.CANCEL_WAITING, success)
			);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
