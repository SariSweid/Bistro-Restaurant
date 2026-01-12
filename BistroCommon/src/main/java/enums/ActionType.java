package enums;

public enum ActionType {
	// Reservation
    GET_ALL_RESERVATIONS,
    GET_USER_RESERVATIONS,
    ADD_RESERVATION,
    UPDATE_RESERVATION,
    GET_AVAILABLE_TIMES,
    GET_NEAREST_TIMES,
    CANCEL_RESERVATION,
    
    // User
    LOGIN,
    LOGOUT,
    GET_USER,
    GET_USER_INFORMATION,
    GET_ALL_USERS,
    ADD_USER,
    UPDATE_USER,
    CHECKUSERIDEXISTS,
    FORGOT_CODE,
    SEAT_CUSTOMER,
    
    // Restaurant settings
    GET_RESTAURANT_SETTINGS,
    ADD_SPECIAL_DATE,
    UPDATE_SPECIAL_DATE,
    UPDATE_OPENING_TIME,
    UPDATE_CLOSING_TIME,
    CREATE_OPENING_HOURS,
    REMOVE_OPENING_HOURS,    

    DELETE_SPECIAL_DATE,
    

    // Tables
    INSERT_TABLE,
    UPDATE_TABLE,
    DELETE_TABLE,
    GET_ALL_TABLES,
    
    
    // Payment
    PAY,
    
    // Reports
    GET_REPORT,
    
    //Waiting List
    ADD_TO_WAITING_LIST,
    CANCEL_WAITING,
    GET_WAITING_LIST_BETWEEN_DATES
}
