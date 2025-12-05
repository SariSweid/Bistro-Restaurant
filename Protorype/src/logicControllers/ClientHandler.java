package logicControllers;
import gui.GuestReservationUI;
import javafx.application.Platform;

import java.io.IOException;
import java.util.List;

import Entities.Reservation;
import messages.AddReservationRequest;
import messages.UpdateReservationRequest;
import src.ocsf.client.AbstractClient;

public class ClientHandler extends AbstractClient {
	
	// Singleton instance of ClientHandler
    public static ClientHandler instance;

    private GuestReservationUI guestUI; // added by tamer for wiring
    // Constructor
    public ClientHandler(String host, int port) {
        super(host, port);
        instance = this;
        
        try {
            openConnection();
            System.out.println("Connected to server successfully.");
        } catch (IOException e) {
            System.out.println("Failed to connect to server.");
        }
    }
    
    // Return the active ClientHandler instance
    public static ClientHandler getClient() { 
    	return instance;
    	}

    // ==== SEND REQUESTS ====
    
    // SELECT
    public void getAllReservations() {
        try {
            sendToServer("GET_ALL_RESERVATIONS");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setGuestUI(GuestReservationUI guestUI) { // added setter tamer
        this.guestUI = guestUI;
    }
    
    
    // ADD
    public void addReservation(Reservation reservation) {
        try {
            AddReservationRequest req = new AddReservationRequest(reservation);
            sendToServer(req);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // UPDATE
    public void updateReservation(int id, java.sql.Date date, int guests) {   
        try {
            sendToServer(
                new UpdateReservationRequest(id, date, guests)
            );
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    // ==== RECEIVE RESPONSE ====
    @Override
    protected void handleMessageFromServer(Object msg) {
    	System.out.println("Client received: " + (msg == null ? "null" : msg.getClass().getSimpleName() + " -> " + msg.toString()));

        // ----- RECEIVE RESERVATION LIST -----
        if (msg instanceof List<?> list) {
        	@SuppressWarnings("unchecked")
            List<Reservation> reservations = (List<Reservation>) list;
        	Platform.runLater(() ->
        		guestUI.displayAllReservations(reservations)
        	);
        }

        // ===== RECEIVE UPDATE RESULT =====
        if ("UPDATE_OK".equals(msg)) {
        	Platform.runLater(() ->
            	guestUI.showMessage("Reservation updated successfully.")
        	);
        }

        if ("UPDATE_FAIL".equals(msg)) {
        	Platform.runLater(() ->
            	guestUI.showMessage("Failed to update reservation.")
        	);
        }
        
        if ("ADD_OK".equals(msg)) {
            Platform.runLater(() -> 
            	guestUI.showMessage("Reservation added successfully.")
            );
            return;
        }

        if ("ADD_FAIL".equals(msg)) {
            Platform.runLater(() -> 
            	guestUI.showMessage("Failed to add reservation.")
            );
            return;
        }

        // ==== ERRORS ====
        if ("SERVER_ERROR".equals(msg)) {
            Platform.runLater(() ->
                guestUI.showMessage("Server error.")
            );
        }
        System.out.println("Unknown server message: " + msg);
    }
}
