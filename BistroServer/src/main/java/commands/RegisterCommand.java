package commands;

import Entities.*;
import logicControllers.UserController;
import messages.RegisterRequest;
import common.*;
import enums.ActionType;
import enums.UserRole;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class RegisterCommand implements Command {

    private final UserController userController;

    public RegisterCommand() {
        this.userController = new UserController();
    }

    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            if (!(data instanceof RegisterRequest req)) return;

            boolean success = false;
            User user = null;

            if (req.getRole() == UserRole.SUBSCRIBER) {
                user = new Subscriber(
                        req.getUserID(),
                        req.getName(),
                        req.getEmail(),
                        req.getPhone(),
                        req.getUsername(),
                        req.getMembershipCode()
                );
                success = userController.addUser(user);
            } else if (req.getRole() == UserRole.GUEST) {
                user = new Guest(
                        req.getUserID(),
                        req.getEmail(),
                        req.getPhone()
                );
                success = userController.addUser(user);
            }

            ServerResponse response = success ?
                    new ServerResponse(true, user, req.getRole() + " registration successful") :
                    new ServerResponse(false, null, "Registration failed");

            client.sendToClient(new Message(ActionType.ADD_USER, response));

        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.sendToClient(new Message(ActionType.ADD_USER,
                        new ServerResponse(false, null, "Registration ERROR")));
            } catch (Exception ignored) {}
        }
    }

}
