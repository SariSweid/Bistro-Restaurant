package commands;

import common.Message;
import enums.ActionType;
import logicControllers.ReservationController;
import messages.GetUserReservationsRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class GetUserReservationsCommand implements Command {

    private final ReservationController controller = new ReservationController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            GetUserReservationsRequest req = (GetUserReservationsRequest) data;
            int customerId = req.getCustomerId();

            client.sendToClient(new Message(
                ActionType.GET_USER_RESERVATIONS,
                controller.getReservationsByCustomer(customerId)
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
