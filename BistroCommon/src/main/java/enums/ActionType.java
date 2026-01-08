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
    
    // Tables
    INSERT_TABLE,
    UPDATE_TABLE,
    DELETE_TABLE,
    GET_ALL_TABLES,
    
    
    // Payment
    PAY,
    
    // Reports
    GET_REPORT
}
