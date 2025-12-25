package commands;

import logicControllers.UserController;
import messages.UpdateUserRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class UpdateUserCommand implements Command {
	
	// Controller responsible for reservation business logic
	private UserController userController = new UserController();

	/**
     * Executes the update user operation.
     *
     * @param data   UpdateUserRequest object
     * @param client client that sent the request
     */
	@Override
	public void execute(Object data, ConnectionToClient client) {
		// Cast the incoming data
        UpdateUserRequest req = (UpdateUserRequest) data;
        
        // Update the user using the controller
        boolean success = userController.updateUser(req.getUser());
		
        // Send response back to client
        try {
            client.sendToClient(success ? "UPDATE_OK" : "UPDATE_FAIL");
        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.sendToClient("ERROR");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
	}
}
