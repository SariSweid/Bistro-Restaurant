package commands;

import Entities.User;
import logicControllers.UserController;
import messages.GetUserInformationRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for retrieving detailed information about a specific user.
 * This class handles requests to fetch a user entity from the system, 
 * providing the necessary profile data back to the client or an error message if the user does not exist.
 */
public class GetUserInformationCommand implements Command {

    /**
     * Controller responsible for user-related business logic and database retrieval.
     */
    private UserController userController = new UserController();

    /**
     * Executes the user information retrieval logic.
     * Extracts the user ID from the GetUserInformationRequest, queries the 
     * UserController for the corresponding User object, and transmits either 
     * the object itself or a status string ("USER_NOT_FOUND" / "ERROR") back to the client.
     *
     * @param data   the GetUserInformationRequest containing the target user's ID
     * @param client the connection to the client that issued the request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        // Cast the data object to GetUserInformationRequest
        GetUserInformationRequest req = (GetUserInformationRequest) data;

        // Get the user information from the controller
        User user = userController.getUserInformation(req.getUserID());
        
        // Send the user object or error status back to the client
        try {
            if (user != null) {
                // Successful retrieval: send the user object itself
                client.sendToClient(user);
            } else {
                // User not found in the database
                client.sendToClient("USER_NOT_FOUND");
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                // Handle communication or internal errors
                client.sendToClient("ERROR");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}