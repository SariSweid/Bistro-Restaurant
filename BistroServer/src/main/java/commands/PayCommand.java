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

public class PayCommand implements Command {

    private final PaymentController paymentController = new PaymentController();
    private final ReservationController reservationController = new ReservationController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        if (!(data instanceof PaymentRequest req)) return;

        int confirmationCode = req.getConfirmationCode();
        Reservation reservation = reservationController.getReservationByCode(confirmationCode);
        System.out.println("rsrsrs" + reservation );
        if (reservation == null) {
            try {
				client.sendToClient(new Message(ActionType.PAY,
				    new ServerResponse(false, null, "Reservation not found")));
				return;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return;
        }

        Bill bill = new Bill(0, reservation.getReservationID(), generateRandomAmount(),LocalDateTime.now(),true);
        boolean success = paymentController.addPayment(bill);

        ServerResponse response = success
            ? new ServerResponse(true, bill, "Payment processed successfully")
            : new ServerResponse(false, null, "Payment failed");
        try {
			client.sendToClient(new Message(ActionType.PAY, response));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    private double generateRandomAmount() {
        double amount = 100 + Math.random() * 900; 
        return Math.round(amount * 100.0) / 100.0; 
    }
}
