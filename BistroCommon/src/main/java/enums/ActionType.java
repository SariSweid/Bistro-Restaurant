package enums;

/**
 * Represents the different types of actions that can be performed
 * in the restaurant management system. 
 * Actions are categorized by their functionality such as reservations,
 * users, restaurant settings, tables, payments, reports, and waiting list management.
 */
public enum ActionType {

    // Reservation actions
    GET_ALL_RESERVATIONS,
    GET_USER_RESERVATIONS,
    ADD_RESERVATION,
    UPDATE_RESERVATION,
    GET_AVAILABLE_TIMES,
    GET_NEAREST_TIMES,
    CANCEL_RESERVATION,
    MARK_NOTIFIED,

    // User actions
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

    // Restaurant settings actions
    GET_RESTAURANT_SETTINGS,
    ADD_SPECIAL_DATE,
    UPDATE_SPECIAL_DATE,
    UPDATE_OPENING_TIME,
    UPDATE_CLOSING_TIME,
    CREATE_OPENING_HOURS,
    REMOVE_OPENING_HOURS,
    DELETE_SPECIAL_DATE,

    // Table actions
    INSERT_TABLE,
    UPDATE_TABLE,
    DELETE_TABLE,
    GET_ALL_TABLES,
    RESERVATION_AFFECTED_BY_TABLE,

    // Payment actions
    PAY,

    // Report actions
    GET_REPORT,

    // Waiting list actions
    ADD_TO_WAITING_LIST,
    CANCEL_WAITING,
    GET_WAITING_LIST_BETWEEN_DATES
}
