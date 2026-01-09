package handlers;

import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;

import Controllers.BaseReservationController;
import Controllers.MainMenuController;
import Controllers.OrderController;
import Controllers.ReportController;
import Controllers.RestaurantSettingsController;
import Controllers.TablesController;
import Entities.Reservation;
import Entities.SpecialDates;
import client.GuestUpdateReservationUI;
import common.Message;
import enums.ActionType;
import enums.ReportType;
import enums.ReservationStatus;
import enums.UserRole;
import messages.*;
import src.ocsf.client.AbstractClient;

public class ClientHandler extends AbstractClient {

    public boolean awaitResponse = false;
    private HashMap<ActionType, ResponseHandler> handlers;

    public static ClientHandler instance;

    private GuestUpdateReservationUI guestUI;

    
    private ReportController reportController;
    private BaseReservationController activeReservationController;
    private Object activeCancelController;
    private MainMenuController mainMenuController;

    private RestaurantSettingsController activeRestaurantSettingsController;
    
    
    private UserRole currentuserrole;
    private int currentUserId;
    private boolean connected = false;
    
    private OrderController orderController;

    public void setOrderController(OrderController controller) {
        this.orderController = controller;
    }

    public OrderController getOrderController() {
        return orderController;
    }

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
    
    public void setCurrentUserRole(UserRole role) {
    	this.currentuserrole = role;
    }
    
    public void setReportController(ReportController controller) {
        this.reportController = controller;
    }
    
    
    
    public UserRole getCurrentUserRole() {
    	return currentuserrole;
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

    public void setActiveCancelController(Object controller) {
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
    
    private TablesController tablesController;

    public void setTablesController(TablesController controller) {
        this.tablesController = controller;
    }

    public TablesController getTablesController() {
        return tablesController;
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
        handlers.put(ActionType.LOGOUT, new LogoutHandler());
        handlers.put(ActionType.GET_RESTAURANT_SETTINGS, new GetRestaurantSettingsHandler());
        handlers.put(ActionType.ADD_SPECIAL_DATE, new AddSpecialDateHandler());
        handlers.put(ActionType.UPDATE_SPECIAL_DATE, new UpdateSpecialDateHandler());
        handlers.put(ActionType.GET_ALL_RESERVATIONS, new GetAllReservationsHandler(guestUI));
        handlers.put(ActionType.UPDATE_RESERVATION, new UpdateReservationHandler(guestUI));
        handlers.put(ActionType.FORGOT_CODE, new ForgotCodeHandler());
        handlers.put(ActionType.SEAT_CUSTOMER, new SeatCustomerHandler());
        handlers.put(ActionType.GET_ALL_TABLES, new GetAllTablesHandler());
        handlers.put(ActionType.INSERT_TABLE, new InsertTableHandler());
        handlers.put(ActionType.UPDATE_TABLE, new UpdateTableHandler());
        handlers.put(ActionType.DELETE_TABLE, new DeleteTableHandler());
        handlers.put(ActionType.DELETE_TABLE, new DeleteTableHandler());
        handlers.put(ActionType.GET_REPORT, new ReportHandler(null));

    }

    private void sendRequest(Message msg) {
        try {
            // auto-reconnect if the real socket is dead
            if (!isConnected()) {
                System.out.println("Reconnecting socket...");
                openConnection();
            }
            sendToServer(msg);  // use the LIVE socket
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Socket failed: " + e.getMessage());
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
    
    public void logout() {
        sendRequest(new Message(ActionType.LOGOUT, new LogoutRequest()));
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
    
    public void seatCustomer(int userId, int confirmationCode, LocalTime actualArrival) {
        connect();
        SeatCustomerRequest req = new SeatCustomerRequest(userId, confirmationCode);
        req.setActualArrivalTime(actualArrival);  // set before sending
        sendRequest(new Message(ActionType.SEAT_CUSTOMER, req));
    }


    public void forgotCode(int userId) {
    		connect();
        sendRequest(new Message(ActionType.FORGOT_CODE, new ForgotCodeRequest(userId)));
    }

    
    public void getAllTables() {
        sendRequest(new Message(ActionType.GET_ALL_TABLES, null));
    }

    public void insertTable(int tableId, int seats) {
        InsertTableRequest req = new InsertTableRequest(tableId, seats);
        sendRequest(new Message(ActionType.INSERT_TABLE, req));
    }

    public void updateTable(int tableId, int seats) {
        UpdateTableRequest req = new UpdateTableRequest(tableId, seats);
        sendRequest(new Message(ActionType.UPDATE_TABLE, req));
    }

    public void deleteTable(int tableId) {
        DeleteTableRequest req = new DeleteTableRequest(tableId);
        sendRequest(new Message(ActionType.DELETE_TABLE, req));
    }
    
    public void requestReport(ReportType reportType, ReportController controller) {
        connect();

      
        ResponseHandler handler = handlers.get(ActionType.GET_REPORT);
        if (handler instanceof ReportHandler) {
            ((ReportHandler) handler).setController(controller);
        }

         
        ReportRequest req = new ReportRequest(reportType);
        sendRequest(new Message(ActionType.GET_REPORT, req));
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
