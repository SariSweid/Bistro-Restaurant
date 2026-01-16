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

/**
 * Command implementation for seating a customer when they arrive at the restaurant.
 * This class validates the reservation status—checking specifically for system-initiated 
 * cancellations—before proceeding with the seating logic via the ReservationController.
 */
public class SeatCustomerCommand implements Command {

    /**
     * Controller responsible for reservation state transitions and seating logic.
     */
    private ReservationController reservationController = new ReservationController();

    /**
     * Executes the customer seating logic.
     * First, it verifies if the reservation exists and hasn't been cancelled by the system 
     * without the user's knowledge. If valid, it updates the reservation to reflect 
     * the actual arrival time and seat assignment.
     *
     * @param data   the SeatCustomerRequest containing the confirmation code and arrival details
     * @param client the connection to the client that issued the seating request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {

        SeatCustomerRequest req = (SeatCustomerRequest) data;
        ServerResponse response;

        try {
            // Find the reservation to check its current status in the database
            Reservation existingRes = reservationController.getReservationByCode(req.getConfirmationCode());

            // Check if the reservation was already system-cancelled but the user wasn't notified
            if (existingRes != null && 
                existingRes.getStatus() == ReservationStatus.CANCELLED && 
                !existingRes.isNotified()) {
                
                // Do not attempt to seat; return existing state to trigger client-side notification popup
                response = new ServerResponse(
                    false, 
                    existingRes, 
                    "Reservation was cancelled by the system."
                );
                
            } else {
                // Proceed with the standard seating process through the controller
                response = reservationController.seatCustomerByCode(
                        req.getConfirmationCode(),
                        req.getUserId(),
                        req.getActualArrivalTime()
                );
            }

            // Send the result back to the client
            client.sendToClient(new Message(ActionType.SEAT_CUSTOMER, response));
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}