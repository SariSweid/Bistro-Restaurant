package server;

import java.util.HashMap;
import java.util.List;

import Entities.Reservation;
import logicControllers.ReservationController;
import messages.*;
import commands.*;
import common.*;
import enums.ActionType;
import src.ocsf.server.AbstractServer;
import src.ocsf.server.ConnectionToClient;

/**
 * Main server controller.
 * Receives messages from clients and handling
 * to the appropriate Command using a HashMap.
 */
public class ServerController extends AbstractServer {
	
	// Maps each ActionType to its corresponding Command
    private HashMap<ActionType, Command> commands;
    
	/** Default port number the server listens on */
    public static final int DEFAULT_PORT = 5555;
    
    //private ReservationController reservationController;
    
    private ServerUIController ui;
    
    public void setUi(ServerUIController ui) {
        this.ui = ui;
    }
   
    /**
     * Constructs a new ServerController on the specified port.
     * Initializes periodic reservation cleanup and late retrieval checking.
     * @param port the port number to listen on
     */
    public ServerController(int port) {
        super(port);  // Initialize the AbstractServer with the given port
        System.out.println("Server started on port " + port);
        //reservationController = new ReservationController();
        
        // Setup the commands
        commands = new HashMap<>();
        initializeCommands();
    }

    // ===== CLIENT CONNECT EVENT =====
    @Override
    protected void clientConnected(ConnectionToClient client) {
        String ip   = client.getInetAddress().getHostAddress();
        String host = client.getInetAddress().getHostName();

        String info = ip + " (" + host + ")";
        System.out.println("Client connected: " +info); //changed (tamer)
        
        if(ui!=null)
        	ui.addClient(info);
    }
    
    @Override
    protected void clientDisconnected(ConnectionToClient client) { //changed (tamer)
        String info = client.getInetAddress().getHostAddress() + " (" 
                    + client.getInetAddress().getHostName() + ")";
        System.out.println("Client disconnected: " + info);

        if (ui != null) {
            ui.removeClient(info);
        }
    }

    
    /**
     * Registers all supported commands.
     */
    private void initializeCommands() {
        // Command: Update Reservations
        commands.put(ActionType.UPDATE_RESERVATION, new UpdateReservationCommand());
        
        // Command: Add Reservation
        commands.put(ActionType.ADD_RESERVATION, new AddReservationCommand());

        // Command: Get All Reservations
        commands.put(ActionType.GET_ALL_RESERVATIONS, new GetAllReservationsCommand());
        
        // Command: Login 
        commands.put(ActionType.LOGIN, new LoginCommand());
        
        // Command: Logout 
        commands.put(ActionType.LOGOUT, new LogoutCommand());
      
        // Command: Register
        commands.put(ActionType.ADD_USER, new RegisterCommand());
        
        // Command: Get all available times  // <- ADDED THIS
        commands.put(ActionType.GET_AVAILABLE_TIMES, new GetAvailableTimesCommand());
        
        // Command: Get nearest available times (alternatives) <- ADDED THIS
        commands.put(ActionType.GET_NEAREST_TIMES, new GetNearestAvailableTimesCommand());
        
        // Command: Get all user reservations (active)
        commands.put(ActionType.GET_USER_RESERVATIONS, new GetUserReservationsCommand());
        
        // Command: Cancel Reservation
        commands.put(ActionType.CANCEL_RESERVATION, new CancelReservationCommand());
        
        // Command: Add payment
        commands.put(ActionType.PAY, new PayCommand());
        
        // Command: Forgot Code
        commands.put(ActionType.FORGOT_CODE, new ForgotCodeCommand());
        
        // Command: Seat customer
        commands.put(ActionType.SEAT_CUSTOMER, new SeatCustomerCommand());
        
        // Command: Get all tables from db
        commands.put(ActionType.GET_ALL_TABLES, new GetAllTablesCommand());
        
        // Command: Insert new table
        commands.put(ActionType.INSERT_TABLE, new InsertTableCommand());
        
        // Command: Update table seats
        commands.put(ActionType.UPDATE_TABLE, new UpdateTableCommand());
        
     // Command: Delete table
        commands.put(ActionType.DELETE_TABLE, new DeleteTableCommand());
        
        commands.put(ActionType.GET_REPORT, new GetReportCommand());
        
    }
    
    /**
     * Handles incoming messages from clients.
     *
     * @param msg    received object
     * @param client client connection
     */
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
    	if (msg instanceof Message) {
            Message message = (Message) msg;
            ActionType type = message.getAction();

            if (commands.containsKey(type)) {
                // Execute the command.
                System.out.println("Server: Received request for " + type);
                commands.get(type).execute(message.getData(), client);
            } else {
                // We don't have a command for this action
                System.out.println("Server: Error! Unknown Command received: " + type);
            }
        } else {
            System.out.println("Server: Received a non-Message object. Ignoring.");
        }
    }
}
