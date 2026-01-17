package Entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

import enums.ExitReason;
import enums.WaitingStatus;

/**
 * Represents an entry in the restaurant waiting list.
 * 
 * Contains information about the user or guest, contact details, 
 * number of guests, confirmation code, waiting date and time, 
 * exit reason, and current waiting status.
 */
@SuppressWarnings("serial")
public class WaitingListEntry implements Serializable {

    /** The user ID, null if the entry belongs to a guest. */
    private Integer userID;

    /** Email of the user or phone as a fallback. */
    private String Email;

    /** Phone number of the user or guest. */
    private String phone;

    /** Number of guests in this reservation. */
    private int numOfGuests;

    /** Confirmation code for this entry. */
    private int confirmationCode;

    /** Date the user is waiting for. */
    private LocalDate WaitDate;

    /** Time the user is waiting for. */
    private LocalTime WaitTime;

    /** Reason why the customer left the waiting list. */
    private ExitReason exitReason;

    /** Current waiting status (WAITING, NOTIFIED, SEATED). */
    private WaitingStatus status;

    /**
     * Constructs a waiting list entry with all fields.
     *
     * @param userID the ID of the user, null for guests
     * @param email the user's email
     * @param phone the user's phone
     * @param numOfGuests number of guests
     * @param confirmationCode the confirmation code
     * @param waitDate the date the user is waiting for
     * @param waitTime the time the user is waiting for
     * @param exitReason reason why the customer left
     * @param status current waiting status
     */
    public WaitingListEntry(Integer userID, String email, String phone, int numOfGuests, int confirmationCode,
                            LocalDate waitDate, LocalTime waitTime, ExitReason exitReason, WaitingStatus status) {
        this.userID = userID;
        Email = email;
        this.phone = phone;
        this.numOfGuests = numOfGuests;
        this.confirmationCode = confirmationCode;
        WaitDate = waitDate;
        WaitTime = waitTime;
        this.exitReason = exitReason;
        this.status = status;
    }

    /**
     * Constructs a waiting list entry without specifying the status.
     *
     * @param userID the ID of the user, null for guests
     * @param email the user's email
     * @param phone the user's phone
     * @param numOfGuests number of guests
     * @param confirmationCode the confirmation code
     * @param waitDate the date the user is waiting for
     * @param waitTime the time the user is waiting for
     * @param exitReason reason why the customer left
     */
    public WaitingListEntry(Integer userID, String email, String phone, int numOfGuests, int confirmationCode,
                            LocalDate waitDate, LocalTime waitTime, ExitReason exitReason) {
        this(userID, email, phone, numOfGuests, confirmationCode, waitDate, waitTime, exitReason, null);
    }

    /** Returns the user ID. */
    public Integer getUserID() { return userID; }

    /** Sets the user ID. */
    public void setUserID(Integer userID) { this.userID = userID; }

    /** Returns the email of the user. */
    public String getEmail() { return Email; }

    /** Sets the email of the user. */
    public void setEmail(String email) { Email = email; }

    /** Returns the phone number of the user. */
    public String getPhone() { return phone; }

    /** Sets the phone number of the user. */
    public void setPhone(String phone) { this.phone = phone; }

    /** Returns the date the user is waiting for. */
    public LocalDate getWaitDate() { return WaitDate; }

    /** Sets the date the user is waiting for. */
    public void setWaitDate(LocalDate waitDate) { WaitDate = waitDate; }

    /** Returns the time the user is waiting for. */
    public LocalTime getWaitTime() { return WaitTime; }

    /** Sets the time the user is waiting for. */
    public void setWaitTime(LocalTime waitTime) { WaitTime = waitTime; }

    /** Returns the number of guests. */
    public int getNumOfGuests() { return this.numOfGuests; }

    /** Sets the number of guests. */
    public void setNumOfGuests(int numOfGuests) { this.numOfGuests = numOfGuests; }

    /** Returns the confirmation code. */
    public int getConfirmationCode() { return this.confirmationCode; }

    /** Sets the confirmation code. */
    public void setConfirmationCode(int confirmationCode) { this.confirmationCode = confirmationCode; }

    /** Returns the exit reason. */
    public ExitReason getExitReason() { return this.exitReason; }

    /** Sets the exit reason. */
    public void setExitReason(ExitReason exitReason) { this.exitReason = exitReason; }

    /**
     * Marks the entry as exited for the given reason.
     *
     * @param exitReason the reason the customer left
     */
    public void exit(ExitReason exitReason) { this.exitReason = exitReason; }

    /** Returns the current waiting status. */
    public WaitingStatus getStatus() { return status; }

    /** Sets the current waiting status. */
    public void setStatus(WaitingStatus status) { this.status = status; }
}
