package logicControllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import DAO.ReservationDAO;
import DAO.TableDAO;
import Entities.Reservation;
import Entities.Table;
import enums.ReservationStatus;

/**
 * Controller responsible for physical table management within the restaurant.
 * It handles the assignment of specific tables to groups, occupancy updates,
 * and calculations of remaining capacity.
 */
public class TableController {
	private final TableDAO TabledbController;
	private final ReservationDAO Resdb;
	
	/**
	 * Initializes the controller with the necessary Data Access Objects.
	 */
	public TableController() {
		this.TabledbController = new TableDAO();
		this.Resdb = new ReservationDAO();
	}
	
	/**
	 * Retrieves a full list of all physical tables in the restaurant.
	 * @return List of Table entities.
	 */
	public List<Table> getAllTables(){
		return this.TabledbController.GetAllTables();
	}
	
	/**
	 * Retrieves a specific table's details by its unique identifier.
	 * @param tableID The primary key of the table.
	 * @return The Table entity, or null if not found.
	 */
	public Table getTableByID(int tableID) {
		return this.TabledbController.GetTable(tableID);
	}
	
	/**
	 * Finds the "Best Fit" table for a group of a specific size at a specific time.
	 * * Logic:
	 * 1. Filters out tables that are too small.
	 * 2. Filters out tables already assigned to a confirmed reservation.
	 * 3. Selects the smallest possible table that fits the group (to optimize space).
	 * * @param numOfGuests Size of the party.
	 * @param date Requested date.
	 * @param time Requested time.
	 * @return An Optional containing the optimal Table, or empty if none are available.
	 */
	public Optional<Table> findAvailableTable(int numOfGuests, LocalDate date, LocalTime time){
		List<Reservation> reservations = this.Resdb.getReservationsAt(date, time);
		List<Table> allTables = this.getAllTables();
		
        return allTables.stream()
                .filter(t -> t.canSeat(numOfGuests)) // Is it big enough?
                .filter(t -> reservations.stream()
                        .noneMatch(r -> r.getTableID() == t.getTableID())) // Is it unreserved?
                .min((t1, t2) -> Integer.compare(t1.getCapacity(), t2.getCapacity())); // Best fit
	}
	
	/**
	 * Calculates the number of remaining seats available in the restaurant 
	 * for a given time slot by subtracting confirmed guests from total capacity.
	 * * @param date The date to check.
	 * @param time The time slot to check.
	 * @return The count of unoccupied seats.
	 */
	public int getTotalAvailableSeats(LocalDate date, LocalTime time) {
	    List<Table> allTables = getAllTables();
	    List<Reservation> reservations = Resdb.getReservationsAt(date, time);

	    int usedSeats = reservations.stream()
	                                .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED)
	                                .mapToInt(Reservation::getNumOfGuests)
	                                .sum();

	    int totalCapacity = allTables.stream()
	                                 .mapToInt(Table::getCapacity)
	                                 .sum();

	    return totalCapacity - usedSeats;
	}

	/**
	 * Updates a table's state to 'Occupied' in the database.
	 * This is typically called when a guest is seated.
	 * * @param tableID The ID of the table to occupy.
	 * @return true if the database update was successful.
	 */
	public boolean occupyTable(int tableID) {
		Table table = this.getTableByID(tableID);
		if(table == null || !table.isAvailable()) {
			return false;
		}
		table.occupy();
		return this.TabledbController.UpdateTable(table);
	}
	
	/**
	 * Updates a table's state to 'Available' in the database.
	 * This is typically called when a guest pays and leaves.
	 * * @param tableID The ID of the table to release.
	 * @return true if the database update was successful.
	 */
	public boolean releaseTable(int tableID) {
		Table table = this.getTableByID(tableID);
		if(table == null) {
			return false;
		}
		table.release();
		return this.TabledbController.UpdateTable(table);
	}
}