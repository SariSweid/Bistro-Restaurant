package handlers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import Controllers.GuestMakeReservationController;
import Controllers.MainMenuController;
import Entities.Reservation;
import client.GuestUpdateReservationUI;
import common.Message;
import enums.ActionType;
import enums.ReservationStatus;
import messages.AddReservationRequest;
import messages.GetAllReservationsRequest;
import messages.GetAvailableTimesRequest;
import messages.LoginRequest;
import messages.RegisterRequest;
import messages.UpdateReservationRequest;
import src.ocsf.client.AbstractClient;

public class ClientHandler extends AbstractClient {
	public boolean awaitResponse = false;
	
	private HashMap<ActionType, ResponseHandler> handlers;
	
	// Singleton instance of ClientHandler
    public static ClientHandler instance;

    private GuestUpdateReservationUI guestUI; // added by tamer for wiring
    
    private GuestMakeReservationController guestMakeReservationController;
    
    private MainMenuController mainMenuController;

    public void setMainMenuController(MainMenuController controller) {
        this.mainMenuController = controller;
    }

    public MainMenuController getMainMenuController() {
        return mainMenuController;
    }
    
    // Constructor
    public ClientHandler(String host, int port) throws IOException {
        super(host, port);
        instance = this;
        openConnection();
        System.out.println(">> Connected to server at " + host + ":" + port);
        handlers = new HashMap<>();
        initializeHandlers();
    }
    
    public void setGuestUI(GuestUpdateReservationUI guestUI) { // added setter tamer
        this.guestUI = guestUI;
    }
    
    // Return the active ClientHandler instance
    public static ClientHandler getClient() { 
    		return instance;
    	}
    
    private void initializeHandlers() {
        // When Server sends reservations --> Run GetAllReservationsHandler
        handlers.put(ActionType.GET_ALL_RESERVATIONS, new GetAllReservationsHandler(guestUI));
        // When Server confirms update --> Run UpdateConfirmHandler
        handlers.put(ActionType.UPDATE_RESERVATION, new UpdateReservationHandler(guestUI));
        // When Client login
        handlers.put(ActionType.LOGIN, new LoginHandler());
        // When Server add user
        handlers.put(ActionType.ADD_USER, new RegisterHandler());
        // When Server add reservation --> Run AddReservationHandler
        handlers.put(ActionType.ADD_RESERVATION, new AddReservationHandler(guestMakeReservationController));
        // When Server returns Available times
        handlers.put(ActionType.GET_AVAILABLE_TIMES, new GetAvailableTimesHandler(guestMakeReservationController));
        
    }
    
    private void sendRequest(Message msg) {
        try {
            sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    
    // ==== SEND REQUESTS ====

    public void register(RegisterRequest req) {
        sendRequest(new Message(ActionType.ADD_USER, req));
    }
    
    public void login(int userID, int membershipCode) {
        sendRequest(new Message(ActionType.LOGIN, new LoginRequest(userID, membershipCode)));
    }
    
    public void getAllReservations() {
        sendRequest(new Message(ActionType.GET_ALL_RESERVATIONS, new GetAllReservationsRequest()));
    }

    public void addReservation(Reservation reservation) {
        awaitResponse = true;
        sendRequest(new Message(ActionType.ADD_RESERVATION, new AddReservationRequest(reservation)));
    }

    public void updateReservation(int id, LocalDate date,LocalTime time , int guests, ReservationStatus status) {
    		sendRequest(new Message(ActionType.UPDATE_RESERVATION, new UpdateReservationRequest(id, date, time, guests, status)));
    }
    
    public void getAvailableTimes(LocalDate date, int guests) {
        sendRequest(new Message(ActionType.GET_AVAILABLE_TIMES, new GetAvailableTimesRequest(date, guests)));
    }
    

    // ==== RECEIVE RESPONSE ====
    @Override
    protected void handleMessageFromServer(Object msg) {
    		// Validate the message format
        if (msg instanceof Message) {
            Message m = (Message) msg;
            ActionType type = m.getAction();

            // Look up the correct Handler in the HashMap
            if (handlers.containsKey(type)) {
                // FOUND IT: Execute the specific handler code
                handlers.get(type).handle(m.getData());
            } else {
                // NOT FOUND: We don't know how to handle this action
                System.out.println(">> Error: Received unknown action type: " + type);
            }

            // Release the Lock
            awaitResponse = false;
        }
    }
}

