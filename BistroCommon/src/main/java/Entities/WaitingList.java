package Entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/**
 * WaitingList class represents a waiting list of a single day
 */
public class WaitingList implements Serializable {
	private LocalDate waitingListDate;
	private List<WaitingListEntry> waitingList;
	
	/**
	 * constructor for a new waiting list for a new day
	 * @param waitingListDate
	 */
	public WaitingList(LocalDate waitingListDate) {
		this.waitingListDate = waitingListDate;
		this.waitingList = new ArrayList<>();
	}
	
	//getters
	
	public List<WaitingListEntry> getWaitingList(){
		return this.waitingList;
	}
	
	public LocalDate getWaitingListDate() {
		return this.waitingListDate;
	}
	
	/**
	 * addEntryToWaitingList adds a new entry to the waiting list
	 * @param waitingListEntry
	 */
	public void addEntryToWaitingList(WaitingListEntry waitingListEntry) {
		waitingList.add(waitingListEntry);
	}
	
	/**
	 * getCurrentWaitingList returns the current waiting list
	 * @return the current waiting list - all entries who's exit reason is still null
	 */
	public List<WaitingListEntry> getCurrentWaitingList(){
		return this.waitingList.stream().filter(e -> e.getExitReason() == null).toList();
	}
}
