package commands;

import java.io.IOException;
import java.time.LocalDateTime;

import Entities.Bill;
import Entities.Reservation;
import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.PaymentController;
import logicControllers.ReservationController;
import messages.PaymentRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;
import util.PaymentResult;

/**
 * Command implementation for processing payments and finalizing reservations.
 * This class coordinates between the PaymentController to handle billing 
 * and the ReservationController to update the reservation status upon successful payment.
 */
public class PayCommand implements Command {

    /**
     * Controller responsible for processing payments and generating bill records.
     */
    private final PaymentController paymentController = new PaymentController();
    
    /**
     * Controller responsible for retrieving and updating reservation details.
     */
    private final ReservationController reservationController = new ReservationController();

    /**
     * Executes the payment process for a specific reservation.
     * The method retrieves the reservation by its confirmation code, generates a bill with 
     * a calculated amount, and attempts to process the payment. If successful, it updates 
     * the reservation's status to COMPLETED, records the departure time, and releases the table.
     *
     * @param data   the PaymentRequest containing the confirmation code and departure time
     * @param client the connection to the client that issued the payment request
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        if (!(data instanceof PaymentRequest req)) return;

        int confirmationCode = req.getConfirmationCode();
        
        // Step 1: Locate the reservation to be paid
        Reservation reservation = reservationController.getReservationByCode(confirmationCode);
        if (reservation == null) {
            sendToClient(client, false, null, "Reservation not found");
            return;
        }

        // Step 2: Generate billing information
        double amount = generateRandomAmount();
        Bill bill = new Bill(0, reservation.getReservationID(), amount, LocalDateTime.now(), true);
        
        // Step 3: Process the payment via the controller
        PaymentResult result = paymentController.addPayment(bill);

        // Step 4: If payment succeeded, finalize the reservation details
        if (result.isSuccess()) {
            reservation.setExpectedDepartureTime(req.getDepartureTime());
            reservation.setStatus(enums.ReservationStatus.COMPLETED);
            reservation.setTableID(null); // Release the table assignment
            reservationController.updateReservationFull(reservation); 
        }

        // Step 5: Notify the client of the result
        sendToClient(client, result.isSuccess(), result.getBill(), result.getMessage());
    }

    /**
     * Wraps the response data and sends it back to the client.
     *
     * @param client  the client connection instance
     * @param success indicates if the operation was successful
     * @param bill    the resulting Bill object (null if failed)
     * @param message a descriptive status message
     */
    private void sendToClient(ConnectionToClient client, boolean success, Bill bill, String message) {
        try {
            client.sendToClient(new Message(ActionType.PAY, new ServerResponse(success, bill, message)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a random bill amount for simulation purposes.
     *
     * @return a double representing the total bill amount, rounded to two decimal places
     */
    private double generateRandomAmount() {
        double amount = 100 + Math.random() * 900;
        return Math.round(amount * 100.0) / 100.0;
    }
}