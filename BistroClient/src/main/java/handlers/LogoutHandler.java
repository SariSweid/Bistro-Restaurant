package handlers;

public class LogoutHandler implements ResponseHandler {

    @Override
    public void handle(Object data) {

        // Clear client-side session
        ClientHandler.getClient().setCurrentUserId(-1);

        System.out.println("Client logged out successfully.");
    }
}
