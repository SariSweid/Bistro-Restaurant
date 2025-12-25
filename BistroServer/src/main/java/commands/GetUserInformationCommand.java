package commands;

import Entities.User;
import logicControllers.UserController;
import messages.GetUserInformationRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class GetUserInformationCommand implements Command {
	// Controller responsible for reservation business logic
    private UserController userController = new UserController();

    /**
     * Command responsible for handling GET_USER_INFORMATION requests.
     */
	@Override
	public void execute(Object data, ConnectionToClient client) {
		// Cast the data object to GetUserInformationRequest
		GetUserInformationRequest req = (GetUserInformationRequest) data;

		// Get the user information from the controller
        User user = userController.getUserInformation(req.getUserID());
        
        // Send the user object back to the client
        try {
            if (user != null) {
                client.sendToClient(user); // user object itself
            } else {
                client.sendToClient("USER_NOT_FOUND");
            }
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
