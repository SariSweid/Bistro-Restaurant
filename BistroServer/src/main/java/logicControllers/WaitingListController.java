package logicControllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import DB.DBController;
import Entities.WaitingList;
import Entities.WaitingListEntry;
import enums.ExitReason;

public class WaitingListController {
	private WaitingList waitingList; //today waiting list
	private Random random; //in order to generate random confirmation code
	private final DBController dbController;
	
	/**
	 * constructor to create new daily waiting list
	 */
	public WaitingListController() {
		this.waitingList = new WaitingList(LocalDate.now());
		this.random = new Random();
		this.dbController = new DBController();
	}
	
	/**
	 * creates new waiting list for today
	 * called at opening time each day
	 * saves the previous day's list to db
	 */
	public void startNewDay() {
		saveDailyWaitingList();
		this.waitingList = new WaitingList(LocalDate.now());
	}
	
	/**
	 * adding new entry to the waiting list
	 * @param userID
	 * @param contactInfo
	 * @param numOfGuests
	 * @return confirmation code
	 */
	public int addToWaitingList(Integer userID, String contactInfo, int numOfGuests) {
		int confirmationCode = generateConfirmationCode();
		WaitingListEntry entry = new WaitingListEntry(userID, contactInfo, numOfGuests, confirmationCode);
		this.waitingList.addEntryToWaitingList(entry);
		return confirmationCode;
	}
	
	/**
	 * marks entry in the waiting list as seated
	 * @param confirmationCode
	 * @return 
	 */
	public boolean seatCustomer(int confirmationCode) {
		return exitFromWaitingList(confirmationCode, ExitReason.SEATED);
	}
	
	/**
	 * marks entry in the waiting list as cancelled
	 * @param confirmationCode
	 * @return
	 */
	public boolean cancelWaiting(int confirmationCode) {
		return exitFromWaitingList(confirmationCode, ExitReason.CANCELLED);
	}
	
	/**
	 * 
	 * @return list of active waiting list
	 */
	public List<WaitingListEntry> getActiveWaitingList(){
		return this.waitingList.getCurrentWaitingList();
	}
	
	/**
	 * 
	 * @return full waiting list of today
	 */
	public List<WaitingListEntry> getFullWaitingList(){
		return this.waitingList.getWaitingList();
	}
	
	/**
	 * 
	 * @return waiting list date
	 */
	public LocalDate getWaitingListDate() {
		return this.waitingList.getWaitingListDate();
	}
	
	/**
	 * saves waiting list entries in db
	 */
	public void saveDailyWaitingList() {
		for(WaitingListEntry entry: this.waitingList.getWaitingList()) {
			this.dbController.addToWitingList(entry);
		}
	}
	
	/**
	 * exitFromWaitingList handles exiting from waiting list and saves the entry into db
	 * @param confirmationCode
	 * @param exitReason
	 * @return true if the entry was found and 
	 */
	private boolean exitFromWaitingList(int confirmationCode, ExitReason exitReason) {		
		//Optional is a wrapper generic object that can hold T object or be null, prevents NullPointerException
		//filter the active waiting list to find the confirmation code in the list
		Optional<WaitingListEntry> entryOpt = this.waitingList.getCurrentWaitingList().stream().filter(e -> e.getConfirmationCode() == confirmationCode).findFirst();
		if(entryOpt.isPresent()) {
			WaitingListEntry entry = entryOpt.get();
			entry.exit(exitReason);			
			//add entry to db
			this.dbController.addToWitingList(entry);
			return true;
		}
		return false;
	}
	
	/**
	 * 
	 * @return random 6 digit confirmation code
	 */
	private int generateConfirmationCode() {
		return 100000 + random.nextInt(900000); //random 6 digit confirmation code
	}
}
