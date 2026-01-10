package logicControllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import DAO.WaitingListDAO;
import Entities.WaitingList;
import Entities.WaitingListEntry;
import common.ServerResponse;
import enums.ExitReason;

public class WaitingListController {

    private WaitingList waitingList;
    private Random random;
    private final WaitingListDAO waitingListDAO;

    public WaitingListController() {
        this.waitingList = new WaitingList(LocalDate.now());
        this.random = new Random();
        this.waitingListDAO = new WaitingListDAO();
    }

    /**
     * Add customer to waiting list
     */
    public int addToWaitingList(Integer userID,
                                String email,
                                String phone,
                                int numOfGuests) {

        int confirmationCode = generateConfirmationCode();

        WaitingListEntry entry = new WaitingListEntry(
                userID,
                email,
                phone,
                numOfGuests,
                confirmationCode,
                LocalDate.now(),
                LocalTime.now(),
                null
        );

        waitingList.addEntryToWaitingList(entry);
        waitingListDAO.addToWitingList(entry);

        return confirmationCode;
    }

    /**
     * Cancel waiting
     */
    public ServerResponse cancelWaiting(int confirmationCode, Integer currentUserId) {

        WaitingListEntry entry =
                waitingListDAO.getByConfirmationCode(confirmationCode);

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

        // Ownership check (subscriber only)
        if (entry.getUserID() != null &&
            !entry.getUserID().equals(currentUserId)) {

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


    /**
     * Exit from waiting list (cancel / seated)
     */
    private boolean exitFromWaitingList(int confirmationCode, ExitReason exitReason) {
        return waitingListDAO.updateExitReason(confirmationCode, exitReason);
    }

    
    
    public List<WaitingListEntry> getWaitingListBetweenDates(LocalDate startDate,LocalDate endDate) {
    	


        return waitingListDAO.getWaitingListBetweenDates(startDate, endDate);
    }

    private int generateConfirmationCode() {
        return 100000 + random.nextInt(900000);
    }
}
