package Entities;

import java.io.Serializable;

/**
 * Represents a table in the restaurant.
 * 
 * A table has an ID, a seating capacity, and availability status.
 * It provides methods to check if it can seat a given number of guests,
 * and to occupy or release the table.
 */
@SuppressWarnings("serial")
public class Table implements Serializable {

    /** Unique identifier for the table. */
    private int tableID;

    /** Number of seats available at the table. */
    private int capacity;

    /** True if the table is available, false if occupied. */
    private boolean available;

    /**
     * Constructs a new table with a given ID and capacity.
     * The table is initially available.
     *
     * @param tableID the table's unique identifier
     * @param capacity the number of seats at the table
     */
    public Table(int tableID, int capacity) {
        this.tableID = tableID;
        this.capacity = capacity;
        this.available = true;
    }

    /**
     * Constructs a table with a given ID, capacity, and availability.
     * This constructor is typically used when loading from the database.
     *
     * @param tableID the table's unique identifier
     * @param capacity the number of seats at the table
     * @param available true if the table is available, false if occupied
     */
    public Table(int tableID, int capacity, boolean available) {
        this.tableID = tableID;
        this.capacity = capacity;
        this.available = available;
    }

    /** Returns the table ID. */
    public int getTableID() {
        return this.tableID;
    }

    /** Returns the table's seating capacity. */
    public int getCapacity() {
        return this.capacity;
    }

    /** Returns true if the table is available. */
    public boolean isAvailable() {
        return this.available;
    }

    /** Sets the table's seating capacity. */
    public void setCapacity(int newCapacity) {
        this.capacity = newCapacity;
    }

    /**
     * Checks if the table can seat a given number of guests.
     *
     * @param numOfGuests the number of guests
     * @return true if the table is available and has enough seats
     */
    public boolean canSeat(int numOfGuests) {
        return this.available && numOfGuests <= this.capacity;
    }

    /** Marks the table as occupied. */
    public void occupy() {
        this.available = false;
    }

    /** Marks the table as available. */
    public void release() {
        this.available = true;
    }
}
