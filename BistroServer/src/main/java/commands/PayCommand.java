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

public class PayCommand implements Command {

    private final PaymentController paymentController = new PaymentController();
    private final ReservationController reservationController = new ReservationController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        if (!(data instanceof PaymentRequest req)) return;

        int confirmationCode = req.getConfirmationCode();
        Reservation reservation = reservationController.getReservationByCode(confirmationCode);
        if (reservation == null) {
            sendToClient(client, false, null, "Reservation not found");
            return;
        }

        double amount = generateRandomAmount();
        Bill bill = new Bill(0, reservation.getReservationID(), amount, LocalDateTime.now(), true);
        PaymentResult result = paymentController.addPayment(bill);

        if (result.isSuccess()) {
            reservation.setExpectedDepartureTime(req.getDepartureTime());
            reservation.setStatus(enums.ReservationStatus.COMPLETED);
            reservationController.updateReservationFull(reservation); // now departureTime is saved
        }

        
        sendToClient(client, result.isSuccess(), result.getBill(), result.getMessage());
    }

    private void sendToClient(ConnectionToClient client, boolean success, Bill bill, String message) {
        try {
            client.sendToClient(new Message(ActionType.PAY, new ServerResponse(success, bill, message)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private double generateRandomAmount() {
        double amount = 100 + Math.random() * 900;
        return Math.round(amount * 100.0) / 100.0;
    }
}
