package logicControllers;

import server.DBController;
import entities.Reservation;
import messages.AddReservationRequest;
import messages.UpdateReservationRequest;
import java.util.List;
import ocsf.server.*;

public class ServerController extends AbstractServer {
   
	/** Default port number the server listens on */
    public static final int DEFAULT_PORT = 5555;
    
    private ReservationController reservationController;
   
    /**
     * Constructs a new ServerController on the specified port.
     * Initializes periodic reservation cleanup and late retrieval checking.
     * @param port the port number to listen on
     */
    public ServerController(int port) {
        super(port);  // Initialize the AbstractServer with the given port
        System.out.println("Server started on port " + port);
        reservationController = new ReservationController();
    }

    // ===== CLIENT CONNECT EVENT =====
    @Override
    protected void clientConnected(ConnectionToClient client) {
        String ip   = client.getInetAddress().getHostAddress();
        String host = client.getInetAddress().getHostName();

        System.out.println("Client connected: IP=" + ip + "\nHOST=" + host);
    }
    
    @Override
    protected void clientDisconnected(ConnectionToClient client) {
        System.out.println("Client disconnected: " + client.getInetAddress());
    }

    /**
     * Receives messages from clients and delegates them to specific handler methods.
     * @param msg - the message received from the client
     * @param client - the client connection that sent the message
     */
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
    	System.out.println("Server received: " + (msg == null ? "null" : msg.getClass().getSimpleName() + " -> " + msg.toString()));

    	try {
    		
    		// =========== GET ALL RESERVATIONS ===========
            if ("GET_ALL_RESERVATIONS".equals(msg)) {
                System.out.println("Received get_reservations request");
                List<Reservation> reservations = reservationController.getAllReservations();
                client.sendToClient(reservations);
            }

            // =========== UPDATE RESERVATION ===========
            if (msg instanceof UpdateReservationRequest req) {
                System.out.println("Received update request");
                boolean success = reservationController.updateReservation(
                        req.getReservationID(),
                        req.getReservationDate(),
                        req.getNumOfGuests()
                );
                client.sendToClient(success ? "UPDATE_OK" : "UPDATE_FAIL");
            }
            
            // =========== ADD RESERVATION ===========
            if (msg instanceof AddReservationRequest req) {
                System.out.println("Received add reservation request");
                boolean success = reservationController.addReservation(req.getReservation());
                client.sendToClient(success ? "ADD_OK" : "ADD_FAIL");
            }

            // ----------- UNKNOWN MESSAGE -----------
            client.sendToClient("UNKNOWN_REQUEST");
        }
        catch (Exception e) {

            e.printStackTrace();

            try {
                client.sendToClient("SERVER_ERROR");
            } catch (Exception ignored) {}
        }
    }

}