package logicControllers;

import Entities.Reservation;
import Entities.Table;
import messages.UpdateReservationRequest;
import DB.DBController;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Server-side logic controller for reservations.
 */
public class ReservationController {
	
    private static final LocalTime OPEN_TIME = LocalTime.of(10, 0);  // From 10:00
    private static final LocalTime CLOSE_TIME = LocalTime.of(22, 0);  // To 22:00
    private static final int RESERVATION_DURATION = 30;  // Minutes
    
    private final DBController db;
    
    // Constructor
    public ReservationController() {
        this.db = new DBController(); // implement DBController to connect to your DB
    }
    


    // ======================
    // return all available times
    // ======================
    public List<LocalTime> getAvailableTimes(LocalDate date, int guests) {
    	
    		List<LocalTime> available = new ArrayList<>();
    		List<Table> tables = db.GetAllTables();
    		
        LocalTime time = OPEN_TIME;

        while (!time.isAfter(CLOSE_TIME.minusMinutes(RESERVATION_DURATION))) {

	        	if (findAvailableTable(date, time, guests, tables) != null) {
	                available.add(time);
	            }
	        	
	        		time = time.plusMinutes(30);
        }

        return available;
    }
    
    // ======================
    // Find a table for given time
    // ======================
    private Integer findAvailableTable(LocalDate date, LocalTime time, int guests, List<Table> tables) {
    	
        // Get all reservations at that specific date
    		List<Reservation> reservationsAtTime =db.getReservationsAt(date, time);

    		for (Table table : tables) {

    	        if (table.getCapacity() < guests) continue;

    	        boolean taken = false;

    	        for (Reservation r : reservationsAtTime) {
    	        		if (r.getTableID() != null && r.getTableID().equals(table.getTableID())) {
    	                // Check overlap
    	                LocalTime rStart = r.getReservationTime();
    	                LocalTime rEnd = rStart.plusMinutes(RESERVATION_DURATION); // End time of existing reservation
    	                LocalTime newEnd = time.plusMinutes(RESERVATION_DURATION); // End time of the new reservation

    	                // If the new reservation starts before existing ends AND
    	                // ends after existing starts, then it overlaps
    	                if (!(time.isAfter(rEnd) || newEnd.isBefore(rStart))) {
    	                    taken = true;
    	                    break;
    	                }
    	        		}
    	        }

    	        if (!taken) {
    	            return table.getTableID(); // FOUND A TABLE
    	        }
    	    }

    	    return null; // NO TABLE AVAILABLE
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
	   
       // Validation
       if (r == null || r.getNumOfGuests() <= 0 || r.getReservationDate() == null
               || r.getReservationTime() == null) return false;
       
       List<Table> tables = db.GetAllTables();
       
       // check availability
       Integer tableId = findAvailableTable(
               r.getReservationDate(),
               r.getReservationTime(),
               r.getNumOfGuests(),
               tables
       );
       
       if (tableId == null) {
           return false; // no table available
       }
       
       r.setTableID(tableId);
       // generate confirmation code
       r.setConfirmationCode(generateConfirmationCode());
       r.setStatus(enums.ReservationStatus.CONFIRMED);
       
       return db.insertReservation(r);
   }
   
   
   private int generateConfirmationCode() {
       return 100000 + new java.util.Random().nextInt(900000);
   }
   
}
