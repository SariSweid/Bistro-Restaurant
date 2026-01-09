package commands;

import common.Message;
import common.ServerResponse;
import enums.ActionType;
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
	    if (!(data instanceof UpdateUserRequest req)) return;

	    boolean success = userController.updateUser(req.getUser());

	    try {
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
