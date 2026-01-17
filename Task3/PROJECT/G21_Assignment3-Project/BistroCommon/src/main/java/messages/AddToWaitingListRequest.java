package messages;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a request to add a user or guest to the restaurant waiting list.
 * 
 * Contains information about the user ID (null for guests), contact details,
 * number of guests, and the desired waiting date and time.
 */
@SuppressWarnings("serial")
public class AddToWaitingListRequest implements Serializable {

    /** The user ID, null if the request is from a guest. */
    private Integer userID;

    /** The user's email address. */
    private String email;

    /** The user's phone number. */
    private String phone;

    /** Number of guests in this request. */
    private int numOfGuests;

    /** The date for which the user is waiting. */
    private LocalDate date;

    /** The time for which the user is waiting. */
    private LocalTime time;

    /**
     * Constructs a new request to add a user or guest to the waiting list.
     *
     * @param userID the ID of the user, null if guest
     * @param email the user's email
     * @param phone the user's phone
     * @param numOfGuests number of guests
     * @param date the date of the reservation
     * @param time the time of the reservation
     */
    public AddToWaitingListRequest(Integer userID, String email, String phone,
                                   int numOfGuests, LocalDate date, LocalTime time) {
        this.userID = userID;
        this.email = email;
        this.phone = phone;
        this.numOfGuests = numOfGuests;
        this.date = date;
        this.time = time;
    }

    /** Returns the user ID, null if a guest. */
    public Integer getUserID() { return userID; }

    /** Returns the email of the user. */
    public String getEmail() { return email; }

    /** Returns the phone number of the user. */
    public String getPhone() { return phone; }

    /** Returns the number of guests in this request. */
    public int getNumOfGuests() { return numOfGuests; }

    /** Returns the date for which the user is waiting. */
    public LocalDate getDate() { return date; }

    /** Returns the time for which the user is waiting. */
    public LocalTime getTime() { return time; }
}
