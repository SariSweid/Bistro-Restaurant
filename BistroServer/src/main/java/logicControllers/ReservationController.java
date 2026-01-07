package logicControllers;

import Entities.Reservation;
import Entities.Table;
import Entities.User;
import enums.UserRole;
import messages.AvailableDateTimes;
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
	
    private static final LocalTime OPEN_TIME = LocalTime.of(10, 00);  // From 10:00
    private static final LocalTime CLOSE_TIME = LocalTime.of(22, 00);  // To 22:00
    private static final int RESERVATION_DURATION = 2;  // Hours
    
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

        // If reservation is for TODAY → start from now + 1 hour
        if (date.equals(LocalDate.now())) {
            LocalTime oneHourFromNow = LocalTime.now().plusHours(1).withSecond(0).withNano(0);

            if (oneHourFromNow.isAfter(time)) {
                // round up to next 30-minute slot
                int minute = oneHourFromNow.getMinute();
                if (minute > 0 && minute <= 30) {
                    oneHourFromNow = oneHourFromNow.withMinute(30);
                } else if (minute > 30) {
                    oneHourFromNow = oneHourFromNow.plusHours(1).withMinute(0);
                } else {
                    oneHourFromNow = oneHourFromNow.withMinute(0);
                }

                time = oneHourFromNow;
            }
        }
        
        while (!time.isAfter(CLOSE_TIME.minusHours(RESERVATION_DURATION))) {

	        	if (findAvailableTable(date, time, guests, tables) != null) {
	                available.add(time);
	            }
	        	
	        		time = time.plusMinutes(30);
        }

        return available;
    }
    
    
    // ======================
    // Return alternative available times
    // ======================
    public List<AvailableDateTimes> getNearestAvailableDates(LocalDate requestedDate, int guests) {

        List<AvailableDateTimes> result = new ArrayList<>();

        for (int i = 1; i <= 2; i++) {
            LocalDate dateToCheck = requestedDate.plusDays(i);

            List<LocalTime> times =
                getAvailableTimes(dateToCheck, guests);

            if (!times.isEmpty()) {
            		result.add(new AvailableDateTimes(dateToCheck, times)); 
            }
        }
        return result;
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
    	                LocalTime rEnd = rStart.plusHours(RESERVATION_DURATION); // End time of existing reservation
    	                LocalTime newEnd = time.plusHours(RESERVATION_DURATION); // End time of the new reservation

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
   
   
   /**
    * Cancel a reservation based on user role.
    * Subscriber: by reservationID
    * Guest: by confirmationCode, must match customerID
    */
   public boolean cancelReservation(User user, Integer reservationId, Integer confirmationCode, Integer guestId) {
       Reservation r = null;

       if (user != null && user.getRole() == UserRole.SUBSCRIBER) {
    	   		System.out.println(user);
           // Subscribers cancel by reservationID
           r = db.GetReservation(reservationId);
           if (r == null || r.getCustomerId() != user.getUserId()) {
        	   System.out.println("confirmationCode1 = " + confirmationCode);
        	   System.out.println("guestId1 = " + guestId);
        	   System.out.println("user1 = " + user);
        	   return false;
           }

       } else if (guestId != null) {
           // Guests cancel by confirmationCode + guestId
           r = getReservationByCode(confirmationCode);
           if (r == null || r.getCustomerId() != guestId) {
        	   System.out.println("confirmationCode2 = " + confirmationCode);
        	   System.out.println("guestId2 = " + guestId);
        	   System.out.println("user2 = " + user);
        	   return false;
           }

       } else if (confirmationCode != null) {  
    	    // Guests cancel by confirmationCode only
    	    r = getReservationByCode(confirmationCode);
    	    if (r == null) return false;

    	} else {
    	    // Other roles cannot cancel
    	    return false;
    	}
       
	   System.out.println("confirmationCode2 = " + confirmationCode);
	   System.out.println("guestId2 = " + guestId);
	   System.out.println("user2 = " + user);

	   // NEW CHECK: prevent double cancellation
	   if (r.getStatus() == enums.ReservationStatus.CANCELLED) {
	       System.out.println("Reservation already cancelled.");
	       return false;
	   }

       // Actually cancel the reservation
       r.setStatus(enums.ReservationStatus.CANCELLED);
       boolean updated = db.updateReservation(r);
       System.out.println("DB update result = " + updated);
       return updated;
   }

   // Method to find by confirmation code
   public Reservation getReservationByCode(int code) {
       List<Reservation> all = db.readAllReservations();
       for (Reservation r : all) {
           if (r.getConfirmationCode() == code) return r;
       }
       return null;
   }
   
   
   public List<Reservation> getReservationsByCustomer(int customerId) {
	    return db.getReservationsByCustomer(customerId);
	}

   
}
