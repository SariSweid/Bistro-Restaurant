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
 * Controller responsible for managing the restaurant's waiting list and real-time reservation logic.
 * This class implements the decision-making engine that determines whether a guest can be
 * immediately accommodated (based on current table availability) or should be added to the waiting list.
 */
public class WaitingListController {

    private Random random;
    private final TableDAO tableDAO;
    private final ReservationDAO reservationDAO;
    private final WaitingListDAO waitingListDAO;
    private final UserDAO userDAO;

    /**
     * Scheduler for managing time-sensitive tasks, such as auto-cancelling reservations for no-shows.
     */
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    public WaitingListController() {
        this.random = new Random();
        this.tableDAO = new TableDAO();
        this.reservationDAO = new ReservationDAO();
        this.waitingListDAO = new WaitingListDAO();
        this.userDAO = new UserDAO();
    }

    /**
     * Processes a request to join the restaurant at a specific time.
     * 1. Validates group size against maximum table capacity.
     * 2. Checks current availability using a greedy table-assignment algorithm.
     * 3. Confirms a reservation if space is available; otherwise, adds the guest to the waiting list.
     *
     * @param userID      The ID of the registered user (null for walk-in guests).
     * @param email       Contact email for notifications.
     * @param phone       Contact phone for notifications.
     * @param numOfGuests Size of the dining party.
     * @param date        The desired dining date.
     * @param time        The desired dining time.
     * @return A ServerResponse containing the resulting Reservation or WaitingListEntry.
     */
    public ServerResponse addToWaitingList(Integer userID, String email, String phone,
            int numOfGuests, LocalDate date, LocalTime time) {

        // Handle guest user registration if not logged in
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

        // Capacity check
        List<Table> tables = tableDAO.GetAllTables();
        int maxTableCapacity = tables.stream().mapToInt(Table::getCapacity).max().orElse(0);

        if (numOfGuests > maxTableCapacity) {
            return new ServerResponse(false, null,
                    "Cannot accommodate group of this size. Max table size is " + maxTableCapacity);
        }

        // Availability check
        boolean tableAvailable = canFitReservation(date, time, numOfGuests);

        if (tableAvailable) {
            int confirmationCode = generateConfirmationCode();

            Reservation reservation = new Reservation(
                    0, userID, null, null, numOfGuests, confirmationCode,
                    date, time, LocalDate.now(), LocalTime.now(),
                    ReservationStatus.CONFIRMED, true
                    );

            if (!reservationDAO.insertReservation(reservation)) {
                return new ServerResponse(false, null, "Failed to create reservation");
            }

            // Start the 15-minute arrival timer
            scheduleAutoCancel(reservation.getReservationID());

            return new ServerResponse(true, reservation,
                    "Table available! Your reservation is confirmed. Your confirmation code is: " + confirmationCode);
        }

        // Duplicate entry check
        WaitingListEntry existing = waitingListDAO.findExistingEntry(userID, date, time);
        if (existing == null && (email != null || phone != null)) {
            existing = waitingListDAO.findExistingEntryByContact(email, phone, date, time);
        }

        if (existing != null && existing.getExitReason() == null) {
            return new ServerResponse(false, null,
                    "You are already on the waiting list for this date and time.");
        }

        // Add to waiting list if no immediate table is found
        int confirmationCode = generateConfirmationCode();
        WaitingListEntry entry = new WaitingListEntry(
            userID, email, phone, numOfGuests, confirmationCode, date, time, null, WaitingStatus.WAITING
        );

        boolean success = waitingListDAO.addToWitingList(entry);
        if (!success) {
            return new ServerResponse(false, null, "Failed to add to waiting list");
        }

        return new ServerResponse(true, entry,
            "Added to waiting list. Your confirmation code: " + entry.getConfirmationCode());
    }

    /**
     * Internal algorithm to determine if a new group can fit in the restaurant.
     * Uses a 'Greedy' approach: sorts groups and tables by size and attempts to 
     * find a valid seating configuration for all active reservations plus the new request.
     *
     * @return true if a configuration exists where everyone has a table.
     */
    private boolean canFitReservation(LocalDate date, LocalTime time, int newGroupSize) {
        // Prepare tables (sorted by capacity descending)
        List<Table> tables = tableDAO.GetAllTables()
                .stream()
                .map(t -> new Table(t.getTableID(), t.getCapacity())) 
                .sorted((a, b) -> b.getCapacity() - a.getCapacity()) 
                .toList();

        // Get existing active reservations
        List<Reservation> reservations = reservationDAO.getReservationsAt(date, time);

        List<Integer> groups = new ArrayList<>();
        for (Reservation r : reservations) {
            groups.add(r.getNumOfGuests());
        }
        groups.add(newGroupSize);
        groups.sort((a, b) -> b - a);

        // Greedy assignment algorithm
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
     * Schedules an automatic task to cancel a reservation if the guest 
     * fails to check in within 15 minutes of their scheduled time.
     *
     * @param reservationID The ID of the reservation to monitor.
     */
    private void scheduleAutoCancel(int reservationID) {
        scheduler.schedule(() -> {
            try {
                Reservation r = reservationDAO.GetReservation(reservationID);
                // Only cancel if the status hasn't changed to SEATED or COMPLETED
                if (r != null && r.getStatus() == ReservationStatus.CONFIRMED) {
                    reservationDAO.cancelReservationInDB(reservationID, false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 15, TimeUnit.MINUTES);
    }

    /**
     * Removes a guest from the waiting list manually upon their request.
     *
     * @param confirmationCode The unique code provided at registration.
     * @param currentUserId    The ID of the user requesting cancellation (for security validation).
     * @return ServerResponse indicating if the cancellation was processed.
     */
    public ServerResponse cancelWaiting(int confirmationCode, Integer currentUserId) {
        WaitingListEntry entry = waitingListDAO.getByConfirmationCode(confirmationCode);

        if (entry == null) {
            return new ServerResponse(false, null, "Invalid confirmation code.");
        }

        if (entry.getExitReason() != null) {
            return new ServerResponse(false, null, "This waiting request was already closed.");
        }

        // Security check: ensure the request belongs to the user
        if (currentUserId != null && entry.getUserID() != null && !entry.getUserID().equals(currentUserId)) {
            return new ServerResponse(false, null, "This confirmation code does not belong to your account.");
        }

        boolean updated = waitingListDAO.updateExitReason(confirmationCode, ExitReason.CANCELLED);
        return new ServerResponse(updated, null, updated ? "Waiting cancelled successfully." : "Cancellation failed.");
    }

    /**
     * Retrieves historical waiting list data for reporting and analytics.
     */
    public List<WaitingListEntry> getWaitingListBetweenDates(LocalDate startDate, LocalDate endDate) {
        return waitingListDAO.getWaitingListBetweenDates(startDate, endDate);
    }

    /**
     * Generates a 6-digit confirmation code and ensures it is unique within the current waiting list.
     */
    private int generateConfirmationCode() {
        int code;
        do {
            code = 100000 + random.nextInt(900000);
        } while (waitingListDAO.getByConfirmationCode(code) != null);
        return code;
    }
}