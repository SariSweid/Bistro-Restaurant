package Entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WaitingList {
	private LocalDate waitingListDate;
	private List<WaitingListEntry> waitingList;
	
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
	
	public void addEntryToWaitingList(WaitingListEntry waitingListEntry) {
		waitingList.add(waitingListEntry);
	}
	
	/**
	 * @return the current waiting list - all entries who's exit reason is still null
	 */
	public List<WaitingListEntry> getCurrentWaitingList(){
		return this.waitingList.stream().filter(e -> e.getExitReason() == null).toList();
	}
}
