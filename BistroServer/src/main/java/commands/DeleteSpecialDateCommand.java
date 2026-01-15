package commands;

import server.Command;

import src.ocsf.server.ConnectionToClient;
import logicControllers.RestaurantSettingsController;
import messages.DeleteSpecialDateRequest;
import common.Message;
import common.ServerResponse;
import enums.ActionType;

public class DeleteSpecialDateCommand implements Command{
	private final RestaurantSettingsController controller =
            new RestaurantSettingsController();
	
	
	@Override
    public void execute(Object data, ConnectionToClient client) {
        if (!(data instanceof DeleteSpecialDateRequest req)) {
            sendError(client, "Invalid special date data");
            return;
        }

        boolean ok = controller.deleteSpecialDate(req.getDate());

        controller.getAllWeeklyOpeningHours();
        controller.getAllSpecialDates();

        if (!ok) {
            sendError(client, "Failed to delete special date");
            return;
        }

        sendSuccess(client, "Special date delete successfully");
    }
	
	private void sendSuccess(ConnectionToClient client, String msg) {
		try {
			client.sendToClient(new Message(ActionType.GET_RESTAURANT_SETTINGS, 
					new ServerResponse(true, controller.getRestaurantSettings(),msg)));
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendError(ConnectionToClient client, String msg) {
		try {
			client.sendToClient(new Message(ActionType.GET_RESTAURANT_SETTINGS,
                    new ServerResponse(false, null, msg)));
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
}
