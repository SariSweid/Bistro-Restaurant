package commands;

import java.util.List;

import Entities.User;
import common.Message;
import logicControllers.UserController;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class GetAllUsersCommand implements Command {
	
	// Controller responsible for reservation business logic
    private UserController userController = new UserController();

    /**
	 * Command responsible for handling GET_ALL_USERS requests.
	 */
	@Override
	public void execute(Object data, ConnectionToClient client) {
		
		try {
            // Retrieve all users from business logic
            List<User> users = userController.getAllUsers();

            // Send the list back to the client
            client.sendToClient(new Message(enums.ActionType.GET_ALL_USERS, users));

        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.sendToClient("SERVER_ERROR");
            } catch (Exception ignored) {}
        }
	}

}
