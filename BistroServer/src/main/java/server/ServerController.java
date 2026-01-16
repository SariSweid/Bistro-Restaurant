package server;

import java.util.HashMap;
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
    private java.util.Map<ConnectionToClient, String> connectedClients = new java.util.concurrent.ConcurrentHashMap<>();

    @Override
    protected void clientConnected(ConnectionToClient client) {
        String info = client.getInetAddress().getHostAddress() + " (" + client.getInetAddress().getHostName() + ")";
        
        // Store the exact string used for this specific client object
        connectedClients.put(client, info);
        client.setInfo("Disconnected", null); 
        
        System.out.println("Client connected: " + info);
        if (ui != null) ui.addClient(info);
    }

    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        handleDisconnection(client, "Clean Disconnect");
    }

    @Override
    protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
        handleDisconnection(client, "Abrupt Disconnect");
    }

    private void handleDisconnection(ConnectionToClient client, String type) {
        if (client.getInfo("Disconnected") == null) {
            client.setInfo("Disconnected", true);
            
            // Retrieve the exact string we stored when they connected
            String info = connectedClients.remove(client);
            
            if (info != null) {
                System.out.println("Server: " + type + " for " + info);
                if (ui != null) {
                    ui.removeClient(info);
                }
            }
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
        
        // Command: Fetch user information after login
        commands.put(ActionType.GET_USER_INFORMATION, new GetUserInformationCommand());
        
        // Command: Logout 
        commands.put(ActionType.LOGOUT, new LogoutCommand());
        
        // Command: Register
        commands.put(ActionType.ADD_USER, new RegisterCommand());
        
        // Command: Update user information
        commands.put(ActionType.UPDATE_USER, new UpdateUserCommand());
        
        // Command: Get all users information
        commands.put(ActionType.GET_ALL_USERS, new GetAllUsersCommand());

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
        
        commands.put(ActionType.GET_RESTAURANT_SETTINGS, new GetRestaurantSettingsCommand());
        commands.put(ActionType.UPDATE_OPENING_TIME, new UpdateOpeningTimeCommand());
        commands.put(ActionType.UPDATE_CLOSING_TIME, new UpdateClosingTimeCommand());
        commands.put(ActionType.ADD_SPECIAL_DATE, new AddSpecialDateCommand());
        commands.put(ActionType.UPDATE_SPECIAL_DATE, new UpdateSpecialDateCommand());
        commands.put(ActionType.DELETE_SPECIAL_DATE, new DeleteSpecialDateCommand());
        
        // Command: Update Reservations
        commands.put(ActionType.UPDATE_RESERVATION, new UpdateReservationCommand());

        // Command: Add to Waiting List
        commands.put(ActionType.ADD_TO_WAITING_LIST, new AddToWaitingListCommand());

        // Command: Cancel Waiting
        commands.put(ActionType.CANCEL_WAITING, new CancelWaitingCommand());

        // Command: Get Waiting List Between Dates
        commands.put(ActionType.GET_WAITING_LIST_BETWEEN_DATES, new GetWaitingListBetweenDatesCommand());
        
        commands.put(ActionType.CREATE_OPENING_HOURS, new CreateOpeningHoursCommand());
        commands.put(ActionType.REMOVE_OPENING_HOURS, new RemoveOpeningHoursCommand());
        
        commands.put(ActionType.MARK_NOTIFIED, new MarkNotifiedCommand());
        
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