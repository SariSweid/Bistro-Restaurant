package commands;

import Entities.*;
import logicControllers.UserController;
import messages.RegisterRequest;
import common.*;
import enums.ActionType;
import enums.UserRole;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for registering new users in the system.
 * This class handles the registration of different user types (Subscribers and Guests),
 * ensuring that business rules like membership code uniqueness are enforced before 
 * persisting the user data.
 */
public class RegisterCommand implements Command {

    /**
     * Controller responsible for user management and registration logic.
     */
    private final UserController userController;

    /**
     * Constructs a new RegisterCommand and initializes its dependencies.
     */
    public RegisterCommand() {
        this.userController = new UserController();
    }

    /**
     * Executes the registration logic.
     * Extracts the registration details from the RegisterRequest, determines the 
     * appropriate User subclass to instantiate based on the role, and attempts 
     * to add the user via the UserController. It returns a detailed ServerResponse 
     * indicating success or specific failure reasons (e.g., duplicate IDs or codes).
     *
     * @param data   the RegisterRequest containing user details and the target role
     * @param client the connection to the client that issued the registration request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            if (!(data instanceof RegisterRequest req)) return;

            boolean success = false;
            User user = null;

            // Logic for Subscriber registration
            if (req.getRole() == UserRole.SUBSCRIBER) {

                // Validate that the membership code is unique
                if (userController.getUserByMembershipCode(req.getMembershipCode()) != null) {
                    ServerResponse response = new ServerResponse(false, null,
                            "Registration failed, membership code already exists");
                    client.sendToClient(new Message(ActionType.ADD_USER, response));
                    return; 
                }

                user = new Subscriber(
                        req.getUserID(),
                        req.getName(),
                        req.getEmail(),
                        req.getPhone(),
                        req.getUsername(),
                        req.getMembershipCode()
                );
                success = userController.addUser(user);

            // Logic for Guest registration
            } else if (req.getRole() == UserRole.GUEST) {
                user = new Guest(
                        req.getUserID(),
                        req.getEmail(),
                        req.getPhone()
                );
                success = userController.addUser(user);
            }

            // Prepare response based on the registration outcome
            ServerResponse response = success ?
                    new ServerResponse(true, user, req.getRole() + " registration successful") :
                    new ServerResponse(false, null, "Registration failed, userId already exists");

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