package handlers;

import Entities.User;

public class GetUserInformationHandler implements ResponseHandler {

    private final ClientHandler client;

    public GetUserInformationHandler(ClientHandler client) {
        this.client = client;
    }

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
