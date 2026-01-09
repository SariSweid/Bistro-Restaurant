package commands;

import server.Command;
import src.ocsf.server.ConnectionToClient;
import logicControllers.RestaurantSettingsController;
import Entities.WeeklyOpeningHours;

public class UpdateOpeningTimeCommand implements Command {

    private final RestaurantSettingsController controller =
            new RestaurantSettingsController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        if (!(data instanceof WeeklyOpeningHours)) return;

        WeeklyOpeningHours hours = (WeeklyOpeningHours) data;

        WeeklyOpeningHours current =
                controller.getOpeningHoursForDay(hours.getDay());

        current.setOpeningTime(hours.getOpeningTime());
        controller.updateWeeklyOpeningHours(current);
        
        System.out.println("OPENING UPDATE: day=" + hours.getDay() + " open=" + hours.getOpeningTime());
        boolean ok = controller.updateWeeklyOpeningHours(current);
        System.out.println("OPENING UPDATE OK? " + ok);

    }
}
