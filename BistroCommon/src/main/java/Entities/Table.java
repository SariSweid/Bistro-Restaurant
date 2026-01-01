package Entities;

public class Table {
	private int tableID;
	private int capacity;
	private boolean available;
	
	//constructor for new table
	public Table(int tableID, int capacity) {
		this.tableID = tableID;
		this.capacity = capacity;
		this.available = true;
	}
	
	//constructor for table from db
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
	
	public boolean canSeat(int numOfGuests) {
		return this.available && numOfGuests <= this.capacity;
	}
	
	public void occupy() {
		this.available = false;
	}
	
	public void release() {
		this.available = true;
	}
}
