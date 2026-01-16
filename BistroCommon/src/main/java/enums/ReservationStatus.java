package enums;

/**
 * Represents the status of a reservation in the restaurant system.
 * Each status indicates the current state of a reservation.
 */
public enum ReservationStatus {

    /** Reservation has been made but not yet confirmed. */
    PENDING,

    /** Reservation has been confirmed. */
    CONFIRMED,

    /** Reservation has been cancelled. */
    CANCELLED,

    /** The customer has been seated. */
    SEATED,

    /** The reservation has been completed successfully. */
    COMPLETED,

    /** The customer did not show up for the reservation. */
    NOT_SHOWED,

    /** The reservation is on the waiting list. */
    WAITLIST
}
