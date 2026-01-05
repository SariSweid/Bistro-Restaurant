package logicControllers;

import java.time.LocalDate;
import java.util.Random;

import Entities.WaitingList;

public class WaitingListController {
	private WaitingList waitingList; //today waiting list
	private Random random; //in order to generate random confirmation code
	
	/**
	 * constructor to create new daily waiting list
	 */
	public WaitingListController() {
		this.waitingList = new WaitingList(LocalDate.now());
		this.random = new Random();
	}
	/**
	 * 
	 * @return random 6 digit confirmation code
	 */
	private int generateConfirmationCode() {
		return 100000 + random.nextInt(900000); //random 6 digit confirmation code
	}
	
	/**
	 * 
	 */
	public void addToWaitingList() {
		
	}
}
