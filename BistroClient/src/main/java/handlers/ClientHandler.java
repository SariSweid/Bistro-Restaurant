package handlers;

import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import Controllers.BaseReservationController;
import Controllers.CancelReservationController;
import Controllers.MainMenuController;
import Controllers.RestaurantSettingsController;
import Entities.Reservation;
import Entities.SpecialDates;
import client.GuestUpdateReservationUI;
import common.Message;
import enums.ActionType;
import enums.ReservationStatus;
import messages.*;
import src.ocsf.client.AbstractClient;

public class ClientHandler extends AbstractClient {

    public boolean awaitResponse = false;
    private HashMap<ActionType, ResponseHandler> handlers;

    public static ClientHandler instance;

    private GuestUpdateReservationUI guestUI;

    private BaseReservationController activeReservationController;
    private CancelReservationController activeCancelController;
    private MainMenuController mainMenuController;

    private RestaurantSettingsController activeRestaurantSettingsController;
    
    
    
    private int currentUserId;
    private boolean connected = false;

    private ClientHandler(String host, int port) throws IOException {
        super(host, port);
        handlers = new HashMap<>();
        initializeHandlers();
    }

    public static ClientHandler getClient() {
        if (instance == null) {
            try {
                instance = new ClientHandler("localhost", 5555);
                instance.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    public void connect() {
        if (!connected) {
            try {
                openConnection();
                connected = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setCurrentUserId(int id) {
        this.currentUserId = id;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setMainMenuController(MainMenuController controller) {
        this.mainMenuController = controller;
    }

    public MainMenuController getMainMenuController() {
        return mainMenuController;
    }

    public void setActiveReservationController(BaseReservationController controller) {
        this.activeReservationController = controller;
    }

    public BaseReservationController getActiveReservationController() {
        return activeReservationController;
    }

    public void setActiveCancelController(CancelReservationController controller) {
        this.activeCancelController = controller;
    }

    public Object getActiveCancelController() {
        return activeCancelController;
    }

    public void setGuestUI(GuestUpdateReservationUI guestUI) {
        this.guestUI = guestUI;
    }
    
    public void setHandler(ActionType type, ResponseHandler handler) {
        handlers.put(type, handler);
    }

    private void initializeHandlers() {
        handlers.put(ActionType.GET_ALL_RESERVATIONS, new GetAllReservationsHandler(guestUI));
        handlers.put(ActionType.UPDATE_RESERVATION, new UpdateReservationHandler(guestUI));
        handlers.put(ActionType.LOGIN, new LoginHandler());
        handlers.put(ActionType.ADD_USER, new RegisterHandler());
        handlers.put(ActionType.ADD_RESERVATION, new AddReservationHandler());
        handlers.put(ActionType.GET_AVAILABLE_TIMES, new GetAvailableTimesHandler());
        handlers.put(ActionType.GET_NEAREST_TIMES, new GetNearestAvailableTimesHandler());
        handlers.put(ActionType.GET_USER_RESERVATIONS, new GetUserReservationsHandler());
        handlers.put(ActionType.CANCEL_RESERVATION, new CancelReservationHandler());
        handlers.put(ActionType.PAY, new PaymentHandler( null, null));
        handlers.put(ActionType.GET_RESTAURANT_SETTINGS, new GetRestaurantSettingsHandler());
        handlers.put(ActionType.ADD_SPECIAL_DATE, new AddSpecialDateHandler());
        handlers.put(ActionType.UPDATE_SPECIAL_DATE, new UpdateSpecialDateHandler());
        handlers.put(ActionType.GET_ALL_RESERVATIONS, new GetAllReservationsHandler(guestUI));
        handlers.put(ActionType.UPDATE_RESERVATION, new UpdateReservationHandler(guestUI));


    }

    private void sendRequest(Message msg) {
        try {
            sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void register(RegisterRequest req) {
        connect();
        sendRequest(new Message(ActionType.ADD_USER, req));
    }

    public void login(int userID, int membershipCode) {
        connect();
        setCurrentUserId(userID);
        sendRequest(new Message(ActionType.LOGIN, new LoginRequest(userID, membershipCode)));
    }

    public void getAllReservations() {
        connect();
        sendRequest(new Message(ActionType.GET_ALL_RESERVATIONS, new GetAllReservationsRequest()));
    }

    public void addReservation(Reservation reservation) {
        connect();
        awaitResponse = true;
        sendRequest(new Message(ActionType.ADD_RESERVATION, new AddReservationRequest(reservation)));
    }

    public void getUserReservations(int userId) {
        connect();
        sendRequest(new Message(ActionType.GET_USER_RESERVATIONS, new GetUserReservationsRequest(userId)));
    }

    public void cancelReservation(Integer reservationId, Integer confirmationCode, Integer guestId) {
        connect();
        sendRequest(new Message(ActionType.CANCEL_RESERVATION, new CancelReservationRequest(reservationId, confirmationCode, guestId)));
    }

    public void updateReservation(int id, LocalDate date, LocalTime time, int guests, ReservationStatus status) {
        connect();
        sendRequest(new Message(ActionType.UPDATE_RESERVATION,
                new UpdateReservationRequest(id, date, time, guests, status)));
    }

    public void getAvailableTimes(LocalDate date, int guests) {
        connect();
        sendRequest(new Message(ActionType.GET_AVAILABLE_TIMES,
                new GetAvailableTimesRequest(date, guests)));
    }

    public void getNearestAvailableTimes(LocalDate date, int guests) {
        connect();
        sendRequest(new Message(ActionType.GET_NEAREST_TIMES,
                new GetNearestAvailableTimesRequest(date, guests)));
    }
    
    public void Pay(PaymentRequest req) {
        connect();
        sendRequest(new Message(ActionType.PAY, req));
    }

    @Override
    protected void handleMessageFromServer(Object msg) {
        if (msg instanceof Message) {
            Message m = (Message) msg;

            // 
            System.out.println("Received message from server:");
            System.out.println("Action: " + m.getAction());
            System.out.println("Data: " + m.getData());

            ActionType type = m.getAction();
            if (handlers.containsKey(type)) {
                handlers.get(type).handle(m.getData());
            }
            awaitResponse = false;
        } else {
            System.out.println("Received unknown object from server: " + msg);
        }
    }
    
    //restaurant settings:
    public RestaurantSettingsController getActiveRestaurantSettingsController(){
    	return activeRestaurantSettingsController;
    }
    
    public void setActiveRestaurantSettingsController(RestaurantSettingsController controller){
    	this.activeRestaurantSettingsController=controller;
    }
    
    public void getRestaurantSettings() {
    	connect();
    	sendRequest(new Message(ActionType.GET_RESTAURANT_SETTINGS, new GetRestaurantSettingsRequest()));
    }
    
    public void addSpecialDate(SpecialDates specialDate) {
    	connect();
    	sendRequest(new Message(ActionType.ADD_SPECIAL_DATE, new AddSpecialDateRequest(specialDate)));
    }
    
    public void updateSpecialDate(UpdateSpecialDateRequest req) {
        connect();
        sendRequest(new Message(ActionType.UPDATE_SPECIAL_DATE, req));
    }

    public void updateRegularOpeningTime(LocalTime openingTime) {
        connect();
        sendRequest(new Message(ActionType.UPDATE_OPENING_TIME,
                new updateRegularOpeningTimeRequest(openingTime)));
    }

    public void updateRegularClosingTime(LocalTime closingTime) {
        connect();
        sendRequest(new Message(ActionType.UPDATE_CLOSING_TIME,
                new updateRegularClosingTimeRequest(closingTime)));
    }

    
    
    
    
    
    
    
    
    
    
    
    
    //
    
    
    
    
    
    
}
