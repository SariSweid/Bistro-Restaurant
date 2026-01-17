package commands;

import common.Message;
import enums.ActionType;
import logicControllers.ReservationController;
import messages.GetUserReservationsRequest;
import server.Command;
import src.ocsf.server.ConnectionToClient;

/**
 * Command implementation for retrieving all reservations associated with a specific customer.
 * This class processes requests to fetch a user's reservation history or upcoming bookings
 * by delegating the query to the ReservationController.
 */
public class GetUserReservationsCommand implements Command {

    /**
     * Controller responsible for managing reservation data and customer-specific queries.
     */
    private final ReservationController controller = new ReservationController();

    /**
     * Executes the retrieval logic for a customer's reservations.
     * Extracts the customer ID from the GetUserReservationsRequest, fetches the 
     * relevant reservation list via the controller, and transmits the data back 
     * to the client within a Message object.
     *
     * @param data   the GetUserReservationsRequest containing the unique customer ID
     * @param client the connection to the client that requested the reservation data
     */
    @Override
    public void execute(Object data, ConnectionToClient client) {
        try {
            // Cast the incoming data to the specific request type
            GetUserReservationsRequest req = (GetUserReservationsRequest) data;
            int customerId = req.getCustomerId();

            // Fetch reservations and send them back to the client
            client.sendToClient(new Message(
                ActionType.GET_USER_RESERVATIONS,
                controller.getReservationsByCustomer(customerId)
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}