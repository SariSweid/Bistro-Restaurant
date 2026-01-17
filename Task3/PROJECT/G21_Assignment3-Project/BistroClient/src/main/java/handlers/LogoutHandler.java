package handlers;

/**
 * Handles the logout process for the client.
 * When invoked, it clears the current user's session information
 * on the client side, effectively logging the user out.
 */
public class LogoutHandler implements ResponseHandler {

    /**
     * Handles the server response for a logout action.
     * This method clears the current user's ID and role in the
     * ClientHandler and prints a confirmation message.
     *
     * @param data the response object from the server (ignored in this handler)
     */
    @Override
    public void handle(Object data) {

        // Clear client-side session
        ClientHandler client = ClientHandler.getClient();
        client.setCurrentUserId(-1);
        client.setCurrentUserRole(null);
        System.out.println("Client logged out successfully.");
    }
}
