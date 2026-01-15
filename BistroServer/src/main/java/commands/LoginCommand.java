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
import java.util.concurrent.ConcurrentHashMap;

public class LoginCommand implements Command {

    private final UserController userController;
    private static final ConcurrentHashMap<Integer, ConnectionToClient> activeUsers = new ConcurrentHashMap<>();

    public LoginCommand() {
        this.userController = new UserController();
    }

    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            LoginRequest req = (LoginRequest) data;
            int userID = req.getUserID();
            int membershipCode = req.getMembershipCode();

            if (activeUsers.containsKey(userID)) {
                client.sendToClient(new Message(ActionType.LOGIN,
                        new ServerResponse(false, null, "User already logged in!")));
                System.out.println("DEBUG: Login attempt blocked for user " + userID);
                return;
            }

            User user = userController.getUserInformation(userID);
            if (user == null) {
                client.sendToClient(new Message(ActionType.LOGIN,
                        new ServerResponse(false, null, "Invalid user ID")));
                System.out.println("DEBUG: Invalid userID " + userID);
                return;
            }

            ServerResponse response;
            switch (user.getRole()) {
                case SUBSCRIBER -> {
                    Subscriber sub = (Subscriber) user;
                    if (sub.getMembershipCode() != membershipCode) {
                        response = new ServerResponse(false, null, "Invalid membership code");
                        client.sendToClient(new Message(ActionType.LOGIN, response));
                        System.out.println("DEBUG: Invalid membership code for subscriber " + userID);
                        return;
                    }
                    response = new ServerResponse(true, sub, "Subscriber login successful");
                    client.setInfo("user", sub);
                    client.setInfo("userID", userID);
                    activeUsers.put(userID, client);
                    System.out.println("DEBUG: Subscriber " + userID + " logged in");
                }
                case SUPERVISOR -> {
                    Supervisor sup = (Supervisor) user;
                    if (sup.getMembershipCode() != membershipCode) {
                        response = new ServerResponse(false, null, "Invalid membership code");
                        client.sendToClient(new Message(ActionType.LOGIN, response));
                        System.out.println("DEBUG: Invalid membership code for supervisor " + userID);
                        return;
                    }
                    response = new ServerResponse(true, sup, "Supervisor login successful");
                    client.setInfo("user", sup);
                    client.setInfo("userID", userID);
                    activeUsers.put(userID, client);
                    System.out.println("DEBUG: Supervisor " + userID + " logged in");
                }
                case MANAGER -> {
                    Manager mgr = (Manager) user;
                    if (mgr.getMembershipCode() != membershipCode) {
                        response = new ServerResponse(false, null, "Invalid membership code");
                        client.sendToClient(new Message(ActionType.LOGIN, response));
                        System.out.println("DEBUG: Invalid membership code for manager " + userID);
                        return;
                    }
                    response = new ServerResponse(true, mgr, "Manager login successful");
                    client.setInfo("user", mgr);
                    client.setInfo("userID", userID);
                    activeUsers.put(userID, client);
                    System.out.println("DEBUG: Manager " + userID + " logged in");
                }
                default -> {
                    response = new ServerResponse(false, null, "Unknown role");
                    client.sendToClient(new Message(ActionType.LOGIN, response));
                    System.out.println("DEBUG: Unknown role for user " + userID);
                    return;
                }
            }
            
            activeUsers.put(userID, client);
            client.setInfo("userID", userID);
            client.setInfo("user", user);

            client.sendToClient(new Message(ActionType.LOGIN, response));

        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.sendToClient(new Message(ActionType.LOGIN,
                        new ServerResponse(false, null, "Login ERROR")));
            } catch (Exception ignored) {}
        }
    }

    public static void logoutUser(int userID) {
        activeUsers.remove(userID);
        System.out.println("DEBUG: User " + userID + " removed from activeUsers");
    }
}
