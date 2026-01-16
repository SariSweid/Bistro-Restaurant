package logicControllers;

import DAO.BillDAO;
import DAO.ReservationDAO;
import DAO.TableDAO;
import DAO.WaitingListDAO;
import Entities.Bill;
import Entities.Reservation;
import Entities.Table;
import Entities.WaitingListEntry;
import enums.ExitReason;
import enums.ReservationStatus;
import util.PaymentResult;

import java.time.LocalDateTime;
import java.util.List;

/**
 * A background watchdog controller that implements Runnable to perform periodic system tasks.
 * It automates the lifecycle of reservations and waiting lists, including marking no-shows,
 * seating customers from the waiting list when tables become free, and auto-generating bills
 * for completed dining sessions.
 */
public class DailyFunctionController implements Runnable {

    /** The interval at which the controller performs its checks (set to 1 minute). */
    private static final int CHECK_INTERVAL_MS = 60 * 1000;
    
    /** Synchronization lock for thread-safe operations within the run loop. */
    private static final Object lock = new Object();

    /** Flag indicating whether the background thread is currently active. */
    private boolean isRunning = false;
    
    /** The actual thread executing the periodic tasks. */
    private Thread workerThread;

    /** DAO for managing reservation data. */
    private final ReservationDAO reservationDAO = new ReservationDAO();
    
    /** DAO for managing physical table statuses. */
    private final TableDAO tableDAO = new TableDAO();
    
    /** DAO for managing bill and payment records. */
	private final BillDAO billDAO = new BillDAO();
	
    /** DAO for managing waiting list entries. */
    private final WaitingListDAO waitingListDAO = new WaitingListDAO();
    
    /** Controller used to process payments and handle subscriber discounts. */
    private final PaymentController paymentController = new PaymentController();
    
    /** Controller used to retrieve and manage table state. */
    private final TableController tableController = new TableController();

    /**
     * Starts the background watchdog thread if it is not already running.
     */
    public void start() {
        if (!isRunning) {
            isRunning = true;
            workerThread = new Thread(this, "DailyFunctionWatchdog");
            workerThread.start();
            System.out.println("DailyFunctionController started");
        }
    }

    /**
     * Stops the background watchdog thread.
     */
    public void stop() {
        isRunning = false;
        System.out.println("DailyFunctionController stopped");
    }

    /**
     * The main execution loop of the controller. 
     * Periodically triggers maintenance tasks every minute while the controller is running.
     */
    @Override
    public void run() {
        while (isRunning) {
            try {
                synchronized (lock) {
                    handleNoShows();
                    handleWaitingList();
                    cancelExpiredWaitingListEntries();
                    handleBillGeneration();
                }

                Thread.sleep(CHECK_INTERVAL_MS);

            } catch (InterruptedException e) {
                isRunning = false;
            } catch (Exception e) {
                System.err.println(" Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    /**
     * Identifies confirmed reservations where the customer failed to arrive.
     * If a customer is more than 15 minutes late, the reservation is marked as NOT_SHOWED
     * and the assigned table is released.
     */
    private void handleNoShows() {
        List<Reservation> reservations = reservationDAO.readAllReservations();
        LocalDateTime now = LocalDateTime.now();

        for (Reservation r : reservations) {
            if (r.getStatus() != ReservationStatus.CONFIRMED)
                continue;

            LocalDateTime reservationDateTime =
                    LocalDateTime.of(r.getReservationDate(), r.getReservationTime());

            if (now.isAfter(reservationDateTime.plusMinutes(15))) {
                r.setStatus(ReservationStatus.NOT_SHOWED);

                if (r.getTableID() != null) {
                    Table table = tableDAO.GetTable(r.getTableID());
                    if (table != null) {
                        table.release();
                        tableDAO.UpdateTable(table);
                    }
                    r.setTableID(null);
                }

                reservationDAO.updateReservation(r);
            }
        }
    }

    /**
     * Checks the waiting list for the next eligible entry and attempts to seat them.
     * It simulates the current restaurant capacity to see if a suitable table is available
     * for the waiting group at their requested time.
     */
    private void handleWaitingList() {
        WaitingListEntry entry = waitingListDAO.getNextWaitingEntry();
        if (entry == null) return;
        if (entry.getExitReason() == ExitReason.CANCELLED || entry.getExitReason() == ExitReason.SEATED) return;

        List<Table> allTables = tableController.getAllTables();
        List<Reservation> reservations = reservationDAO.getConfirmedReservationsAt(entry.getWaitDate(), entry.getWaitTime());

        boolean[] tableOccupied = new boolean[allTables.size()];
        for (Reservation r : reservations) {
            for (int i = 0; i < allTables.size(); i++) {
                Table t = allTables.get(i);
                if (!tableOccupied[i] && t.getCapacity() >= r.getNumOfGuests()) {
                    tableOccupied[i] = true;
                    break;
                }
            }
        }

        boolean canSeat = false;
        for (int i = 0; i < allTables.size(); i++) {
            Table t = allTables.get(i);
            if (!tableOccupied[i] && t.getCapacity() >= entry.getNumOfGuests()) {
                canSeat = true;
                break;
            }
        }

        if (!canSeat) return;
        
        Integer uid = entry.getUserID();

        Reservation reservation = new Reservation(
                0,
                uid == null ? 0 : uid,
                entry.getNumOfGuests(),
                entry.getConfirmationCode(),
                entry.getWaitDate(),
                entry.getWaitTime(),
                ReservationStatus.CONFIRMED,
                true
        );

        reservationDAO.insertReservation(reservation);
        waitingListDAO.updateExitReasonByConfirmationCode(entry.getConfirmationCode(), ExitReason.SEATED);
    }

    /**
     * Cancels any waiting list entries whose requested time has already passed 
     * without them being seated.
     */
    private void cancelExpiredWaitingListEntries() {
        List<WaitingListEntry> waitingList =
                waitingListDAO.getWaitingEntriesWithoutExitReason();

        LocalDateTime now = LocalDateTime.now();

        for (WaitingListEntry entry : waitingList) {
            LocalDateTime entryDateTime =
                    LocalDateTime.of(entry.getWaitDate(), entry.getWaitTime());

            if (entryDateTime.isBefore(now)) {
                waitingListDAO.updateExitReasonByConfirmationCode(
                        entry.getConfirmationCode(),
                        ExitReason.CANCELLED
                );
            }
        }
    }

    /**
     * Automatically handles the transition from SEATED to COMPLETED.
     * If a group has been seated for more than 2 hours, the system generates a random bill,
     * processes the payment via PaymentController, and marks the reservation as completed.
     */
    private void handleBillGeneration() {
        List<Reservation> reservations = reservationDAO.readAllReservations();
        LocalDateTime now = LocalDateTime.now();

        for (Reservation r : reservations) {
            if (r.getStatus() != ReservationStatus.SEATED ||
                r.getActualArrivalTime() == null ||
                r.getBillID() != null)
                
                continue;

            LocalDateTime arrivalDateTime =
                    LocalDateTime.of(r.getReservationDate(), r.getActualArrivalTime());

            if (now.isAfter(arrivalDateTime.plusHours(2))) {
                Bill bill = new Bill(0, r.getReservationID(), generateRandomAmount());
                PaymentResult result = paymentController.addPayment(bill);

                if (result.isSuccess() && result.getBill() != null) {
                    r.setBillID(result.getBill().getBillID());
                    r.setStatus(ReservationStatus.COMPLETED);
                    reservationDAO.updateReservation(r);
                }
            }
        }
    }

    /**
     * Generates a random bill amount between 100.0 and 1000.0.
     * @return A double representing the simulated bill total, rounded to two decimal places.
     */
    private double generateRandomAmount() {
        double amount = 100 + Math.random() * 900;
        return Math.round(amount * 100.0) / 100.0;
    }
}