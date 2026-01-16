package commands;

import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.UserController;
import messages.UpdateUserRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for updating user information in the system.
 * This class processes requests to modify existing user records by delegating 
 * the data persistence to the UserController and returning the result to the client.
 */
public class UpdateUserCommand implements Command {
	
	/**
     * Controller responsible for user management and profile updates.
     */
	private UserController userController = new UserController();

	/**
     * Executes the update user operation.
     * Validates the input data as an UpdateUserRequest, invokes the update logic 
     * in the controller, and transmits a ServerResponse indicating whether 
     * the profile was successfully modified.
     *
     * @param data   the UpdateUserRequest object containing the modified user entity
     * @param client the connection to the client that issued the request
     */
	@Override
	public void execute(Object data, ConnectionToClient client) {
	    if (!(data instanceof UpdateUserRequest req)) return;

	    // Attempt to update the user details via the business logic layer
	    boolean success = userController.updateUser(req.getUser());

	    try {
	        // Prepare and send the response back to the client
	        client.sendToClient(
	            new Message(
	                ActionType.UPDATE_USER,
	                new ServerResponse(
	                    success,
	                    null,
	                    success ? "User updated successfully" : "User update failed"
	                )
	            )
	        );
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

}