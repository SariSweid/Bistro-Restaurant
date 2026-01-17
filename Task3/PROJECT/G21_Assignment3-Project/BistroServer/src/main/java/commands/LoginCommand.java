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

/**
 * Command implementation for handling user login requests.
 * This class validates user credentials, checks for existing active sessions to prevent 
 * dual logins, and stores session information (user details and ID) upon successful authentication.
 * It maintains a global map of active users to manage concurrency and state across the server.
 */
public class LoginCommand implements Command {

    /** Controller responsible for retrieving user records and information. */
    private final UserController userController;

    /** * A thread-safe map that tracks all currently logged-in users.
     * Key: userID (Integer), Value: ConnectionToClient instance.
     */
    private static final ConcurrentHashMap<Integer, ConnectionToClient> activeUsers = new ConcurrentHashMap<>();

    /**
     * Constructs a new LoginCommand and initializes the user controller.
     */
    public LoginCommand() {
        this.userController = new UserController();
    }

    /**
     * Executes the login authentication logic.
     * The process includes:
     * 1. Checking if the user is already logged in via activeUsers map.
     * 2. Fetching user data from the database.
     * 3. Validating the membership code based on the specific UserRole.
     * 4. Saving session data in the ConnectionToClient object.
     * 5. Adding the user to the global activeUsers list.
     *
     * @param data   the LoginRequest object containing userID and membershipCode
     * @param client the connection to the client attempting to log in
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            LoginRequest req = (LoginRequest) data;
            int userID = req.getUserID();
            int membershipCode = req.getMembershipCode();

            // Prevention of dual login: Check if userID is already in the map
            if (activeUsers.containsKey(userID)) {
                client.sendToClient(new Message(ActionType.LOGIN,
                        new ServerResponse(false, null, "User already logged in!")));
                System.out.println("DEBUG: Login attempt blocked for user " + userID);
                return;
            }

            // Fetch user information from database
            User user = userController.getUserInformation(userID);
            if (user == null) {
                client.sendToClient(new Message(ActionType.LOGIN,
                        new ServerResponse(false, null, "Invalid user ID")));
                System.out.println("DEBUG: Invalid userID " + userID);
                return;
            }

            ServerResponse response;
            // Validate credentials based on Role (Subscriber, Supervisor, Manager)
            switch (user.getRole()) {
                case SUBSCRIBER -> {
                    Subscriber sub = (Subscriber) user;
                    if (sub.getMembershipCode() != membershipCode) {
                        response = new ServerResponse(false, null, "Invalid membership code");
                        client.sendToClient(new Message(ActionType.LOGIN, response));
                        return;
                    }
                    response = new ServerResponse(true, sub, "Subscriber login successful");
                    updateSession(client, userID, sub);
                }
                case SUPERVISOR -> {
                    Supervisor sup = (Supervisor) user;
                    if (sup.getMembershipCode() != membershipCode) {
                        response = new ServerResponse(false, null, "Invalid membership code");
                        client.sendToClient(new Message(ActionType.LOGIN, response));
                        return;
                    }
                    response = new ServerResponse(true, sup, "Supervisor login successful");
                    updateSession(client, userID, sup);
                }
                case MANAGER -> {
                    Manager mgr = (Manager) user;
                    if (mgr.getMembershipCode() != membershipCode) {
                        response = new ServerResponse(false, null, "Invalid membership code");
                        client.sendToClient(new Message(ActionType.LOGIN, response));
                        return;
                    }
                    response = new ServerResponse(true, mgr, "Manager login successful");
                    updateSession(client, userID, mgr);
                }
                default -> {
                    response = new ServerResponse(false, null, "Unknown role");
                    client.sendToClient(new Message(ActionType.LOGIN, response));
                    return;
                }
            }
            
            client.sendToClient(new Message(ActionType.LOGIN, response));

        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.sendToClient(new Message(ActionType.LOGIN,
                        new ServerResponse(false, null, "Login ERROR")));
            } catch (Exception ignored) {}
        }
    }

    /**
     * Helper method to store session data and track the active user.
     * * @param client the client connection
     * @param userID the ID of the user
     * @param user   the user object to store in the session
     */
    private void updateSession(ConnectionToClient client, int userID, User user) {
        activeUsers.put(userID, client);
        client.setInfo("userID", userID);
        client.setInfo("user", user);
        System.out.println("DEBUG: " + user.getRole() + " " + userID + " logged in");
    }

    /**
     * Removes a user from the active users map, effectively logging them out.
     * This method is called by LogoutCommand or upon client disconnection.
     *
     * @param userID the ID of the user to be removed
     */
    public static void logoutUser(int userID) {
        activeUsers.remove(userID);
        System.out.println("DEBUG: User " + userID + " removed from activeUsers");
    }
}