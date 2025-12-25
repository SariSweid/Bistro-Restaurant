package logicControllers;

import Entities.Reservation;
import messages.UpdateReservationRequest;
import DB.DBController;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Server-side logic controller for reservations.
 */
public class ReservationController {

    private final DBController db;
    
    // Constructor
    public ReservationController() {
        this.db = new DBController(); // implement DBController to connect to your DB
    }
    
    // Constructor for tests (inject a mock DB)
    public ReservationController(DBController db) {
        this.db = db;
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
//    public boolean updateReservation(int reservationId, LocalDate newDate, int newNumGuests) {
//        // Validation
//        if (reservationId <= 0) return false;
//        if (newDate == null) return false;
//        if (newNumGuests <= 0) return false;
//
//        try {
//        		Date sqlDate = Date.valueOf(newDate);  // conversion
//            return db.updateReservation(reservationId, sqlDate, newNumGuests);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }

    
    /**
     * Add a new reservation. Returns true on success.
     */
   public boolean addReservation(Reservation r) {
       if (r == null) return false;
       // Validation
       if (r.getReservationID() <= 0) return false;
       if (r.getReservationDate() == null) return false;
       if (r.getNumOfGuests() <= 0) return false;

       try {       
    	    	return db.insertReservation(r);
        } catch (Exception e) {
           e.printStackTrace();
           return false;
        }
    }
}
