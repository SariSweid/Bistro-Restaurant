package commands;

import common.ServerResponse;
import common.Message;
import enums.ActionType;
import server.Command;
import src.ocsf.server.ConnectionToClient;


public class LogoutCommand implements Command {

    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            Integer userID = (Integer) client.getInfo("userID");
            if (userID != null) {
                LoginCommand.logoutUser(userID);
                client.setInfo("user", null);
                client.setInfo("userID", null);
                System.out.println("DEBUG: User " + userID + " logged out");
            }

            Message response = new Message(ActionType.LOGOUT,
                    new ServerResponse(true, "Logout successful", null));
            client.sendToClient(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}