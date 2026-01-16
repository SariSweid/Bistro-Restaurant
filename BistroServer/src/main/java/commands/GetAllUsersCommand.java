package commands;

import java.util.List;

import Entities.User;
import common.Message;
import logicControllers.UserController;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Concrete implementation of the Command interface.
 * This class handles the logic for retrieving all user records from the system
 * and transmitting them back to the requesting client.
 */
public class GetAllUsersCommand implements Command {
	
	/**
	 * Controller responsible for user-related business logic and database operations.
	 */
    private UserController userController = new UserController();

    /**
     * Executes the command to fetch all users. 
     * It interacts with the UserController, retrieves the list of User entities,
     * and wraps them in a Message object to be sent via the client's connection.
     *
     * @param data   the data sent from the client (not used in this specific command)
     * @param client the connection to the client that issued the command
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