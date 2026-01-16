package handlers;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;

import Controllers.AvailableTimesListener;
import Controllers.BaseDisplayController;
import Controllers.BaseReservationController;
import Controllers.MainMenuController;
import Controllers.ReportController;
import Controllers.RestaurantSettingsController;
import Controllers.TablesController;
import Entities.Reservation;
import Entities.SpecialDates;
import Entities.User;
import Entities.WeeklyOpeningHours;
import client.GuestUpdateReservationUI;
import common.Message;
import enums.ActionType;
import enums.ReportType;
import enums.ReservationStatus;
import enums.UserRole;
import messages.AddReservationRequest;
import messages.AddSpecialDateRequest;
import messages.AddToWaitingListRequest;
import messages.CancelReservationRequest;
import messages.CancelWaitingRequest;
import messages.CreateRegularOpeningHoursRequest;
import messages.DeleteOpeningHoursRequest;
import messages.DeleteSpecialDateRequest;
import messages.DeleteTableRequest;
import messages.ForgotCodeRequest;
import messages.GetAllReservationsRequest;
import messages.GetAvailableTimesRequest;
import messages.GetNearestAvailableTimesRequest;
import messages.GetRestaurantSettingsRequest;
import messages.GetUserInformationRequest;
import messages.GetUserReservationsRequest;
import messages.GetWaitingListBetweenDatesRequest;
import messages.InsertTableRequest;
import messages.LoginRequest;
import messages.LogoutRequest;
import messages.PaymentRequest;
import messages.RegisterRequest;
import messages.ReportRequest;
import messages.SeatCustomerRequest;
import messages.UpdateReservationRequest;
import messages.UpdateSpecialDateRequest;
import messages.UpdateTableRequest;
import messages.UpdateUserRequest;
import messages.updateRegularClosingTimeRequest;
import messages.updateRegularOpeningTimeRequest;
import src.ocsf.client.AbstractClient;

/**
 * ClientHandler manages communication with the server for all client operations.
 * It extends AbstractClient to provide a custom messaging protocol for the restaurant system.
 * 
 * Responsibilities include:
 * - Handling reservations
 * - Managing users
 * - Fetching reports
 * - Managing restaurant tables and settings
 * - Handling waiting lists
 * 
 * This class follows a singleton pattern. Use ClientHandler.getClient() to access the instance.
 */
public class ClientHandler extends AbstractClient {

    /** Flag indicating whether the client is awaiting a server response */
    public boolean awaitResponse = false;

    /** Maps ActionType to corresponding response handlers */
    private HashMap<ActionType, ResponseHandler> handlers;

    /** Maps ReportType to corresponding report handlers */
    private HashMap<ReportType, ReportHandler> reportHandlers = new HashMap<>();

    /** Singleton instance of ClientHandler */
    public static ClientHandler instance;

    /** Guest reservation UI reference */
    private GuestUpdateReservationUI guestUI;

    /** Reference to main menu controller */
    private MainMenuController mainMenuController;

    /** Tracks if user navigated from a higher role */
    private boolean cameFromHigherRole = false;

    /** Active restaurant settings controller */
    private RestaurantSettingsController activeRestaurantSettingsController;

    /** Current user's role */
    private UserRole currentuserrole;

    /** Current user's ID */
    private int currentUserId;

    /** Connection status */
    private boolean connected = false;

    /** Current user object */
    private User currentUser;

    /** Reference to tables controller */
    private TablesController tablesController;

    /** List of all reservations */
    private List<Reservation> allReservations;

    /** Listener for available times updates */
    private AvailableTimesListener availableTimesListener;

    /** Active reservation controller */
    private BaseReservationController activeReservationController;

    /**
     * Returns the currently active reservation controller
     */
    public BaseReservationController getActiveReservationController() {
        return activeReservationController;
    }

    /**
     * Sets the currently active reservation controller
     */
    public void setActiveReservationController(BaseReservationController activeReservationController) {
        this.activeReservationController = activeReservationController;
    }

    /**
     * Private constructor for singleton pattern.
     * Initializes handlers map and sets up all default response handlers.
     */
    private ClientHandler(String host, int port) throws IOException {
        super(host, port);
        handlers = new HashMap<>();
        initializeHandlers();
    }

    /**
     * Returns the singleton instance of ClientHandler, connecting to localhost if necessary
     */
    public static ClientHandler getClient() {
        if (instance == null) {
            return getClient("localhost");
        }
        return instance;
    }

    /**
     * Returns the singleton instance of ClientHandler connecting to a specific host
     */
    public static ClientHandler getClient(String host) {
        if (instance == null) {
            try {
                instance = new ClientHandler(host, 5555);
                instance.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instance;
    }

    /**
     * Connects to the server if not already connected
     */
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

    /**
     * Sets the current user and updates ID and role
     */
    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.currentUserId = user.getUserId();
        this.currentuserrole = user.getRole();
    }

    /**
     * Sets only the current user ID
     */
    public void setCurrentUserId(int id) {
        this.currentUserId = id;
    }

    /**
     * Sets only the current user role
     */
    public void setCurrentUserRole(UserRole role) {
        this.currentuserrole = role;
    }

    /**
     * Returns the current user object
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * Returns the current user role
     */
    public UserRole getCurrentUserRole() {
        return currentuserrole;
    }

    /**
     * Returns the current user ID
     */
    public int getCurrentUserId() {
        return currentUserId;
    }

    /**
     * Sets whether the user came from a higher role
     */
    public void setCameFromHigherRole(boolean value) {
        this.cameFromHigherRole = value;
    }

    /**
     * Returns true if the user came from a higher role
     */
    public boolean cameFromHigherRole() {
        return cameFromHigherRole;
    }

    /**
     * Sets the main menu controller reference
     */
    public void setMainMenuController(MainMenuController controller) {
        this.mainMenuController = controller;
    }

    /**
     * Returns the main menu controller
     */
    public MainMenuController getMainMenuController() {
        return mainMenuController;
    }

    /** Reference to the currently active display controller */
    private BaseDisplayController activeDisplayController;

    /**
     * Sets the active display controller
     */
    public void setActiveDisplayController(BaseDisplayController controller) {
        this.activeDisplayController = controller;
    }

    /**
     * Returns the currently active display controller
     */
    public BaseDisplayController getActiveDisplayController() {
        return activeDisplayController;
    }

    /**
     * Sets the guest reservation UI
     */
    public void setGuestUI(GuestUpdateReservationUI guestUI) {
        this.guestUI = guestUI;
    }

    /**
     * Registers a specific ResponseHandler for an ActionType
     */
    public void setHandler(ActionType type, ResponseHandler handler) {
        handlers.put(type, handler);
    }

    /**
     * Sets the tables controller reference
     */
    public void setTablesController(TablesController controller) {
        this.tablesController = controller;
    }

    /**
     * Returns the tables controller
     */
    public TablesController getTablesController() {
        return tablesController;
    }

    /**
     * Returns the list of all reservations
     */
    public List<Reservation> getAllReservationsList() {
        return allReservations;
    }

    /**
     * Sets the list of all reservations
     */
    public void setAllReservationsList(List<Reservation> list) {
        this.allReservations = list;
    }

    /**
     * Initializes all default response handlers
     */
    private void initializeHandlers() {
        handlers.put(ActionType.GET_ALL_RESERVATIONS, new GetAllReservationsHandler());
        handlers.put(ActionType.UPDATE_RESERVATION, new UpdateReservationHandler(guestUI));
        handlers.put(ActionType.LOGIN, new LoginHandler());
        handlers.put(ActionType.GET_USER_INFORMATION, new GetUserInformationHandler(this));
        handlers.put(ActionType.ADD_USER, new RegisterHandler());
        handlers.put(ActionType.UPDATE_USER, new UpdateUserHandler());
        handlers.put(ActionType.GET_ALL_USERS, new GetAllUsersHandler());
        handlers.put(ActionType.ADD_RESERVATION, new AddReservationHandler());
        handlers.put(ActionType.GET_AVAILABLE_TIMES, new GetAvailableTimesHandler());
        handlers.put(ActionType.GET_NEAREST_TIMES, new GetNearestAvailableTimesHandler());
        handlers.put(ActionType.GET_USER_RESERVATIONS, new GetUserReservationsHandler());
        handlers.put(ActionType.CANCEL_RESERVATION, new CancelReservationHandler());
        handlers.put(ActionType.PAY, new PaymentHandler(null, null));
        handlers.put(ActionType.LOGOUT, new LogoutHandler());
        handlers.put(ActionType.GET_RESTAURANT_SETTINGS, new GetRestaurantSettingsHandler());
        handlers.put(ActionType.ADD_SPECIAL_DATE, new AddSpecialDateHandler());
        handlers.put(ActionType.UPDATE_SPECIAL_DATE, new UpdateSpecialDateHandler());
        handlers.put(ActionType.DELETE_SPECIAL_DATE, new DeleteSpecialDateHandler());
        handlers.put(ActionType.FORGOT_CODE, new ForgotCodeHandler());
        handlers.put(ActionType.SEAT_CUSTOMER, new SeatCustomerHandler());
        handlers.put(ActionType.GET_ALL_TABLES, new GetAllTablesHandler());
        handlers.put(ActionType.INSERT_TABLE, new InsertTableHandler());
        handlers.put(ActionType.UPDATE_TABLE, new UpdateTableHandler());
        handlers.put(ActionType.DELETE_TABLE, new DeleteTableHandler());
        handlers.put(ActionType.GET_REPORT, new ReportHandler(null));
        handlers.put(ActionType.GET_WAITING_LIST_BETWEEN_DATES, new WeeklyWaitingListHandler(null));
        handlers.put(ActionType.ADD_TO_WAITING_LIST, new AddWaitingHandler());
        handlers.put(ActionType.CANCEL_WAITING, new CancelWaitingHandler());
        handlers.put(ActionType.CREATE_OPENING_HOURS, new CreateOpeningHoursHandler());
        handlers.put(ActionType.REMOVE_OPENING_HOURS, new DeleteOpeningHoursHandler());
        handlers.put(ActionType.MARK_NOTIFIED, new MarkNotifiedHandler());
    }

    /**
     * Sends a message to the server
     */
    private void sendRequest(Message msg) {
        try {
            if (!isConnected()) openConnection();
            sendToServer(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registers a new user
     */
    public void register(RegisterRequest req) {
        connect();
        sendRequest(new Message(ActionType.ADD_USER, req));
    }

    /**
     * Logs in a user with ID and membership code
     */
    public void login(int userID, int membershipCode) {
        connect();
        setCurrentUserId(userID);
        sendRequest(new Message(ActionType.LOGIN, new LoginRequest(userID, membershipCode)));
    }

    /**
     * Fetches user information for a given user ID
     */
    public void getUserInformation(int userId) {
        connect();
        sendRequest(new Message(ActionType.GET_USER_INFORMATION, new GetUserInformationRequest(userId)));
    }

    /**
     * Updates an existing user
     */
    public void updateUser(User user) {
        connect();
        sendRequest(new Message(ActionType.UPDATE_USER, new UpdateUserRequest(user)));
    }

    /**
     * Logs out the current user
     */
    public void logout() {
        sendRequest(new Message(ActionType.LOGOUT, new LogoutRequest()));
    }

    /**
     * Requests all reservations
     */
    public void getAllReservations() {
        connect();
        sendRequest(new Message(ActionType.GET_ALL_RESERVATIONS, new GetAllReservationsRequest()));
    }

    /**
     * Adds a reservation
     */
    public void addReservation(Reservation reservation) {
        connect();
        awaitResponse = true;
        sendRequest(new Message(ActionType.ADD_RESERVATION, new AddReservationRequest(reservation)));
    }

    /**
     * Requests reservations for a specific user
     */
    public void getUserReservations(int userId) {
        connect();
        sendRequest(new Message(ActionType.GET_USER_RESERVATIONS, new GetUserReservationsRequest(userId)));
    }

    /**
     * Requests all users
     */
    public void getAllUsers() {
        sendRequest(new Message(ActionType.GET_ALL_USERS, null));
    }

    /**
     * Cancels a reservation
     */
    public void cancelReservation(Integer reservationId, Integer confirmationCode, Integer guestId) {
        connect();
        sendRequest(new Message(ActionType.CANCEL_RESERVATION, new CancelReservationRequest(reservationId, confirmationCode, guestId)));
    }

    /**
     * Updates a reservation
     */
    public void updateReservation(int id, LocalDate date, LocalTime time, int guests, ReservationStatus status) {
        connect();
        sendRequest(new Message(ActionType.UPDATE_RESERVATION, new UpdateReservationRequest(id, date, time, guests, status)));
    }

    /**
     * Requests available times for a date
     */
    public void getAvailableTimes(LocalDate date, int guests, boolean forWaitingList) {
        connect();
        sendRequest(new Message(ActionType.GET_AVAILABLE_TIMES, new GetAvailableTimesRequest(date, guests, forWaitingList)));
    }

    /**
     * Requests nearest available times for a date
     */
    public void getNearestAvailableTimes(LocalDate date, int guests) {
        connect();
        sendRequest(new Message(ActionType.GET_NEAREST_TIMES, new GetNearestAvailableTimesRequest(date, guests)));
    }

    /**
     * Makes a payment request
     */
    public void Pay(PaymentRequest req) {
        connect();
        sendRequest(new Message(ActionType.PAY, req));
    }

    /**
     * Marks a customer as seated
     */
    public void seatCustomer(int userId, int confirmationCode, LocalTime actualArrival) {
        connect();
        SeatCustomerRequest req = new SeatCustomerRequest(userId, confirmationCode);
        req.setActualArrivalTime(actualArrival);
        sendRequest(new Message(ActionType.SEAT_CUSTOMER, req));
    }

    /**
     * Adds a customer to the waiting list
     */
    public void addWaitingList(Integer userID, String email, String phone, int numOfGuests, LocalDate date, LocalTime time) {
        AddToWaitingListRequest req = new AddToWaitingListRequest(userID, email, phone, numOfGuests, date, time);
        sendRequest(new Message(ActionType.ADD_TO_WAITING_LIST, req));
    }

    /**
     * Cancels a waiting list entry
     */
    public void cancelWaiting(int confirmationCode) {
        int userId = getCurrentUserId();
        CancelWaitingRequest req = new CancelWaitingRequest(confirmationCode, userId);
        sendRequest(new Message(ActionType.CANCEL_WAITING, req));
    }

    /**
     * Requests waiting list for the current week
     */
    public void getCurrentWeekWaitingList() {
        connect();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        GetWaitingListBetweenDatesRequest req = new GetWaitingListBetweenDatesRequest(startDate, endDate);
        sendRequest(new Message(ActionType.GET_WAITING_LIST_BETWEEN_DATES, req));
    }

    /**
     * Requests a forgot code operation for a user
     */
    public void forgotCode(int userId) {
        connect();
        sendRequest(new Message(ActionType.FORGOT_CODE, new ForgotCodeRequest(userId)));
    }

    /**
     * Requests all restaurant tables
     */
    public void getAllTables() {
        sendRequest(new Message(ActionType.GET_ALL_TABLES, null));
    }

    /**
     * Inserts a new table
     */
    public void insertTable(int tableId, int seats) {
        sendRequest(new Message(ActionType.INSERT_TABLE, new InsertTableRequest(tableId, seats)));
    }

    /**
     * Updates a table
     */
    public void updateTable(int tableId, int seats) {
        sendRequest(new Message(ActionType.UPDATE_TABLE, new UpdateTableRequest(tableId, seats)));
    }

    /**
     * Deletes a table
     */
    public void deleteTable(int tableId) {
        sendRequest(new Message(ActionType.DELETE_TABLE, new DeleteTableRequest(tableId)));
    }

    /**
     * Requests a report
     */
    public void requestReport(ReportType reportType, int year, int month, ReportController controller) {
        connect();
        ReportHandler handler = reportHandlers.get(reportType);
        if (handler == null) {
            handler = new ReportHandler(controller);
            reportHandlers.put(reportType, handler);
            handlers.put(ActionType.GET_REPORT, handler);
        } else {
            handler.setController(controller);
            handlers.put(ActionType.GET_REPORT, handler);
        }
        sendRequest(new Message(ActionType.GET_REPORT, new ReportRequest(reportType, month, year)));
    }

    /**
     * Marks a reservation as notified
     */
    public void markReservationAsNotified(int reservationId) {
        sendRequest(new Message(ActionType.MARK_NOTIFIED, reservationId));
    }

    /**
     * Handles messages received from server
     */
    @Override
    protected void handleMessageFromServer(Object msg) {
        if (msg instanceof Message) {
            Message m = (Message) msg;
            ActionType type = m.getAction();
            if (handlers.containsKey(type)) {
                handlers.get(type).handle(m.getData());
            }
            awaitResponse = false;
        }
    }

    /**
     * Returns the active restaurant settings controller
     */
    public RestaurantSettingsController getActiveRestaurantSettingsController() {
        return activeRestaurantSettingsController;
    }

    /**
     * Sets the active restaurant settings controller
     */
    public void setActiveRestaurantSettingsController(RestaurantSettingsController controller) {
        this.activeRestaurantSettingsController = controller;
    }

    /**
     * Requests restaurant settings
     */
    public void getRestaurantSettings() {
        connect();
        sendRequest(new Message(ActionType.GET_RESTAURANT_SETTINGS, new GetRestaurantSettingsRequest()));
    }

    /**
     * Adds a special date
     */
    public void addSpecialDate(SpecialDates specialDate) {
        connect();
        sendRequest(new Message(ActionType.ADD_SPECIAL_DATE, new AddSpecialDateRequest(specialDate)));
    }

    /**
     * Creates regular opening hours
     */
    public void createRegularOpeningHours(WeeklyOpeningHours hours) {
        System.out.println("Sending CREATE_OPENING_HOURS for day: " + hours.getDay() + " " + hours.getOpeningTime() + " - " + hours.getClosingTime());
        connect();
        sendRequest(new Message(ActionType.CREATE_OPENING_HOURS, new CreateRegularOpeningHoursRequest(hours)));
    }

    /**
     * Updates a special date
     */
    public void updateSpecialDate(UpdateSpecialDateRequest req) {
        connect();
        sendRequest(new Message(ActionType.UPDATE_SPECIAL_DATE, req));
    }

    /**
     * Deletes a special date
     */
    public void deleteSpecialDate(LocalDate date) {
        connect();
        sendRequest(new Message(ActionType.DELETE_SPECIAL_DATE, new DeleteSpecialDateRequest(date)));
    }

    /**
     * Updates the regular opening time for a specific day.
     * Sends an update request to the server with the new opening time.
     *
     * @param hours the weekly opening hours object containing the day and opening time
     */
    public void updateRegularOpeningTime(WeeklyOpeningHours hours) {
        connect();
        sendRequest(new Message(
            ActionType.UPDATE_OPENING_TIME,
            new updateRegularOpeningTimeRequest(hours.getDay(), hours.getOpeningTime())
        ));
    }
    
    /**
     * Updates the regular closing time for a specific day.
     * Sends an update request to the server with the new closing time.
     *
     * @param hours the weekly opening hours object containing the day and closing time
     */
    public void updateRegularClosingTime(WeeklyOpeningHours hours) {
        connect();
        sendRequest(new Message(
            ActionType.UPDATE_CLOSING_TIME,
            new updateRegularClosingTimeRequest(hours.getDay(), hours.getClosingTime())
        ));
    }
    
    /**
     * Deletes the regular opening hours for a specific day.
     * Sends a delete request to the server if the provided object is not null.
     *
     * @param wh the weekly opening hours object containing the day to remove
     */
    public void deleteRegularOpeningHours(WeeklyOpeningHours wh) {
        if (wh == null) return;
        try {
            connect();
            DeleteOpeningHoursRequest request =
                    new DeleteOpeningHoursRequest(wh.getDay());
            sendRequest(new Message(ActionType.REMOVE_OPENING_HOURS, request));
            System.out.println(
                "Sent DELETE_OPENING_HOURS request for day: " + wh.getDay()
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Registers or replaces a report handler for a specific report type.
     *
     * @param type the report type
     * @param handler the handler responsible for processing the report response
     */
    public void setHandler(ReportType type, ReportHandler handler) {
        reportHandlers.put(type, handler);
    }
    
    /**
     * Sets the listener for receiving available times updates.
     *
     * @param availableTimesListener the listener to register
     */
    public void setAvailableTimesListener(
            AvailableTimesListener availableTimesListener) {
        this.availableTimesListener = availableTimesListener;
    }
    
    /**
     * Returns the listener used to receive available times updates.
     *
     * @return the available times listener
     */
    public AvailableTimesListener getAvailableTimesListener() {
        return availableTimesListener;
    }

}




    
    
    

    
