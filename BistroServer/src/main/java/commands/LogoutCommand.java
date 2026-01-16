package commands;

import common.ServerResponse;
import common.Message;
import enums.ActionType;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for handling user logout requests.
 * This class ensures that a user's session is properly terminated by clearing 
 * session attributes from the client connection and updating the global login status.
 */
public class LogoutCommand implements Command {

    /**
     * Executes the logout logic for a connected client.
     * Retrieves the user ID associated with the client session, invokes the global 
     * logout process, and clears the session metadata (user object and ID). 
     * Finally, it sends a confirmation message back to the client.
     *
     * @param data   the data payload (not explicitly used in this command)
     * @param client the connection to the client requesting logout
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            // Retrieve the stored userID from the connection session
            Integer userID = (Integer) client.getInfo("userID");
            
            if (userID != null) {
                // Remove user from the global logged-in users tracking
                LoginCommand.logoutUser(userID);
                
                // Clear session attributes to prevent unauthorized access
                client.setInfo("user", null);
                client.setInfo("userID", null);
                
                System.out.println("DEBUG: User " + userID + " logged out");
            }

            // Construct and send the success response
            Message response = new Message(ActionType.LOGOUT,
                    new ServerResponse(true, "Logout successful", null));
            client.sendToClient(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}