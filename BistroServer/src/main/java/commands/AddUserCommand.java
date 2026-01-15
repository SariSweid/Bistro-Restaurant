package commands;

import Entities.User;
import logicControllers.UserController;
import messages.AddUserRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class AddUserCommand implements Command {
	
	// Controller responsible for reservation business logic
    private UserController userController = new UserController();
    
	/**
     * Command responsible for handling ADD_USER requests.
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
