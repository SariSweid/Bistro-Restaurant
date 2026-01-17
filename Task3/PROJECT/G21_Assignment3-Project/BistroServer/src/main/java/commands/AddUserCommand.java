package commands;

import Entities.User;
import logicControllers.UserController;
import messages.AddUserRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for adding a new user to the system.
 * This class handles the extraction of user data from the client's request
 * and coordinates with the UserController to persist the new user record.
 */
public class AddUserCommand implements Command {
	
	/**
	 * Controller responsible for user-related business logic and database interactions.
	 */
    private UserController userController = new UserController();
    
	/**
	 * Executes the logic to add a new user.
	 * Casts the incoming data to an AddUserRequest, extracts the User entity,
	 * and attempts to register the user via the business logic layer. 
	 * Returns a status string to the client indicating success or failure.
	 *
	 * @param data   the AddUserRequest object containing the new user's information
	 * @param client the connection to the client that issued the request
	 */
	@Override
	public void execute(Object data, ConnectionToClient client) {
		// Cast the data object to AddUserRequest
        AddUserRequest req = (AddUserRequest) data;
        
        // Extract the User object
        User user = req.getUser();
        
		try {
			// Call business logic to add the user
			boolean success = userController.addUser(user);
			
			// Send response back to the client
			client.sendToClient(success ? "ADD_OK" : "ADD_FAIL");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}