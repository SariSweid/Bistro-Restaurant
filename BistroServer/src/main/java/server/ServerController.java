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
    
    private ReservationController reservationController;
    
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
        reservationController = new ReservationController();
        
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
        // Command #1: Update Reservations
        commands.put(ActionType.UPDATE_RESERVATION, new UpdateReservationCommand());
        
        // Command #2: Add Reservation
       commands.put(ActionType.ADD_RESERVATION, new AddReservationCommand());

        // Command #3: Get All Reservations
        commands.put(ActionType.GET_ALL_RESERVATIONS, new GetAllReservationsCommand());
        
        // Command #4: Login 
        commands.put(ActionType.LOGIN, new LoginCommand());
        
        // Command #4: Register
        commands.put(ActionType.ADD_USER, new RegisterCommand());
        
        //Command #5: Get all available times  // <- ADDED THIS
        commands.put(ActionType.GET_AVAILABLE_TIMES, new GetAvailableTimesCommand());
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
