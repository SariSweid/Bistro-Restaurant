package logicControllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import DAO.ReservationDAO;
import DAO.TableDAO;
import DAO.WaitingListDAO;
import Entities.Reservation;
import Entities.Table;
import Entities.WaitingListEntry;
import Entities.WeeklyOpeningHours;
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

    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);

    public WaitingListController() {
        this.random = new Random();
        this.tableDAO = new TableDAO();
        this.reservationDAO = new ReservationDAO();
        this.waitingListDAO = new WaitingListDAO();
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

        List<Table> tables = tableDAO.GetAllTables();

        int maxTableCapacity = tables.stream()
                                     .mapToInt(Table::getCapacity)
                                     .max()
                                     .orElse(0);

        if (numOfGuests > maxTableCapacity) {
            return new ServerResponse(
                    false,
                    null,
                    "Cannot accommodate group of this size. Max table size is " + maxTableCapacity
            );
        }

        int tablesThatFit = (int) tables.stream()
                                        .filter(t -> t.getCapacity() >= numOfGuests)
                                        .count();

        int overlapping = reservationDAO.countOverlappingReservations(
                date,
                time,
                2,
                numOfGuests
        );

        boolean tableAvailable = overlapping < tablesThatFit;

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

            return new ServerResponse(
                    true,
                    reservation,
                    "Table available! Your reservation is confirmed. Your confirmation code is: " + confirmationCode
            );
        }

        WaitingListEntry existing = waitingListDAO.findExistingEntry(userID, date, time);
        if (existing != null && existing.getExitReason() == null) {
            return new ServerResponse(
                    false,
                    null,
                    "You are already on the waiting list for this date and time."
            );
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

        return new ServerResponse(
                true,
                entry,
                "Added to waiting list. Your confirmation code: " + entry.getConfirmationCode()
        );
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
                        tableDAO.updateTableIsAvailable(r.getTableID(), true);
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
     * Generates a random 6-digit confirmation code.
     *
     * @return Confirmation code
     */
    private int generateConfirmationCode() {
        return 100000 + random.nextInt(900000);
    }


}
