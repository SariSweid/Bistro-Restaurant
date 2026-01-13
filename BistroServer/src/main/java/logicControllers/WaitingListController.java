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
import common.ServerResponse;
import enums.ExitReason;
import enums.ReservationStatus;
import enums.WaitingStatus;

public class WaitingListController {
	
	private Random random;
    private final TableDAO tableDAO;
    private final ReservationDAO reservationDAO;
    private final WaitingListDAO waitingListDAO;

    public WaitingListController() {
        this.random = new Random();
        this.tableDAO = new TableDAO();
        this.reservationDAO = new ReservationDAO();
        this.waitingListDAO = new WaitingListDAO();
    }

    /**
     * Add customer to waiting list or create PENDING reservation if table is available.
     */
    public ServerResponse addToWaitingList(Integer userID, String email, String phone,
                                           int numOfGuests, LocalDate date, LocalTime time) {

        // 1. Check table availability
        Table table = tableDAO.getAvailableTableAtTime(numOfGuests, date, time);

        if (table != null) {
            // Table available → create CONFIRMED reservation
            int confirmationCode = generateConfirmationCode();

            Reservation reservation = new Reservation(
                    0,
                    userID,
                    table.getTableID(),
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

            tableDAO.updateTableIsAvailable(table.getTableID(), false);
            scheduleAutoCancel(reservation.getReservationID());

            return new ServerResponse(
                    true,
                    reservation,
                    "Table available! please proceed to table #" + table.getTableID()
            );
        }

        // 2. No table → check if user already has a waiting entry
        WaitingListEntry existing = waitingListDAO.findExistingEntry(userID, date, time);
        if (existing != null && existing.getExitReason() == null) {
            return new ServerResponse(
                    false,
                    null,
                    "You are already on the waiting list for this date and time."
            );
        }

        // 3. Add new waiting list entry
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
                "Added to waiting list. Your confirmation code: " + confirmationCode
        );
    }




    /**
     * Cancels a CONFIRMED reservation after 15 minutes if not SEATED
     */
    private static final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(5);
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
     * Cancel waiting
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

        // Ownership check
        if (entry.getUserID() != null &&
            !entry.getUserID().equals(currentUserId)) {
        		System.out.println(currentUserId);
            return new ServerResponse(
                    false,
                    null,
                    "This confirmation code does not belong to your account."
            );
        }

        boolean updated =
                waitingListDAO.updateExitReason(
                        confirmationCode,
                        ExitReason.CANCELLED
                );

        return new ServerResponse(
                updated,
                null,
                updated
                    ? "Waiting cancelled successfully."
                    : "Cancellation failed."
        );
    }
    
    
    public List<WaitingListEntry> getWaitingListBetweenDates(LocalDate startDate,LocalDate endDate) {
    	


        return waitingListDAO.getWaitingListBetweenDates(startDate, endDate);
    }

    private int generateConfirmationCode() {
        return 100000 + random.nextInt(900000);
    }
}
