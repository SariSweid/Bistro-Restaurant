package handlers;

public class LogoutHandler implements ResponseHandler {

    @Override
    public void handle(Object data) {

        // Clear client-side session
        ClientHandler client = ClientHandler.getClient();
        client.setCurrentUserId(-1);
        client.setCurrentUserRole(null);
        System.out.println("Client logged out successfully.");
    }
}
