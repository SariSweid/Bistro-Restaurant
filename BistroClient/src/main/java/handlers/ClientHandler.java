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

public class ClientHandler extends AbstractClient {

    public boolean awaitResponse = false;
    private HashMap<ActionType, ResponseHandler> handlers;
    private HashMap<ReportType, ReportHandler> reportHandlers = new HashMap<>();
    public static ClientHandler instance;
    private GuestUpdateReservationUI guestUI;
    private MainMenuController mainMenuController;
    private boolean cameFromHigherRole = false;
    private RestaurantSettingsController activeRestaurantSettingsController;
    private UserRole currentuserrole;
    private int currentUserId;
    private boolean connected = false;
    private User currentUser;
    private TablesController tablesController;
    private List<Reservation> allReservations;
    private AvailableTimesListener availableTimesListener;
    private BaseReservationController activeReservationController;

    public BaseReservationController getActiveReservationController() {
		return activeReservationController;
	}

	public void setActiveReservationController(BaseReservationController activeReservationController) {
		this.activeReservationController = activeReservationController;
	}

	private ClientHandler(String host, int port) throws IOException {
        super(host, port);
        handlers = new HashMap<>();
        initializeHandlers();
    }

    //used for all classes other than MainMenuApp
    public static ClientHandler getClient() {
        if (instance == null) {
        	return getClient("localhost");
        }
        return instance;
    }
    
    //used only in MainMenuApp class
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

    public void setCurrentUser(User user) {
        this.currentUser = user;
        this.currentUserId = user.getUserId();
        this.currentuserrole = user.getRole();
    }

    public void setCurrentUserId(int id) {
        this.currentUserId = id;
    }

    public void setCurrentUserRole(UserRole role) {
        this.currentuserrole = role;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public UserRole getCurrentUserRole() {
        return currentuserrole;
    }

    public int getCurrentUserId() {
        return currentUserId;
    }

    public void setCameFromHigherRole(boolean value) {
        this.cameFromHigherRole = value;
    }

    public boolean cameFromHigherRole() {
        return cameFromHigherRole;
    }

    public void setMainMenuController(MainMenuController controller) {
        this.mainMenuController = controller;
    }

    public MainMenuController getMainMenuController() {
        return mainMenuController;
    }



    private BaseDisplayController activeDisplayController;

    public void setActiveDisplayController(BaseDisplayController controller) {
        this.activeDisplayController = controller;
    }

    public BaseDisplayController getActiveDisplayController() {
        return activeDisplayController;
    }

    public void setGuestUI(GuestUpdateReservationUI guestUI) {
        this.guestUI = guestUI;
    }

    public void setHandler(ActionType type, ResponseHandler handler) {
        handlers.put(type, handler);
    }

    public void setTablesController(TablesController controller) {
        this.tablesController = controller;
    }

    public TablesController getTablesController() {
        return tablesController;
    }

    public List<Reservation> getAllReservationsList() {
        return allReservations;
    }

    public void setAllReservationsList(List<Reservation> list) {
        this.allReservations = list;
    }

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
    
    }

    private void sendRequest(Message msg) {
        try {
            if (!isConnected()) openConnection();
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

    public void getUserInformation(int userId) {
        connect();
        sendRequest(new Message(ActionType.GET_USER_INFORMATION, new GetUserInformationRequest(userId)));
    }

    public void updateUser(User user) {
        connect();
        sendRequest(new Message(ActionType.UPDATE_USER, new UpdateUserRequest(user)));
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

    public void getAllUsers() {
        sendRequest(new Message(ActionType.GET_ALL_USERS, null));
    }

    public void cancelReservation(Integer reservationId, Integer confirmationCode, Integer guestId) {
        connect();
        sendRequest(new Message(ActionType.CANCEL_RESERVATION, new CancelReservationRequest(reservationId, confirmationCode, guestId)));
    }

    public void updateReservation(int id, LocalDate date, LocalTime time, int guests, ReservationStatus status) {
        connect();
        sendRequest(new Message(ActionType.UPDATE_RESERVATION, new UpdateReservationRequest(id, date, time, guests, status)));
    }

    public void getAvailableTimes(LocalDate date, int guests, boolean forWaitingList) {
        connect();
        sendRequest(new Message(ActionType.GET_AVAILABLE_TIMES, new GetAvailableTimesRequest(date, guests, forWaitingList)));
    }

    public void getNearestAvailableTimes(LocalDate date, int guests) {
        connect();
        sendRequest(new Message(ActionType.GET_NEAREST_TIMES, new GetNearestAvailableTimesRequest(date, guests)));
    }

    public void Pay(PaymentRequest req) {
        connect();
        sendRequest(new Message(ActionType.PAY, req));
    }

    public void seatCustomer(int userId, int confirmationCode, LocalTime actualArrival) {
        connect();
        SeatCustomerRequest req = new SeatCustomerRequest(userId, confirmationCode);
        req.setActualArrivalTime(actualArrival);
        sendRequest(new Message(ActionType.SEAT_CUSTOMER, req));
    }

    public void addWaitingList(Integer userID, String email, String phone, int numOfGuests, LocalDate date, LocalTime time) {
        //connect();
        AddToWaitingListRequest req = new AddToWaitingListRequest(userID, email, phone, numOfGuests, date, time);
        sendRequest(new Message(ActionType.ADD_TO_WAITING_LIST, req));
    }

    public void cancelWaiting(int confirmationCode) {
        //connect();
    	int userId = getCurrentUserId();
    	if (userId == 0) { // it is a guest
    		
    	}
        CancelWaitingRequest req = new CancelWaitingRequest(confirmationCode, userId);
        sendRequest(new Message(ActionType.CANCEL_WAITING, req));
    }

    public void getCurrentWeekWaitingList() {
        connect();
        LocalDate today = LocalDate.now();
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        GetWaitingListBetweenDatesRequest req = new GetWaitingListBetweenDatesRequest(startDate, endDate);
        sendRequest(new Message(ActionType.GET_WAITING_LIST_BETWEEN_DATES, req));
    }

    public void forgotCode(int userId) {
        connect();
        sendRequest(new Message(ActionType.FORGOT_CODE, new ForgotCodeRequest(userId)));
    }

    public void getAllTables() {
        sendRequest(new Message(ActionType.GET_ALL_TABLES, null));
    }

    public void insertTable(int tableId, int seats) {
        sendRequest(new Message(ActionType.INSERT_TABLE, new InsertTableRequest(tableId, seats)));
    }

    public void updateTable(int tableId, int seats) {
        sendRequest(new Message(ActionType.UPDATE_TABLE, new UpdateTableRequest(tableId, seats)));
    }

    public void deleteTable(int tableId) {
        sendRequest(new Message(ActionType.DELETE_TABLE, new DeleteTableRequest(tableId)));
    }

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

    public RestaurantSettingsController getActiveRestaurantSettingsController() {
        return activeRestaurantSettingsController;
    }

    public void setActiveRestaurantSettingsController(RestaurantSettingsController controller) {
        this.activeRestaurantSettingsController = controller;
    }

    public void getRestaurantSettings() {
        connect();
        sendRequest(new Message(ActionType.GET_RESTAURANT_SETTINGS, new GetRestaurantSettingsRequest()));
    }

    public void addSpecialDate(SpecialDates specialDate) {
        connect();
        sendRequest(new Message(ActionType.ADD_SPECIAL_DATE, new AddSpecialDateRequest(specialDate)));
    }
    
    public void createRegularOpeningHours(WeeklyOpeningHours hours) {
    	System.out.println("Sending CREATE_OPENING_HOURS for day: " + hours.getDay() + " " + hours.getOpeningTime() + " - " + hours.getClosingTime());

        connect();
        sendRequest(new Message(ActionType.CREATE_OPENING_HOURS, new CreateRegularOpeningHoursRequest(hours)));
    }

    public void updateSpecialDate(UpdateSpecialDateRequest req) {
        connect();
        sendRequest(new Message(ActionType.UPDATE_SPECIAL_DATE, req));
    }
    
    public void deleteSpecialDate(LocalDate date) {
    	connect();
    	sendRequest(new Message(ActionType.DELETE_SPECIAL_DATE, new DeleteSpecialDateRequest(date)));
    }
    
    public void updateRegularOpeningTime(WeeklyOpeningHours hours) {
        connect();
        sendRequest(new Message(ActionType.UPDATE_OPENING_TIME, new updateRegularOpeningTimeRequest(hours.getDay(), hours.getOpeningTime())));
    }

    public void updateRegularClosingTime(WeeklyOpeningHours hours) {
        connect();
        sendRequest(new Message(ActionType.UPDATE_CLOSING_TIME, new updateRegularClosingTimeRequest(hours.getDay(), hours.getClosingTime())));
    }
    
    public void deleteRegularOpeningHours(WeeklyOpeningHours wh) {
        if (wh == null) return;
        try {
            connect(); 
            DeleteOpeningHoursRequest request = new DeleteOpeningHoursRequest(wh.getDay());
            sendRequest(new Message(ActionType.REMOVE_OPENING_HOURS, request));
            System.out.println("Sent DELETE_OPENING_HOURS request for day: " + wh.getDay());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setHandler(ReportType type, ReportHandler handler) {
        reportHandlers.put(type, handler);
    }

	public AvailableTimesListener getAvailableTimesListener() {
		return availableTimesListener;
	}

	public void setAvailableTimesListener(AvailableTimesListener availableTimesListener) {
		this.availableTimesListener = availableTimesListener;
	}
}
