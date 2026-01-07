package commands;

import logicControllers.ReservationController;
import messages.SeatCustomerRequest;
import common.ServerResponse;
import common.Message;
import enums.ActionType;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class SeatCustomerCommand implements Command {

    private ReservationController reservationController = new ReservationController();

    @Override
    public void execute(Object data, ConnectionToClient client) {

        SeatCustomerRequest req = (SeatCustomerRequest) data;

        ServerResponse response =
                reservationController.seatCustomerByCode(
                        req.getConfirmationCode(),
                        req.getUserId()
                );

        try {
            client.sendToClient(new Message(ActionType.SEAT_CUSTOMER, response));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

