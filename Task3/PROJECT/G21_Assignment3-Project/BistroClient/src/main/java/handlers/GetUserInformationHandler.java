package handlers;

import Entities.User;

/**
 * Handles the response for a request that retrieves user information.
 * This handler is responsible for validating the received data and,
 * if it represents a valid User object, updating the ClientHandler
 * with the current logged-in user.
 */
public class GetUserInformationHandler implements ResponseHandler {

    /**
     * Reference to the client handler that manages client-side state.
     */
    private final ClientHandler client;

    /**
     * Constructs a new GetUserInformationHandler.
     *
     * @param client the ClientHandler instance that will store the current user
     */
    public GetUserInformationHandler(ClientHandler client) {
        this.client = client;
    }

    /**
     * Handles the server response.
     * If the received data is an instance of User, it is set as the current user
     * in the ClientHandler. Otherwise, an error message is printed.
     *
     * @param data the response object received from the server
     */
    @Override
    public void handle(Object data) {
        if (data instanceof User user) {
            client.setCurrentUser(user);
            System.out.println("Current user loaded: " + user.getUserId());
        } else {
            System.out.println("Failed to load user");
        }
    }
}
