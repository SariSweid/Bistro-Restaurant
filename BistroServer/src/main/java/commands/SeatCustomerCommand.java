package commands;

import Entities.Reservation;
import logicControllers.ReservationController;
import messages.SeatCustomerRequest;
import common.ServerResponse;
import common.Message;
import enums.ActionType;
import enums.ReservationStatus;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class SeatCustomerCommand implements Command {

    private ReservationController reservationController = new ReservationController();

    @Override
    public void execute(Object data, ConnectionToClient client) {

        SeatCustomerRequest req = (SeatCustomerRequest) data;
        ServerResponse response;

        try {
            // FIRST: Find the reservation to check its status
            Reservation existingRes = reservationController.getReservationByCode(req.getConfirmationCode());

            // CHECK: Is it system-cancelled?
            if (existingRes != null && 
                existingRes.getStatus() == ReservationStatus.CANCELLED && 
                !existingRes.isNotified()) {
                
                // Do not try to seat. Return the object so the client knows to show the popup
                response = new ServerResponse(
                    false, // It failed to seat
                    existingRes, // DATA is the Reservation object
                    "Reservation was cancelled by the system."
                );
                
            } else {
                // Proceed to try and seat them
                response = reservationController.seatCustomerByCode(
                        req.getConfirmationCode(),
                        req.getUserId(),
                        req.getActualArrivalTime()
                );
            }

            client.sendToClient(new Message(ActionType.SEAT_CUSTOMER, response));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
