package Entities;

import java.io.Serializable;

/**
 * Table class represents a table in the restaurant
 */
public class Table implements Serializable {
	private int tableID;
	private int capacity;
	private boolean available;
	
	/**
	 * constructor for new table
	 * @param tableID
	 * @param capacity
	 */
	public Table(int tableID, int capacity) {
		this.tableID = tableID;
		this.capacity = capacity;
		this.available = true;
	}
	
	/**
	 * constructor for table from db
	 * @param tableID
	 * @param capacity
	 * @param available
	 */
	public Table(int tableID, int capacity, boolean available) {
		this.tableID = tableID;
		this.capacity = capacity;
		this.available = available;
	}
	
	//getters
	
	public int getTableID() {
		return this.tableID;
	}
	
	public int getCapacity() {
		return this.capacity;
	}
	
	public boolean isAvailable() {
		return this.available;
	}
	
	//setters
	
	public void setCapacity(int newCapacity) {
		this.capacity = newCapacity;
	}
	
	/**
	 * canSeat returns if the customer can seat in this table based on the number of customers
	 * @param numOfGuests
	 * @return True if there is enough chairs for all the customers in the reservation
	 */
	public boolean canSeat(int numOfGuests) {
		return this.available && numOfGuests <= this.capacity;
	}
	
	/**
	 * occupy sets the table as occupied
	 */
	public void occupy() {
		this.available = false;
	}
	
	/**
	 * release sets the table as available
	 */
	public void release() {
		this.available = true;
	}
}
