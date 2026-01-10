package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import DB.DBController;
import Entities.Bill;

/**
 * DAO for handling Bills.
 */
public class BillDAO extends DBController {
	
	
    public Bill getBillById(int billId) {
    	Connection con = getConnection();

        try ( PreparedStatement ps = con.prepareStatement("SELECT * FROM bill WHERE billID = ?")) {
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("BillId");
                int reservationId = rs.getInt("reservationId");
                double totalAmount = rs.getDouble("Amount");
                Timestamp issuedAtTS = rs.getTimestamp("issuedAt");
                boolean paid = rs.getBoolean("paid");
                LocalDateTime issuedAt = issuedAtTS.toLocalDateTime();
                return new Bill(id, reservationId, totalAmount, issuedAt, paid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
	public Bill getBillByReservationId(int reservationId) {
	    Connection con = getConnection();
	    Bill b = null;

	    try (PreparedStatement pst = con.prepareStatement(
	            "SELECT * FROM bill WHERE reservationID = ? ORDER BY BillId DESC LIMIT 1")) {
	        pst.setInt(1, reservationId);
	        ResultSet rs = pst.executeQuery();

	        if (rs.next()) {
	            int id = rs.getInt("BillId");
	            double amount = rs.getDouble("Amount");
	            LocalDateTime issuedAt = rs.getTimestamp("issuedAt").toLocalDateTime();
	            boolean paid = rs.getBoolean("paid");

	            b = new Bill(id, reservationId, amount, issuedAt, paid);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return b;
	}
	
    /**
     * Retrieves a specific bill by their ID.
     *
     * @param Billid the bill ID
     * @return the Bill if found, or null if not found
     */
	public Bill GetBill(int billId) {

	    Connection con = getConnection();
	    Bill b = null;

	    try (PreparedStatement pst = con.prepareStatement(
	            "SELECT * FROM bill WHERE BillId = ?")) {

	        pst.setInt(1, billId);
	        ResultSet rs = pst.executeQuery();

	        if (rs.next()) {

	            int id = rs.getInt("BillId");
	            int reservationID = rs.getInt("reservationID");
	            double amount = rs.getDouble("Amount");
	            LocalDateTime issuedAt =  rs.getTimestamp("issuedAt").toLocalDateTime();
	            boolean paid = rs.getBoolean("paid");

	            b = new Bill(id, reservationID, amount, issuedAt, paid);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return b; 
	}
	
    /**
     * Inserts a new bill into the database.
     *
     * @param b the bill to insert
     * @return true if the insertion succeeded, false otherwise
     */
	public Boolean AddBill(Bill b) {

	    Connection con = getConnection();

	    try {
	        PreparedStatement checkPaid = con.prepareStatement(
	                "SELECT paid FROM bill WHERE reservationID = ? ORDER BY BillId DESC LIMIT 1");
	        checkPaid.setInt(1, b.getReservationID());

	        try (ResultSet rs = checkPaid.executeQuery()) {
	            if (rs.next()) {
	                boolean alreadyPaid = rs.getBoolean("paid");
	                if (alreadyPaid) {
	                    return false;
	                }
	            }
	        }

	        try (PreparedStatement pst = con.prepareStatement(
	                "INSERT INTO bill (reservationID, Amount, issuedAt, paid) VALUES (?,?,?,?)")) {

	            pst.setInt(1, b.getReservationID());
	            pst.setDouble(2, b.getTotalAmount());
	            pst.setTimestamp(3, java.sql.Timestamp.valueOf(b.getIssuedAt()));
	            pst.setBoolean(4, b.isPaid());

	            int updateStatus = pst.executeUpdate();

	            if (updateStatus > 0) {
	                try (PreparedStatement pst2 = con.prepareStatement(
	                        "SELECT BillId FROM bill WHERE reservationID = ? ORDER BY BillId DESC LIMIT 1")) {
	                    pst2.setInt(1, b.getReservationID());

	                    try (ResultSet rs2 = pst2.executeQuery()) {
	                        if (rs2.next()) {
	                            int newBillId = rs2.getInt("BillId");
	                            b.setBillID(newBillId);
	                            return updateReservationBillId(b.getReservationID(), newBillId);
	                        }
	                    }
	                }
	            }

	        }

	        return false;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	
	public boolean updateReservationBillId(int reservationId, int billId) {
		
	    Connection con = getConnection();

	    try (PreparedStatement pst = con.prepareStatement(
	            "UPDATE reservation SET billID = ? WHERE reservationID = ?")) {

	        pst.setInt(1, billId);
	        pst.setInt(2, reservationId);

	        int updateStatus = pst.executeUpdate();
	        return updateStatus > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
	


}
