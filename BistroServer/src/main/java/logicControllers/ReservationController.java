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

public class ReservationController {


	private final ReservationDAO resdb;
	private final TableDAO tabledb;
	private final RestaurantSettings settings;

	public ReservationController() {
	    this.resdb = new ReservationDAO();
	    this.tabledb = new TableDAO();
	    this.settings = RestaurantSettings.getInstance();

	    // Load settings from DB into the singleton
	    logicControllers.RestaurantSettingsController sc = new logicControllers.RestaurantSettingsController();
	    sc.getAllWeeklyOpeningHours();
	    sc.getAllSpecialDates();
	}


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
		    	int tablesThatFit = (int) tables.stream()
		    	        .filter(t -> t.getCapacity() >= guests)
		    	        .count();
	
		    	int overlapping = resdb.countOverlappingReservations(
		    	        date,
		    	        time,
		    	        reservationDuration,
		    	        guests
		    	);
	
		    	if (overlapping < tablesThatFit) {
		    	    available.add(time);
		    	}

	        time = time.plusMinutes(30);
	    }

	    return available;
	}
	

	/**
	 * Returns a list of available times for the waiting list on a given date for a number of guests.
	 * Takes into account special dates, weekly hours, today's current time, and reservation duration.
	 *
	 * @param date The date to get available times for.
	 * @param guests Number of guests.
	 * @return List of available LocalTime objects. Empty list if no times are available.
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
	



    public List<AvailableDateTimes> getNearestAvailableDates(LocalDate requestedDate, int guests) {
        List<AvailableDateTimes> result = new ArrayList<>();
        for (int i = 1; i <= 2; i++) {
            LocalDate dateToCheck = requestedDate.plusDays(i);
            List<LocalTime> times = getAvailableTimes(dateToCheck, guests);
            if (!times.isEmpty()) result.add(new AvailableDateTimes(dateToCheck, times));
        }
        return result;
    }

    public List<Reservation> getAllReservations() {
        return resdb.readAllReservations();
    }

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

    public boolean updateReservationFull(Reservation reservation) {
        if (reservation == null || reservation.getReservationID() <= 0) return false;
        return resdb.updateReservation(reservation);
    }

    public boolean addReservation(Reservation r) {
        if (r == null || r.getNumOfGuests() <= 0 || r.getReservationDate() == null || r.getReservationTime() == null)
            return false;

        List<Table> tables = tabledb.GetAllTables();
        int maxTableCapacity = tables.stream().mapToInt(Table::getCapacity).max().orElse(0);
        if (r.getNumOfGuests() > maxTableCapacity) return false;

        int tablesThatFit = (int) tables.stream()
                .filter(t -> t.getCapacity() >= r.getNumOfGuests())
                .count();

        int overlapping = resdb.countOverlappingReservations(
                r.getReservationDate(),
                r.getReservationTime(),
                settings.getReservationDurationHours(),
                r.getNumOfGuests()
        );

        if (overlapping >= tablesThatFit)
            return false;


        r.setTableID(null);
        r.setConfirmationCode(generateConfirmationCode());
        r.setStatus(ReservationStatus.CONFIRMED);

        return resdb.insertReservation(r);
    }

    private int generateConfirmationCode() {
        return 100000 + new java.util.Random().nextInt(900000);
    }

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

        boolean cancelled = resdb.cancelReservationInDB(r.getReservationID());

        if (cancelled) {
            tabledb.updateTableIsAvailable(r.getTableID(), true);
        }

        return cancelled;
    }


    public Reservation getReservationByCode(int code) {
        List<Reservation> all = resdb.readAllReservations();
        for (Reservation r : all) if (r.getConfirmationCode() == code) return r;
        return null;
    }

    public List<Reservation> getReservationsByCustomer(int customerId) {
        return resdb.getReservationsByCustomer(customerId);
    }

    public boolean hasConfirmedReservation(int userId) {
        List<Reservation> all = resdb.readAllReservations();
        return all.stream().anyMatch(r -> r.getCustomerId() == userId && r.getStatus() == ReservationStatus.CONFIRMED);
    }

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

    public ServerResponse seatCustomerByCode(int confirmationCode, int userId, LocalTime actualArrivalTime) {
        Reservation r = getReservationByCode(confirmationCode);
        if (r == null) return new ServerResponse(false, null, "Confirmation code is incorrect.");
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
}
