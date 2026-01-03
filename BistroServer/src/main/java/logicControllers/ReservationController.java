package logicControllers;

import Entities.Reservation;
import messages.UpdateReservationRequest;
import DB.DBController;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Server-side logic controller for reservations.
 */
public class ReservationController {
	
	private static final int MAX_CAPACITY = 50;
    private static final LocalTime OPEN_TIME = LocalTime.of(10, 0);
    private static final LocalTime CLOSE_TIME = LocalTime.of(22, 0);
    
    private final DBController db;
    
    // Constructor
    public ReservationController() {
        this.db = new DBController(); // implement DBController to connect to your DB
    }
    


    // ======================
    // GET AVAILABLE TIMES
    // ======================
    public List<LocalTime> getAvailableTimes(LocalDate date, int guests) {
    		List<LocalTime> available = new ArrayList<>();
        List<Reservation> all = getAllReservations();
        
        LocalTime time = OPEN_TIME;

        while (!time.isAfter(CLOSE_TIME)) {
            int usedSeats = 0;

            for (Reservation r : all) {
                if (r.getReservationDate().equals(date)
                        && r.getReservationTime().equals(time)) {
                    usedSeats += r.getNumOfGuests();
                }
            }

            if (usedSeats + guests <= MAX_CAPACITY) {
                available.add(time);
            }

            time = time.plusMinutes(30);
        }

        return available;
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

        existing.setReservationDate(ur.getReservationDate());
        existing.setReservationTime(ur.getReservationTime());
        existing.setNumOfGuests(ur.getNumOfGuests());
        existing.setStatus(enums.ReservationStatus.valueOf(ur.getStatus().name()));

        return db.updateReservation(existing);
    }

    
    /**
     * Add a new reservation. Returns true on success.
     */
   public boolean addReservation(Reservation r) {
       if (r == null) return false;
       // Validation
       if (r.getReservationDate() == null) return false;
       if (r.getReservationTime() == null) return false;
       if (r.getNumOfGuests() <= 0) return false;

       // check availability
       List<LocalTime> available =
               getAvailableTimes(r.getReservationDate(), r.getNumOfGuests());

       if (!available.contains(r.getReservationTime())) {
           return false;
       }
       
       // generate confirmation code
       r.setConfirmationCode(generateConfirmationCode());
       r.setStatus(enums.ReservationStatus.CONFIRMED);
       
       return db.insertReservation(r);
   }
   
   
   private int generateConfirmationCode() {
       return 100000 + new java.util.Random().nextInt(900000);
   }
   
}
