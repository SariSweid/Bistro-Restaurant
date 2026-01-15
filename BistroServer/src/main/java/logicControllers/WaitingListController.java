package logicControllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import DAO.ReservationDAO;
import DAO.TableDAO;
import DAO.UserDAO;
import DAO.WaitingListDAO;
import Entities.Guest;
import Entities.Reservation;
import Entities.Table;
import enums.UserRole;
import Entities.WaitingListEntry;
import common.ServerResponse;
import enums.ExitReason;
import enums.ReservationStatus;
import enums.WaitingStatus;

/**
 * Controller for handling waiting list and reservation logic.
 * Decides if a guest can get a reservation or should be added to the waiting list.
 */
public class WaitingListController {

    private Random random;
    private final TableDAO tableDAO;
    private final ReservationDAO reservationDAO;
    private final WaitingListDAO waitingListDAO;
    private final UserDAO userDAO;

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    public WaitingListController() {
        this.random = new Random();
        this.tableDAO = new TableDAO();
        this.reservationDAO = new ReservationDAO();
        this.waitingListDAO = new WaitingListDAO();
        this.userDAO = new UserDAO();
    }

    /**
     * Adds a user to the waiting list or creates a reservation if a table is available.
     *
     * @param userID      User ID
     * @param email       User email
     * @param phone       User phone number
     * @param numOfGuests Number of guests
     * @param date        Desired date
     * @param time        Desired time
     * @return ServerResponse containing status, reservation/waiting list entry, and a message
     */
    public ServerResponse addToWaitingList(Integer userID, String email, String phone,
            int numOfGuests, LocalDate date, LocalTime time) {

        if (userID == null) {
        	Guest guest = new Guest(0, email, phone);
            guest.setEmail(email);
            guest.setPhone(phone);
            guest.setRole(UserRole.GUEST);
            userID = userDAO.insertGuestAndReturnId2(guest);
            if (userID == -1) {
                return new ServerResponse(false, null, "Failed to create guest user.");
            }
        }

    	List<Table> tables = tableDAO.GetAllTables();
    	tables.sort(Comparator.comparingInt(Table::getCapacity));
    	int maxTableCapacity = tables.stream().mapToInt(Table::getCapacity).max().orElse(0);

    	if (numOfGuests > maxTableCapacity) {
    		return new ServerResponse(false, null,
    				"Cannot accommodate group of this size. Max table size is " + maxTableCapacity);
    	}

    	List<Reservation> reservations = reservationDAO.getConfirmedReservationsAt(date, time);
    	reservations.sort((a, b) -> Integer.compare(b.getNumOfGuests(), a.getNumOfGuests()));
    	boolean tableAvailable = canFitReservation(date, time, numOfGuests);

    	if (tableAvailable) {
    		int confirmationCode = generateConfirmationCode();

    		Reservation reservation = new Reservation(
    				0,
    				userID,
    				null,
    				null,
    				numOfGuests,
    				confirmationCode,
    				date,
    				time,
    				LocalDate.now(),
    				LocalTime.now(),
    				ReservationStatus.CONFIRMED
    				);

    		if (!reservationDAO.insertReservation(reservation)) {
    			return new ServerResponse(false, null, "Failed to create reservation");
    		}

    		scheduleAutoCancel(reservation.getReservationID());

    		return new ServerResponse(true, reservation,
    				"Table available! Your reservation is confirmed. Your confirmation code is: " + confirmationCode);
    	}

    	WaitingListEntry existing = waitingListDAO.findExistingEntry(userID, date, time);
    	if (existing == null && (email != null || phone != null)) {
    		existing = waitingListDAO.findExistingEntryByContact(email, phone, date, time);
    	}

    	if (existing != null && existing.getExitReason() == null) {
    		return new ServerResponse(false, null,
    				"You are already on the waiting list for this date and time.");
    	}

    	int confirmationCode = generateConfirmationCode();

	WaitingListEntry entry = new WaitingListEntry(
		userID,
	email,
	phone,
	numOfGuests,
	confirmationCode,
	date,
	time,
	null,
	WaitingStatus.WAITING
		);

	boolean success = waitingListDAO.addToWitingList(entry);
	if (!success) {
		return new ServerResponse(false, null, "Failed to add to waiting list");
	}

	return new ServerResponse(true, entry,
		"Added to waiting list. Your confirmation code: " + entry.getConfirmationCode());
    }
    
    private boolean canFitReservation(
            LocalDate date,
            LocalTime time,
            int newGroupSize
    ) {
        // 1. Get all tables
        List<Table> tables = tableDAO.GetAllTables()
                .stream()
                .map(t -> new Table(t.getTableID(), t.getCapacity())) // copy
                .sorted((a, b) -> b.getCapacity() - a.getCapacity()) 
                .toList();

        // Get existing reservations at this slot
        List<Reservation> reservations =
                reservationDAO.getReservationsAt(date, time);

        // Add the new request
        List<Integer> groups = new ArrayList<>();
        for (Reservation r : reservations) {
            groups.add(r.getNumOfGuests());
        }
        groups.add(newGroupSize);

        // Sort groups DESC
        groups.sort((a, b) -> b - a);

        // Greedy assignment
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

            if (!placed) return false; // cannot fit
        }

        return true; // all fit
    }


    /**
     * Schedules automatic cancellation of a reservation after 15 minutes if the user does not arrive.
     *
     * @param reservationID Reservation ID
     */
    private void scheduleAutoCancel(int reservationID) {
        scheduler.schedule(() -> {
            try {
                Reservation r = reservationDAO.GetReservation(reservationID);
                if (r != null && r.getStatus() == ReservationStatus.CONFIRMED) {
                    boolean cancelled = reservationDAO.cancelReservationInDB(reservationID);
                    if (cancelled) {
                        System.out.println("Reservation " + reservationID +
                                           " cancelled automatically (user didn't arrive).");
                    } else {
                        System.err.println("Failed to cancel reservation " + reservationID + " in DB.");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 15, TimeUnit.MINUTES);
    }

    /**
     * Cancels a waiting list entry based on the confirmation code.
     *
     * @param confirmationCode Confirmation code
     * @param currentUserId    Current user ID
     * @return ServerResponse indicating success or failure
     */
    public ServerResponse cancelWaiting(int confirmationCode, Integer currentUserId) {

        WaitingListEntry entry = waitingListDAO.getByConfirmationCode(confirmationCode);

        if (entry == null) {
            return new ServerResponse(
                    false,
                    null,
                    "Invalid confirmation code."
            );
        }

        if (entry.getExitReason() != null) {
            return new ServerResponse(
                    false,
                    null,
                    "This waiting request was already closed."
            );
        }

        if (currentUserId != null) {
            if (entry.getUserID() != null &&
                !entry.getUserID().equals(currentUserId)) {
                return new ServerResponse(
                        false,
                        null,
                        "This confirmation code does not belong to your account."
                );
            }
        }

        boolean updated = waitingListDAO.updateExitReason(confirmationCode, ExitReason.CANCELLED);

        return new ServerResponse(
                updated,
                null,
                updated ? "Waiting cancelled successfully." : "Cancellation failed."
        );
    }

    /**
     * Returns the waiting list entries between two dates.
     *
     * @param startDate Start date
     * @param endDate   End date
     * @return List of WaitingListEntry
     */
    public List<WaitingListEntry> getWaitingListBetweenDates(LocalDate startDate, LocalDate endDate) {
        return waitingListDAO.getWaitingListBetweenDates(startDate, endDate);
    }

    /**
     * Generates a unique random 6-digit confirmation code.
     *
     * @return Confirmation code
     */
    private int generateConfirmationCode() {
        int code;
        do {
            code = 100000 + random.nextInt(900000);
        } while (waitingListDAO.getByConfirmationCode(code) != null);
        return code;
    }


}
