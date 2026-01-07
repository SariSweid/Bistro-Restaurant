package logicControllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import DB.DBController;
import Entities.Reservation;
import Entities.Table;

public class TableController {
	private final DBController dbController;
	
	/**
	 * constructor to create new TableController
	 */
	public TableController() {
		this.dbController = new DBController();
	}
	
	/**
	 * returns all the tables
	 * @return all tables in the restaurant
	 */
	public List<Table> getAllTables(){
		return this.dbController.GetAllTables();
	}
	
	/**
	 * finds a table by its id
	 * @param tableID
	 * @return table found by tableID
	 */
	public Table getTableByID(int tableID) {
		return this.dbController.GetTable(tableID);
	}
	
	/**
	 * finds the optimal available table for num of guests at given time and date
	 * @param numOfGuests
	 * @param date
	 * @param time
	 * @return the optimal available table
	 */
	public Optional<Table> findAvailableTable(int numOfGuests, LocalDate date, LocalTime time){
		//Tables that are reserved at this date and time
		List<Reservation> reservations = this.dbController.getReservationsAt(date, time);
		
		//all the restaurant tables
		List<Table> allTables = this.getAllTables();
		
		//filter the restaurant tables by:
		//t -> t.canSeat(numOfGuests) - is the table big enough for the given num of guests
		//t -> reservations.stream().noneMatch(r -> r.getTableID() == t.getTableID()) - the table is not taken already
		//min((t1, t2) -> Integer.compare(t1.getCapacity(), t2.getCapacity())) - after all the filtering of the tables, 
		//choose the table with the minimum capacity that is still big enough to hold the num of guests
        return allTables.stream().filter(t -> t.canSeat(numOfGuests)).filter(t -> 
        		reservations.stream().noneMatch(r -> r.getTableID() == t.getTableID())).min((t1, t2) -> 
        		Integer.compare(t1.getCapacity(), t2.getCapacity()));
	}
	
	/**
	 * marks table as occupied and update its status in db
	 * @param tableID
	 * @return true if the update succeeded
	 */
	public boolean occupyTable(int tableID) {
		Table table = this.getTableByID(tableID);
		if(table == null || !table.isAvailable()) {
			return false;
		}
		table.occupy();
		return this.dbController.UpdateTable(table);
	}
	
	/**
	 * marks table as available and update its status in db
	 * @param tableID
	 * @return true if the update succeeded
	 */
	public boolean releaseTable(int tableID) {
		Table table = this.getTableByID(tableID);
		if(table == null || !table.isAvailable()) {
			return false;
		}
		table.release();
		return this.dbController.UpdateTable(table);
	}
}
