// LogoutCommand.java
package commands;

import common.ServerResponse;
import common.Message;
import enums.ActionType;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class LogoutCommand implements Command {
    @Override
    public void execute(Object data, ConnectionToClient client) {
        client.setInfo("user", null);
        System.out.println("User logged out.");

        try {
            
            Message response = new Message(ActionType.LOGOUT, new ServerResponse(true, "Logout successful", null));
            client.sendToClient(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
