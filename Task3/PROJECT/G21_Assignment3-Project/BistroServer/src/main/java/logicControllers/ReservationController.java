package logicControllers;

import Entities.Reservation;
import Entities.RestaurantSettings;
import Entities.Table;
import Entities.User;
import Entities.WeeklyOpeningHours;
import common.ServerResponse;
import enums.ReservationStatus;
import enums.UserRole;
import messages.AvailableDateTimes;
import messages.UpdateReservationRequest;
import DAO.ReservationDAO;
import DAO.TableDAO;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller class responsible for handling the logic of restaurant reservations.
 * This includes checking for availability, managing the seating process, 
 * calculating available time slots, and processing cancellations.
 */
public class ReservationController {

    /** Data access object for reservation-related database operations. */
	private final ReservationDAO resdb;
	
    /** Data access object for table-related database operations. */
	private final TableDAO tabledb;
	
    /** Singleton instance containing the current restaurant configuration. */
	private final RestaurantSettings settings;

	/**
	 * Initializes the ReservationController, instantiates the DAOs, 
	 * and ensures that restaurant settings (opening hours and special dates) 
	 * are loaded from the database into the singleton instance.
	 */
	public ReservationController() {
	    this.resdb = new ReservationDAO();
	    this.tabledb = new TableDAO();
	    this.settings = RestaurantSettings.getInstance();

	    // Load settings from DB into the singleton
	    logicControllers.RestaurantSettingsController sc = new logicControllers.RestaurantSettingsController();
	    sc.getAllWeeklyOpeningHours();
	    sc.getAllSpecialDates();
	}

	/**
	 * Retrieves a list of available reservation times for a specific date and number of guests.
	 * Considers opening hours, current time (for same-day bookings), and table capacity.
	 * * @param date The date to check for availability.
	 * @param guests The number of guests for the reservation.
	 * @return A list of LocalTime objects representing available slots in 30-minute intervals.
	 */
	public List<LocalTime> getAvailableTimes(LocalDate date, int guests) {

	    List<LocalTime> available = new ArrayList<>();
	    List<Table> tables = tabledb.GetAllTables();

	    WeeklyOpeningHours hours = settings.getOpeningHoursForDate(date);

	    if (hours == null) {
	        return Collections.emptyList();
	    }

	    LocalTime openTime = hours.getOpeningTime();
	    LocalTime closeTime = hours.getClosingTime();
	    int reservationDuration = settings.getReservationDurationHours();

	    int maxTableCapacity = tables.stream()
	                                 .mapToInt(Table::getCapacity)
	                                 .max()
	                                 .orElse(0);

	    if (guests > maxTableCapacity)
	        return Collections.emptyList();

	    LocalTime time = openTime;

	    if (date.equals(LocalDate.now())) {
	        LocalTime oneHourFromNow = LocalTime.now().plusHours(1).withSecond(0).withNano(0);

	       // Enforce "at least 1 hour from now" rule
	        if (oneHourFromNow.isAfter(time)) {
	            int minute = oneHourFromNow.getMinute();
	            if (minute > 0 && minute <= 30)
	                oneHourFromNow = oneHourFromNow.withMinute(30);
	            else if (minute > 30)
	                oneHourFromNow = oneHourFromNow.plusHours(1).withMinute(0);
	            else
	                oneHourFromNow = oneHourFromNow.withMinute(0);

	            time = oneHourFromNow;
	        }
	    }

	    LocalTime lastStart = closeTime.minusHours(reservationDuration);

	    while (!time.isAfter(lastStart)) {
	        if (canFitReservation(date, time, guests, reservationDuration)) {
	            available.add(time);
	        }
	        time = time.plusMinutes(30);
	    }
	    
	    if (available.isEmpty()) {
	        time = openTime;
	        if (date.equals(LocalDate.now())) {
	            LocalTime oneHourFromNow = LocalTime.now().plusHours(1).withSecond(0).withNano(0);
	            int minute = oneHourFromNow.getMinute();
	            if (minute > 0 && minute <= 30) oneHourFromNow = oneHourFromNow.withMinute(30);
	            else if (minute > 30) oneHourFromNow = oneHourFromNow.plusHours(1).withMinute(0);
	            else oneHourFromNow = oneHourFromNow.withMinute(0);

	            if (oneHourFromNow.isAfter(time)) time = oneHourFromNow;
	        }

	        while (!time.isAfter(lastStart)) {
	            if (canFitReservation(date, time, guests, reservationDuration)) {
	                available.add(time); // only the first slot
	                break;
	            }
	            time = time.plusMinutes(30);
	        }
	    }

	    return available;
	}

	/**
	 * Returns a list of all possible time slots for the waiting list on a given date.
	 * Unlike standard availability, this does not check if the restaurant is full,
	 * but only if the time falls within operating hours.
	 *
	 * @param date The date to check.
	 * @param guests Number of guests.
	 * @return List of LocalTime objects within operating hours.
	 */
	public List<LocalTime> getAllTimesForWaitingList(LocalDate date, int guests) {
	    List<LocalTime> times = new ArrayList<>();
	    List<Table> tables = tabledb.GetAllTables();

	    int maxTableCapacity = tables.stream().mapToInt(Table::getCapacity).max().orElse(0);
	    if (guests > maxTableCapacity) return Collections.emptyList();

	    WeeklyOpeningHours hours = settings.getOpeningHoursForDate(date);
	    if (hours == null) return Collections.emptyList();

	    LocalTime openTime = hours.getOpeningTime();
	    LocalTime closeTime = hours.getClosingTime();
	    int reservationDuration = settings.getReservationDurationHours();

	    LocalTime lastStart = closeTime.minusHours(reservationDuration);
	    LocalTime time = openTime;

	    if (date.equals(LocalDate.now())) {
	        LocalTime nowPlusOneHour = LocalTime.now().plusHours(1).withSecond(0).withNano(0);

	        int minute = nowPlusOneHour.getMinute();
	        if (minute > 0 && minute <= 30) nowPlusOneHour = nowPlusOneHour.withMinute(30);
	        else if (minute > 30) nowPlusOneHour = nowPlusOneHour.plusHours(1).withMinute(0);
	        else nowPlusOneHour = nowPlusOneHour.withMinute(0);

	        if (nowPlusOneHour.isAfter(openTime)) {
	            time = nowPlusOneHour;
	        }
	    }

	    if (time.isAfter(lastStart)) return Collections.emptyList();

	    while (!time.isAfter(lastStart)) {
	        times.add(time);
	        time = time.plusMinutes(30);
	    }

	    return times;
	}

    /**
     * Checks availability for the two days following the requested date.
     * * @param requestedDate The date the user originally wanted.
     * @param guests The number of guests.
     * @return A list of AvailableDateTimes objects for the next two available days.
     */
    public List<AvailableDateTimes> getNearestAvailableDates(LocalDate requestedDate, int guests) {
        List<AvailableDateTimes> result = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            LocalDate dateToCheck = requestedDate.plusDays(i);
            List<LocalTime> times = getAvailableTimes(dateToCheck, guests);
            if (!times.isEmpty()) result.add(new AvailableDateTimes(dateToCheck, times));
        }
        return result;
    }

    /**
     * Retrieves all reservations from the database.
     * @return A list of all Reservation entities.
     */
    public List<Reservation> getAllReservations() {
        return resdb.readAllReservations();
    }

    /**
     * Updates an existing reservation based on an update request.
     * * @param ur The update request containing new reservation details.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateReservation(UpdateReservationRequest ur) {
        if (ur == null || ur.getReservationID() <= 0 || ur.getReservationDate() == null
                || ur.getReservationTime() == null || ur.getNumOfGuests() <= 0 || ur.getStatus() == null)
            return false;

        Reservation existing = resdb.GetReservation(ur.getReservationID());
        if (existing == null) return false;

        existing.setReservationDate(ur.getReservationDate());
        existing.setReservationTime(ur.getReservationTime());
        existing.setNumOfGuests(ur.getNumOfGuests());
        existing.setStatus(ReservationStatus.valueOf(ur.getStatus().name()));

        return resdb.updateReservation(existing);
    }

    /**
     * Updates a complete reservation entity in the database.
     * * @param reservation The reservation object to update.
     * @return true if successful.
     */
    public boolean updateReservationFull(Reservation reservation) {
        if (reservation == null || reservation.getReservationID() <= 0) return false;
        return resdb.updateReservation(reservation);
    }

    /**
     * Adds a new reservation to the system. 
     * Validates capacity and time slot availability before confirming.
     * * @param r The reservation entity to add.
     * @return true if the reservation was successfully placed.
     */
    public boolean addReservation(Reservation r) {
        if (r == null || r.getNumOfGuests() <= 0 || r.getReservationDate() == null || r.getReservationTime() == null)
            return false;

        List<Table> tables = tabledb.GetAllTables();
        int maxTableCapacity = tables.stream().mapToInt(Table::getCapacity).max().orElse(0);
        if (r.getNumOfGuests() > maxTableCapacity) return false;

        int reservationDuration = settings.getReservationDurationHours();
        if (!canFitReservation(r.getReservationDate(), r.getReservationTime(), r.getNumOfGuests(), reservationDuration)) {
            return false; 
        }

        r.setTableID(null);
        r.setConfirmationCode(generateConfirmationCode());
        r.setStatus(ReservationStatus.CONFIRMED);

        return resdb.insertReservation(r);
    }

    /**
     * Internal logic to determine if a new group can be seated at a specific time.
     * Simulates the seating of all overlapping reservations to ensure a table is available.
     * * @param date The date of the reservation.
     * @param time The start time of the reservation.
     * @param newGroupSize Number of guests in the new group.
     * @param durationHours The expected duration of the reservation.
     * @return true if the group can be accommodated, false if the restaurant is over-capacity.
     */
    private boolean canFitReservation(LocalDate date, LocalTime time, int newGroupSize, int durationHours) {
        List<Table> tables = tabledb.GetAllTables()
                .stream()
                .map(t -> new Table(t.getTableID(), t.getCapacity())) 
                .sorted((a, b) -> b.getCapacity() - a.getCapacity())
                .toList();

        List<Reservation> reservations = resdb.getReservationsByDate(date);

        List<Reservation> overlapping = new ArrayList<>();
        LocalTime newEnd = time.plusHours(durationHours);
        for (Reservation r : reservations) {
            LocalTime resStart = r.getReservationTime();
            LocalTime resEnd = resStart.plusHours(settings.getReservationDurationHours());

            if (resStart.isBefore(newEnd) && resEnd.isAfter(time)) {
                overlapping.add(r);
            }
        }

        List<Integer> groups = new ArrayList<>();
        for (Reservation r : overlapping) groups.add(r.getNumOfGuests());
        groups.add(newGroupSize);

        groups.sort((a, b) -> b - a);

        boolean[] used = new boolean[tables.size()];
        for (int groupSize : groups) {
            boolean placed = false;
            for (int i = 0; i < tables.size(); i++) {
                if (!used[i] && tables.get(i).getCapacity() >= groupSize) {
                    used[i] = true;
                    placed = true;
                    break;
                }
            }
            if (!placed) return false;
        }

        return true;
    }

    /**
     * Generates a random 6-digit confirmation code for a reservation.
     * @return An integer between 100000 and 999999.
     */
    private int generateConfirmationCode() {
        return 100000 + new java.util.Random().nextInt(900000);
    }

    /**
     * Cancels a reservation based on user role or confirmation details.
     * * @param user The user attempting the cancellation (Staff/Subscriber).
     * @param reservationId The ID of the reservation (used for staff).
     * @param confirmationCode The code of the reservation (used for guests).
     * @param guestId Optional guest ID.
     * @return true if the cancellation was successful.
     */
    public boolean cancelReservation(User user, Integer reservationId, Integer confirmationCode, Integer guestId) {
        Reservation r = null;

        if (user != null &&
                (user.getRole() == UserRole.SUBSCRIBER ||
                 user.getRole() == UserRole.SUPERVISOR ||
                 user.getRole() == UserRole.MANAGER)) {

            r = resdb.GetReservation(reservationId);
            if (r == null) return false;

        } else if (guestId != null) {

            r = getReservationByCode(confirmationCode);
            if (r == null) return false;

        } else if (confirmationCode != null) {

            r = getReservationByCode(confirmationCode);
            if (r == null) return false;

        } else return false;

        if (r.getStatus() == ReservationStatus.CANCELLED) return false;

        r.setStatus(ReservationStatus.CANCELLED);

        boolean cancelled = resdb.cancelReservationInDB(r.getReservationID(), false);

        if (cancelled) {
            tabledb.updateTableIsAvailable(r.getTableID(), true);
        }

        return cancelled;
    }

    /**
     * Finds a specific reservation by its confirmation code.
     * @param code The 6-digit confirmation code.
     * @return The Reservation object if found, otherwise null.
     */
    public Reservation getReservationByCode(int code) {
        List<Reservation> all = resdb.readAllReservations();
        for (Reservation r : all) if (r.getConfirmationCode() == code) return r;
        return null;
    }

    /**
     * Retrieves all reservations made by a specific customer.
     * @param customerId The ID of the customer.
     * @return A list of their reservations.
     */
    public List<Reservation> getReservationsByCustomer(int customerId) {
        return resdb.getReservationsByCustomer(customerId);
    }

    /**
     * Checks if a user has at least one active confirmed reservation.
     * @param userId The customer ID.
     * @return true if a confirmed reservation exists.
     */
    public boolean hasConfirmedReservation(int userId) {
        List<Reservation> all = resdb.readAllReservations();
        return all.stream().anyMatch(r -> r.getCustomerID() == userId && r.getStatus() == ReservationStatus.CONFIRMED);
    }

    /**
     * Finds a currently free table that fits the group size. 
     * Used during the walk-in or arrival seating process.
     * * @param groupSize The number of people to seat.
     * @return A suitable Table object if available, otherwise null.
     */
    private Table findFreeTable(int groupSize) {
        List<Table> tables = tabledb.GetAllTables();
        List<Reservation> all = resdb.readAllReservations();

        List<Table> suitableTables = tables.stream()
                .filter(t -> t.getCapacity() >= groupSize)
                .sorted(Comparator.comparingInt(Table::getCapacity))
                .collect(Collectors.toList());

        for (Table t : suitableTables) {
            boolean taken = all.stream()
                    .anyMatch(r -> r.getTableID() != null && r.getTableID() == t.getTableID() && r.getStatus() == ReservationStatus.SEATED);
            if (!taken) return t;
        }
        return null;
    }

    /**
     * Processes the seating of a customer when they arrive at the restaurant.
     * Validates the confirmation code, checks for system cancellations, and assigns a physical table.
     * * @param confirmationCode The code provided by the customer.
     * @param userId The ID of the user.
     * @param actualArrivalTime The time the customer actually arrived.
     * @return A ServerResponse containing the success status and relevant messages or table ID.
     */
    public ServerResponse seatCustomerByCode(int confirmationCode, int userId, LocalTime actualArrivalTime) {
        Reservation r = getReservationByCode(confirmationCode);
        if (r == null) return new ServerResponse(false, null, "Confirmation code is incorrect.");
        if (r.getStatus() == ReservationStatus.CANCELLED && !r.isNotified()) {
            return new ServerResponse(false, r, "This reservation was cancelled by the system.");
        }
        if (r.getStatus() == ReservationStatus.CANCELLED)
            return new ServerResponse(false, null, "This reservation has been cancelled.");
        if (r.getStatus() == ReservationStatus.SEATED)
            return new ServerResponse(true, r.getTableID(), "You are already seated at");

        LocalDate today = LocalDate.now();
        if (r.getReservationDate().isAfter(today))
            return new ServerResponse(false, null, "Your reservation time has not arrived yet.");

        Table freeTable = findFreeTable(r.getNumOfGuests());
        if (freeTable != null) {
            r.setTableID(freeTable.getTableID());
            tabledb.updateTableIsAvailable(freeTable.getTableID(), false);
            r.setStatus(ReservationStatus.SEATED);
            r.setActualArrivalTime(actualArrivalTime);
            resdb.updateReservation(r);
            return new ServerResponse(true, freeTable.getTableID(), "Your table is ready! Please proceed to");
        }

        return new ServerResponse(false, null, "All tables are currently occupied. Please wait nearby — we will notify you as soon as a table becomes available.");
    }
    
    /**
     * Marks a reservation as 'notified' in the database, typically after a system-initiated cancellation.
     * * @param reservationID The ID of the reservation to update.
     * @return true if the database update was successful.
     */
    public boolean markReservationAsNotified(int reservationID) {
        return resdb.markAsNotified(reservationID);
    }
    
}