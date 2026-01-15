package commands;

import logicControllers.UserController;
import messages.LoginRequest;
import server.Command;
import common.*;
import enums.ActionType;
import Entities.Subscriber;
import Entities.Supervisor;
import Entities.User;
import Entities.Manager;
import src.ocsf.server.ConnectionToClient;

public class LoginCommand implements Command {

    private final UserController userController;

    public LoginCommand() {
        this.userController = new UserController();
    }

    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            LoginRequest req = (LoginRequest) data;
            int userID = req.getUserID();
            int membershipCode = req.getMembershipCode();

            User user = userController.getUserInformation(userID);

            if (user == null) {
                client.sendToClient(new Message(ActionType.LOGIN,
                        new ServerResponse(false, null, "Invalid user ID")));
                return;
            }

            // Role-based validation
            ServerResponse response;

            switch (user.getRole()) {
                case SUBSCRIBER -> {
                    Subscriber sub = (Subscriber) user;
                    if (sub.getMembershipCode() != membershipCode) {
                        response = new ServerResponse(false, null, "Invalid membership code");
                        client.sendToClient(new Message(ActionType.LOGIN, response));
                        return;
                    }
                    response = new ServerResponse(true, sub, "Subscriber login successful");
                    client.setInfo("user", sub);  // store in session
                    client.setInfo("userID", sub.getUserId());
                }
                case SUPERVISOR -> {
                		Supervisor sup = (Supervisor) user;
                		if (sup.getMembershipCode() != membershipCode) {
                            response = new ServerResponse(false, null, "Invalid membership code");
                            client.sendToClient(new Message(ActionType.LOGIN, response));
                            return;
                    }
                    response = new ServerResponse(true, sup, "Supervisor login successful");
                    client.setInfo("user", sup);  // store in session
                    client.setInfo("userID", sup.getUserId());
                }
                case MANAGER -> {
	                	Manager mgr = (Manager) user;
	            		if (mgr.getMembershipCode() != membershipCode) {
	                        response = new ServerResponse(false, null, "Invalid membership code");
	                        client.sendToClient(new Message(ActionType.LOGIN, response));
	                        return;
	                }
	                response = new ServerResponse(true, mgr, "Supervisor login successful");
	                client.setInfo("user", mgr);  // store in session
	                client.setInfo("userID", mgr.getUserId());
	            }
                default -> {
                    response = new ServerResponse(false, null, "Unknown role");
                    client.sendToClient(new Message(ActionType.LOGIN, response));
                    return;
                }
            }

            // Send one success response
            client.sendToClient(new Message(ActionType.LOGIN, response));

        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.sendToClient(new Message(ActionType.LOGIN,
                        new ServerResponse(false, null, "Login ERROR")));
            } catch (Exception ignored) {}
        }
    }
}

