package Entities;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Bill class represents the bill of the reservation
 */
public class Bill implements Serializable {
	private int billID;
	private int reservationID;
	private double totalAmount;
	private LocalDateTime issuedAt;
	private boolean paid;
	
	/**
	 * constructor for new bill
	 * @param billID
	 * @param reservationID
	 * @param totalAmount
	 */
	public Bill(int billID, int reservationID, double totalAmount) {
		this.billID = billID;
		this.reservationID = reservationID;
		this.totalAmount = totalAmount;
		this.issuedAt = LocalDateTime.now();
		this.paid = false;
	}
	
	/**
	 * constructor for bill from db
	 * @param billID
	 * @param reservationID
	 * @param totalAmount
	 * @param issuedAt
	 * @param paid
	 */
	public Bill(int billID, int reservationID, double totalAmount, LocalDateTime issuedAt, boolean paid) {
		this.billID = billID;
		this.reservationID = reservationID;
		this.totalAmount = totalAmount;
		this.issuedAt = issuedAt;
		this.paid = paid;
	}
	
	//getters
	
	public int getBillID() {
		return this.billID;
	}
	
	public int getReservationID() {
		return this.reservationID;
	}
	
	public double getTotalAmount() {
		return this.totalAmount;
	}
	
	public LocalDateTime getIssuedAt() {
		return this.issuedAt;
	}
	
	public boolean isPaid() {
		return this.paid;
	}
	
	/**
	 * sets the bill as paid
	 */
	public void markAsPaid() {
		this.paid = true;
	}
}
