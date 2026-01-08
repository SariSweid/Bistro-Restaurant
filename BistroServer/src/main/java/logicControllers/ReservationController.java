package logicControllers;

import Entities.Reservation;
import Entities.Table;
import Entities.User;
import common.ServerResponse;
import enums.ReservationStatus;
import enums.UserRole;
import messages.AvailableDateTimes;
import messages.UpdateReservationRequest;
import DB.DBController;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    

    public List<LocalTime> getAvailableTimes(LocalDate date, int guests) {

        List<LocalTime> available = new ArrayList<>();
        List<Table> tables = db.GetAllTables();

        // 1) Find largest table capacity
        int maxTableCapacity = tables.stream()
                                     .mapToInt(Table::getCapacity)
                                     .max()
                                     .orElse(0);

        // If group is too large → no times available
        if (guests > maxTableCapacity) {
            return Collections.emptyList();
        }

        // 2) Total restaurant capacity
        int totalCapacity = tables.stream()
                                  .mapToInt(Table::getCapacity)
                                  .sum();

        LocalTime time = OPEN_TIME;

        // 3) If reservation is for TODAY → start from now + 1 hour
        if (date.equals(LocalDate.now())) {
            LocalTime oneHourFromNow = LocalTime.now().plusHours(1).withSecond(0).withNano(0);

            if (oneHourFromNow.isAfter(time)) {
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

        // 4) Check each 30-minute slot
        while (!time.isAfter(CLOSE_TIME.minusHours(RESERVATION_DURATION))) {

            if (hasCapacity(date, time, guests, totalCapacity)) {
                available.add(time);
            }

            time = time.plusMinutes(30);
        }

        return available;
    }


    private boolean hasCapacity(LocalDate date, LocalTime time, int guests, int totalCapacity) {

        // Get all reservations on that date, time
        List<Reservation> reservations = db.getReservationsAt(date, time);

        int usedSeats = 0;

        for (Reservation r : reservations) {

            if (!r.isReservationActive()) continue; // only count active ones

            LocalTime rStart = r.getReservationTime();
            LocalTime rEnd = rStart.plusHours(RESERVATION_DURATION);
            LocalTime newEnd = time.plusHours(RESERVATION_DURATION);

            boolean overlap = !(time.isAfter(rEnd) || newEnd.isBefore(rStart));

            if (overlap) {
                usedSeats += r.getNumOfGuests();
            }
        }

        return (usedSeats + guests) <= totalCapacity;
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

        if (r == null || r.getNumOfGuests() <= 0 ||
            r.getReservationDate() == null || r.getReservationTime() == null)
            return false;

        List<Table> tables = db.GetAllTables();

        // Largest table capacity
        int maxTableCapacity = tables.stream()
                                     .mapToInt(Table::getCapacity)
                                     .max()
                                     .orElse(0);

        // Reject if group too large
        if (r.getNumOfGuests() > maxTableCapacity) {
            return false;
        }

        // Total capacity
        int totalCapacity = tables.stream()
                                  .mapToInt(Table::getCapacity)
                                  .sum();

        // Check capacity
        boolean hasCapacity = hasCapacity(
                r.getReservationDate(),
                r.getReservationTime(),
                r.getNumOfGuests(),
                totalCapacity
        );

        if (!hasCapacity) return false;

        // No table assignment here
        r.setTableID(null);

        // Confirmation code
        r.setConfirmationCode(generateConfirmationCode());
        r.setStatus(ReservationStatus.CONFIRMED);

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

	   // Prevent double cancellation
	   if (r.getStatus() == enums.ReservationStatus.CANCELLED) {
	       System.out.println("Reservation already cancelled.");
	       return false;
	   }

       // Actually cancel the reservation
       r.setStatus(enums.ReservationStatus.CANCELLED);
       boolean updated = db.cancelReservationInDB(r.getReservationID());
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

   

   /**
    * Checks if the user has a CONFIRMED reservation.
    *
    * @param userId
    * @return true if user has a confirmed reservation
    */
   public boolean hasConfirmedReservation(int userId) {

       List<Reservation> all = db.readAllReservations();

       for (Reservation r : all) {
           if (r.getCustomerId() == userId &&
               r.getStatus() == enums.ReservationStatus.CONFIRMED) {
               return true;
           }
       }

       return false;
   }
   

   
   private Table findFreeTable(int groupSize) {

	    List<Table> tables = db.GetAllTables();
	    List<Reservation> all = db.readAllReservations();

	    // Filter tables that can fit the group
	    List<Table> suitableTables = tables.stream()
	            .filter(t -> t.getCapacity() >= groupSize)
	            .collect(Collectors.toList());

	    // Sort by capacity ASCENDING (smallest table first)
	    suitableTables.sort(Comparator.comparingInt(Table::getCapacity));

	    for (Table t : suitableTables) {

	        boolean taken = false;

	        for (Reservation r : all) {
	            if (r.getTableID() != null &&
	                r.getTableID() == t.getTableID() &&
	                r.getStatus() == ReservationStatus.SEATED) {

	                taken = true;
	                break;
	            }
	        }

	        if (!taken) {
	            return t; // best-fit free table found
	        }
	    }

	    return null; // no suitable table available
	}




   
   public ServerResponse seatCustomerByCode(int confirmationCode, int userId) {

	    Reservation r = getReservationByCode(confirmationCode);
	    if (r == null) {
	        return new ServerResponse(false, null, "Confirmation code is incorrect.");
	    }

	    /*
	    if (r.getCustomerId() != userId) {
	        return new ServerResponse(false, null, "This confirmation code does not belong to your account.");
	    }
*/
	    if (r.getStatus() == ReservationStatus.CANCELLED) {
	        return new ServerResponse(false, null, "This reservation has been cancelled.");
	    }

	    if (r.getStatus() == ReservationStatus.SEATED) {
	        return new ServerResponse(true, r.getTableID(),
	                "You are already seated at");
	    }

	    LocalDate today = LocalDate.now();

	    if (r.getReservationDate().isAfter(today)) {
	        return new ServerResponse(false, null, "Your reservation time has not arrived yet.");
	    }

	    // Find a free table now
	    Table freeTable = findFreeTable(r.getNumOfGuests());

	    if (freeTable != null) {

	        r.setTableID(freeTable.getTableID());
	        r.setStatus(ReservationStatus.SEATED);
	        db.updateReservation(r);

	        return new ServerResponse(
	                true,
	                freeTable.getTableID(),
	                "Your table is ready! Please proceed to"
	        );
	    }

	    // No table available → wait
	    return new ServerResponse(
	            false,
	            null,
	            "All tables are currently occupied. Please wait nearby — we will notify you as soon as a table becomes available."
	    );
	}



   
}
