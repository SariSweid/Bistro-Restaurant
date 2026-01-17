package server;

import java.util.HashMap;
import commands.*;
import common.*;
import enums.ActionType;
import src.ocsf.server.AbstractServer;
import src.ocsf.server.ConnectionToClient;

/**
 * Main server controller that extends the AbstractServer. 
 * It manages client connections and routes incoming messages to specific 
 * command objects using a Command Pattern implemented with a HashMap.
 */
public class ServerController extends AbstractServer {
	
	/** A map linking each ActionType to its corresponding Command execution logic. */
    private HashMap<ActionType, Command> commands;
    
	/** The default port number used by the server if none is specified. */
    public static final int DEFAULT_PORT = 5555;
    
    /** Controller for managing the Server's User Interface. */
    private ServerUIController ui;
    
    /**
     * Associates a UI controller with the server to display connection updates and logs.
     * @param ui the ServerUIController instance to use.
     */
    public void setUi(ServerUIController ui) {
        this.ui = ui;
    }
   
    /**
     * Constructs a new ServerController on the specified port.
     * Initializes the command map and registers all supported application commands.
     * @param port the port number to listen on.
     */
    public ServerController(int port) {
        super(port);
        System.out.println("Server started on port " + port);
        
        commands = new HashMap<>();
        initializeCommands();
    }

    /** A thread-safe map storing active client connections and their descriptive info strings. */
    private java.util.Map<ConnectionToClient, String> connectedClients = new java.util.concurrent.ConcurrentHashMap<>();

    /**
     * Triggered automatically when a new client connects.
     * Logs connection details and updates the Server UI.
     * @param client the connection object representing the connected client.
     */
    @Override
    protected void clientConnected(ConnectionToClient client) {
        String info =
                client.getInetAddress().getHostAddress() +
                " (" + client.getInetAddress().getHostName() + ")" +
                " | connectionId=" + client.getId();

        connectedClients.put(client, info);
        client.setInfo("Disconnected", null);

        System.out.println("Client connected: " + info);
        if (ui != null) ui.addClient(info);
    }

    /**
     * Triggered when a client disconnects normally.
     * @param client the connection that was closed.
     */
    @Override
    protected synchronized void clientDisconnected(ConnectionToClient client) {
        handleDisconnection(client, "Clean Disconnect");
    }

    /**
     * Triggered when a client connection is lost due to an exception.
     * @param client the connection that encountered the error.
     * @param exception the throwable cause of the disconnect.
     */
    @Override
    protected synchronized void clientException(ConnectionToClient client, Throwable exception) {
        handleDisconnection(client, "Abrupt Disconnect");
    }

    /**
     * Internal helper method to process client removals from the tracking map and UI.
     * Ensures that disconnection logic is only executed once per client.
     * @param client the client being disconnected.
     * @param type a string describing the nature of the disconnect.
     */
    private void handleDisconnection(ConnectionToClient client, String type) {
        if (client.getInfo("Disconnected") == null) {
            client.setInfo("Disconnected", true);
            
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
     * Populates the commands HashMap with ActionType keys and their 
     * respective Command implementations. This maps client requests to server logic.
     */
    private void initializeCommands() {
        // Reservation Management
        commands.put(ActionType.UPDATE_RESERVATION, new UpdateReservationCommand());
        commands.put(ActionType.ADD_RESERVATION, new AddReservationCommand());
        commands.put(ActionType.GET_ALL_RESERVATIONS, new GetAllReservationsCommand());
        commands.put(ActionType.CANCEL_RESERVATION, new CancelReservationCommand());
        commands.put(ActionType.GET_USER_RESERVATIONS, new GetUserReservationsCommand());
        commands.put(ActionType.GET_AVAILABLE_TIMES, new GetAvailableTimesCommand());
        commands.put(ActionType.GET_NEAREST_TIMES, new GetNearestAvailableTimesCommand());
        commands.put(ActionType.MARK_NOTIFIED, new MarkNotifiedCommand());

        // User and Authentication
        commands.put(ActionType.LOGIN, new LoginCommand());
        commands.put(ActionType.GET_USER_INFORMATION, new GetUserInformationCommand());
        commands.put(ActionType.LOGOUT, new LogoutCommand());
        commands.put(ActionType.ADD_USER, new RegisterCommand());
        commands.put(ActionType.UPDATE_USER, new UpdateUserCommand());
        commands.put(ActionType.GET_ALL_USERS, new GetAllUsersCommand());

        // Payment and Operations
        commands.put(ActionType.PAY, new PayCommand());
        commands.put(ActionType.FORGOT_CODE, new ForgotCodeCommand());
        commands.put(ActionType.SEAT_CUSTOMER, new SeatCustomerCommand());
        
        // Table Management
        commands.put(ActionType.GET_ALL_TABLES, new GetAllTablesCommand());
        commands.put(ActionType.INSERT_TABLE, new InsertTableCommand());
        commands.put(ActionType.UPDATE_TABLE, new UpdateTableCommand());
        commands.put(ActionType.DELETE_TABLE, new DeleteTableCommand());

        // Reports and Settings
        commands.put(ActionType.GET_REPORT, new GetReportCommand());
        commands.put(ActionType.GET_RESTAURANT_SETTINGS, new GetRestaurantSettingsCommand());
        commands.put(ActionType.UPDATE_OPENING_TIME, new UpdateOpeningTimeCommand());
        commands.put(ActionType.UPDATE_CLOSING_TIME, new UpdateClosingTimeCommand());
        commands.put(ActionType.ADD_SPECIAL_DATE, new AddSpecialDateCommand());
        commands.put(ActionType.UPDATE_SPECIAL_DATE, new UpdateSpecialDateCommand());
        commands.put(ActionType.DELETE_SPECIAL_DATE, new DeleteSpecialDateCommand());
        commands.put(ActionType.CREATE_OPENING_HOURS, new CreateOpeningHoursCommand());
        commands.put(ActionType.REMOVE_OPENING_HOURS, new RemoveOpeningHoursCommand());

        // Waiting List Management
        commands.put(ActionType.ADD_TO_WAITING_LIST, new AddToWaitingListCommand());
        commands.put(ActionType.CANCEL_WAITING, new CancelWaitingCommand());
        commands.put(ActionType.GET_WAITING_LIST_BETWEEN_DATES, new GetWaitingListBetweenDatesCommand());
    }
    
    /**
     * Core message handler for all incoming data from clients.
     * It expects a Message object, extracts the ActionType, and executes the 
     * corresponding Command found in the map.
     *
     * @param msg the object received from the client (expected to be of type Message).
     * @param client the connection object for the client that sent the message.
     */
    @Override
    protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
    	if (msg instanceof Message) {
            Message message = (Message) msg;
            ActionType type = message.getAction();

            if (commands.containsKey(type)) {
                System.out.println("Server: Received request for " + type);
                commands.get(type).execute(message.getData(), client);
            } else {
                System.out.println("Server: Error! Unknown Command received: " + type);
            }
        } else {
            System.out.println("Server: Received a non-Message object. Ignoring.");
        }
    }
}