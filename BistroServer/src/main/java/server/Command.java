package server;

import src.ocsf.server.ConnectionToClient;

/**
 * Interface for the Command design pattern used by the server to process client requests.
 * Each implementation of this interface handles a specific action type defined by the 
 * communication protocol, allowing for a decoupled and scalable request-handling architecture.
 */
public interface Command {
    
    /**
     * Executes the specific logic associated with a client request.
     * * @param data The data payload sent by the client, which may be cast to specific 
     * message or entity types depending on the implementation.
     * @param client The connection object representing the client that initiated the request, 
     * used to send responses back to that specific client.
     */
    void execute(Object data, ConnectionToClient client);
}