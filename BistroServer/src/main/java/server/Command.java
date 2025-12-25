package server;

import src.ocsf.server.ConnectionToClient;

public interface Command {
    void execute(Object data, ConnectionToClient client);
}