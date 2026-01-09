package commands;

import server.Command;
import src.ocsf.server.ConnectionToClient;
import logicControllers.RestaurantSettingsController;
import messages.UpdateSpecialDateRequest;
import Entities.SpecialDates;

public class UpdateSpecialDateCommand implements Command {

    private final RestaurantSettingsController controller =
            new RestaurantSettingsController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        if (!(data instanceof UpdateSpecialDateRequest)) return;
        
        UpdateSpecialDateRequest req = (UpdateSpecialDateRequest) data;
        
        SpecialDates updated = new SpecialDates(req.getOpeningTime(),req.getClosingTime(),req.getDate(),req.getDescription());
        
        boolean flag = controller.updateSpecialDate(req.getOldDate(), updated);
        
        try {
        	client.sendToClient(flag ? "UPDATE_SPECIAL_DATE_OK" : "UPDATE_SPECIAL_DATE_FAIL");
        } catch(Exception e) {
        	e.printStackTrace();
        }
        	
        
        

        
    }
}
