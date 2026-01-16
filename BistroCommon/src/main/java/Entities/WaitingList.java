package Entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a waiting list for a single day in the restaurant.
 * Contains multiple WaitingListEntry objects and provides methods
 * to manage and retrieve the list.
 */
@SuppressWarnings("serial")
public class WaitingList implements Serializable {

    /**
     * The date for which this waiting list applies.
     */
    private LocalDate waitingListDate;

    /**
     * List of all entries in the waiting list.
     */
    private List<WaitingListEntry> waitingList;

    /**
     * Constructs a new WaitingList for the specified date.
     *
     * @param waitingListDate the date for the waiting list
     */
    public WaitingList(LocalDate waitingListDate) {
        this.waitingListDate = waitingListDate;
        this.waitingList = new ArrayList<>();
    }

    /**
     * Returns the list of all waiting list entries.
     *
     * @return list of WaitingListEntry objects
     */
    public List<WaitingListEntry> getWaitingList() {
        return this.waitingList;
    }

    /**
     * Returns the date of the waiting list.
     *
     * @return the waiting list date
     */
    public LocalDate getWaitingListDate() {
        return this.waitingListDate;
    }

    /**
     * Adds a new entry to the waiting list.
     *
     * @param waitingListEntry the entry to add
     */
    public void addEntryToWaitingList(WaitingListEntry waitingListEntry) {
        waitingList.add(waitingListEntry);
    }

    /**
     * Returns the current waiting list, including only entries
     * whose exit reason is still null (i.e., they have not yet left or been seated).
     *
     * @return list of current WaitingListEntry objects
     */
    public List<WaitingListEntry> getCurrentWaitingList() {
        return this.waitingList.stream()
                .filter(e -> e.getExitReason() == null)
                .toList();
    }
}
