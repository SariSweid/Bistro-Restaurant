package enums;

/**
 * Represents the status of a customer in the waiting list.
 * 
 * WAITING: the customer is waiting to be seated.
 * NOTIFIED: the customer has been notified that a table is ready.
 * SEATED: the customer has been seated at a table.
 * CANCELLED: the customer cancelled the reservation or waiting request.
 * EXPIRED: the customer's waiting time expired before being seated.
 */
public enum WaitingStatus {
    WAITING,
    NOTIFIED,
    SEATED,
    CANCELLED,
    EXPIRED
}
