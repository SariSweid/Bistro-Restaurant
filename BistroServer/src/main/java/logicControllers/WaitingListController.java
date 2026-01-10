package logicControllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import DAO.WaitingListDAO;
import Entities.WaitingList;
import Entities.WaitingListEntry;
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
    public boolean cancelWaiting(int confirmationCode) {
        return exitFromWaitingList(confirmationCode, ExitReason.CANCELLED);
    }

    /**
     * Exit from waiting list (cancel / seated)
     */
    private boolean exitFromWaitingList(int confirmationCode, ExitReason exitReason) {

        Optional<WaitingListEntry> entryOpt =
                waitingList.getCurrentWaitingList().stream()
                        .filter(e -> e.getConfirmationCode() == confirmationCode)
                        .findFirst();

        if (entryOpt.isPresent()) {
            WaitingListEntry entry = entryOpt.get();
            entry.exit(exitReason);

            
            waitingListDAO.updateExitReason(
                    confirmationCode,
                    exitReason
            );
            return true;
        }
        return false;
    }
    
    
    public List<WaitingListEntry> getWaitingListBetweenDates(LocalDate startDate,LocalDate endDate) {
    	


        return waitingListDAO.getWaitingListBetweenDates(startDate, endDate);
    }

    private int generateConfirmationCode() {
        return 100000 + random.nextInt(900000);
    }
}
