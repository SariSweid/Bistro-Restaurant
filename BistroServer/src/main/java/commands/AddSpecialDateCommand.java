package commands;

import server.Command;
import src.ocsf.server.ConnectionToClient;
import logicControllers.RestaurantSettingsController;
import Entities.SpecialDates;

public class AddSpecialDateCommand implements Command {

    private final RestaurantSettingsController controller =
            new RestaurantSettingsController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        if (!(data instanceof SpecialDates)) return;

        controller.addSpecialDate((SpecialDates) data);
        
        System.out.println("ADD_SPECIAL_DATE data class: " + data.getClass());

    }
}

