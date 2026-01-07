package commands;

import server.Command;
import src.ocsf.server.ConnectionToClient;

public class LogoutCommand implements Command {
    @Override
    public void execute(Object data, ConnectionToClient client) {
        client.setInfo("user", null);
        System.out.println("User logged out.");
    }
}
