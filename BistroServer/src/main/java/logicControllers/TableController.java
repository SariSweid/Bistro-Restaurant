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
	 * 
	 * @return all tables in the restaurant
	 */
	public List<Table> getAllTables(){
		return this.dbController.GetAllTables();
	}
	
	/**
	 * 
	 * @param tableID
	 * @return table found by tableID
	 */
	public Table getTableByID(int tableID) {
		return this.dbController.GetTable(tableID);
	}
	
	public Optional<Table> findAvailableTable(int numOfGuests, LocalDate date, LocalTime time){
		//Tables that are reserved at this date and time
		List<Reservation> reservations = this.dbController.getReservationsAt(date, time);
		
		//all the restaurant tables
		List<Table> allTables = this.getAllTables();
		
		//filter the restaurant tables by
        return allTables.stream().filter(t -> t.canSeat(numOfGuests)).filter(t -> 
        		reservations.stream().noneMatch(r -> r.getTableID() == t.getTableID())).min((t1, t2) -> 
        		Integer.compare(t1.getCapacity(), t2.getCapacity()));
	}
	
	public boolean occupyTable(int tableID) {
		Table table = this.getTableByID(tableID);
		if(table == null || !table.isAvailable()) {
			return false;
		}
		table.occupy();
		return this.dbController.UpdateTable(table);
	}
	
	public boolean releaseTable(int tableID) {
		Table table = this.getTableByID(tableID);
		if(table == null || !table.isAvailable()) {
			return false;
		}
		table.release();
		return this.dbController.UpdateTable(table);
	}
}
