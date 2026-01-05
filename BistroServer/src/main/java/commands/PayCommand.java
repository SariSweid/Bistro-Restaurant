package commands;

import java.io.IOException;

import Entities.Bill;
import common.Message;
import common.ServerResponse;
import enums.ActionType;
import logicControllers.PaymentController;
import messages.PaymentRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

public class PayCommand implements Command {

    private PaymentController paymentController = new PaymentController();

    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            if (!(data instanceof PaymentRequest req)) return;

            int code = req.getConfirmationCode();
            Bill bill = new Bill(code , 0 , 0.0);

            boolean success = paymentController.addPayment(bill);

            ServerResponse res = success
                    ? new ServerResponse(true, bill, "Payment successful")
                    : new ServerResponse(false, null, "Payment failed");

            client.sendToClient(new Message(ActionType.PAY, res));

        } catch (Exception e) {
            e.printStackTrace();
            try {
                client.sendToClient(new Message(ActionType.PAY,
                        new ServerResponse(false, null, "Server error")));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
}