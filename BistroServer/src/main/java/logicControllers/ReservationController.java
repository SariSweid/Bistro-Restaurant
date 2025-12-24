package logicControllers;

import Entities.Reservation;
import messages.UpdateReservationRequest;
import DB.DBController;
import java.sql.Date;
import java.util.List;
import java.util.Objects;

/**
 * Server-side logic controller for reservations.
 */
public class ReservationController {

    private final DBController db;

    public ReservationController() {
        this.db = new DBController(); // implement DBController to connect to your DB
    }

    /**
     * Return all reservations from DB.
     */
    public List<Reservation> getAllReservations() {
        return db.readAllReservations();
    }

    /**
     * Update a reservation's date and guest count.
     * Returns true if update succeeded, false otherwise.
     */

    
    
    public boolean updateReservation(UpdateReservationRequest ur) {
        if (ur == null) return false;
        if (ur.getReservationID() <= 0 || ur.getReservationDate() == null
            || ur.getReservationTime() == null || ur.getNumOfGuests() <= 0
            || ur.getStatus() == null) return false;

        Reservation existing = db.GetReservation(ur.getReservationID());
        if (existing == null) return false;

        // עדכון שדות מותרים בלבד
        existing.setReservationDate(ur.getReservationDate());
        existing.setReservationTime(ur.getReservationTime());
        existing.setNumOfGuests(ur.getNumOfGuests());
        existing.setStatus(Entities.Reservation.Status.valueOf(ur.getStatus().name()));

        return db.updateReservation(existing);
    }


    
     // dont need it for prototype , we will use it later.
    /**
     * Add a new reservation. Returns true on success.
     */
    public boolean addReservation(Reservation r) {
        if (!isValidReservation(r)) return false;

        try {
            return db.insertReservation(r);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

   
   
   private boolean isValidReservation(Reservation r) {
	    if (r == null) return false;

	    if (r.getReservationID() <= 0) return false;
	    if (r.getCustomerId() <= 0) return false;

	    if (r.getNumOfGuests() <= 0) return false;
	    if (r.getConfirmationCode() <= 0) return false;

	    if (r.getReservationDate() == null) return false;
	    if (r.getReservationTime() == null) return false;

	    if (r.getReservationPlacedDate() == null) return false;
	    if (r.getReservationPlacedTime() == null) return false;

	    if (r.getStatus() == null) return false;

	    return true;
	}

   
   
   
   
   
   
   
   
   
}
