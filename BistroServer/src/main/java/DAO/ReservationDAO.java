package DAO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import DB.DBController;
import Entities.*;
import enums.ReservationStatus;

/**
 * DAO for handling Reservations
 */
public class ReservationDAO extends DBController {
	
	
    public boolean updateReservation(Reservation reservation) {
        Connection con = getConnection();

        try (PreparedStatement pst = con.prepareStatement(
                "UPDATE reservation SET reservationDate = ?, reservationTime = ?, numOfGuests = ?, status = ?, tableID = ?, actualarrivaltime = COALESCE(?, actualarrivaltime), departuretiime = ? WHERE reservationID = ?"
            )) {
            pst.setDate(1, Date.valueOf(reservation.getReservationDate()));
            pst.setTime(2, Time.valueOf(reservation.getReservationTime()));
            pst.setInt(3, reservation.getNumOfGuests());
            pst.setString(4, reservation.getStatus().name());

            // update tableID safely
            if (reservation.getTableID() != null) {
                pst.setInt(5, reservation.getTableID());
            } else {
                pst.setNull(5, java.sql.Types.INTEGER);
            }

            // safely update actual arrival only if not null, else keep existing
            if (reservation.getActualArrivalTime() != null) {
                pst.setTime(6, Time.valueOf(reservation.getActualArrivalTime()));
            } else {
                pst.setNull(6, java.sql.Types.TIME); // COALESCE in SQL keeps existing
            }

            // update departure time normally
            if (reservation.getExpectedDepartureTime() != null) {
                pst.setTime(7, Time.valueOf(reservation.getExpectedDepartureTime()));
            } else {
                pst.setNull(7, java.sql.Types.TIME);
            }

            pst.setInt(8, reservation.getReservationID());

            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public List<TimeData> getTimeDataBetween(LocalDate startDate, LocalDate endDate) {
        Connection con = getConnection();
        List<TimeData> list = new ArrayList<>();

        try (PreparedStatement pst = con.prepareStatement(
                "SELECT r.reservationTime, r.actualarrivaltime, r.departuretiime FROM reservation r WHERE r.reservationDate BETWEEN ? AND ?")) {

            pst.setDate(1, Date.valueOf(startDate));
            pst.setDate(2, Date.valueOf(endDate));

            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                LocalTime reservationTime = rs.getTime("reservationTime").toLocalTime();

                Timestamp actualArrivalTs = rs.getTimestamp("actualarrivaltime");
                LocalTime actualArrival = (actualArrivalTs != null)
                        ? actualArrivalTs.toLocalDateTime().toLocalTime()
                        : null;

                Timestamp departureTs = rs.getTimestamp("departuretiime");
                LocalTime departure = (departureTs != null)
                        ? departureTs.toLocalDateTime().toLocalTime()
                        : null;

                list.add(new TimeData(reservationTime, actualArrival, departure));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    
    
    public boolean cancelReservationInDB(int reservationID) {
        String sql = "UPDATE reservation SET status = ? WHERE reservationID = ?";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, enums.ReservationStatus.CANCELLED.name());
            pst.setInt(2, reservationID);

            int rows = pst.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("SQL Exception during cancel: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
	/**
	 * Retrieves a reservation by its ID.
	 *
	 * @param ReservationId the reservation ID
	 * @return the Reservation if found, or null if not found
	 */
	public Reservation GetReservation(int ReservationId) {
		
		Connection con = getConnection(); //connect to DB
		Reservation r = null;
		
		try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `reservation` WHERE reservationID = ?")){
			pst.setInt(1, ReservationId);
			ResultSet rs = pst.executeQuery();
			
	        if (rs.next()) {
	        	
    			int reservationID  = rs.getInt("reservationID");
    			Date reservationDate  = rs.getDate("reservationDate"); 
    			Time reservationTime  = rs.getTime("reservationTime");
    			int numOfGuests  = rs.getInt("numOfGuests");
    			int confirmation_code = rs.getInt("confirmationCode");
    			enums.ReservationStatus status = enums.ReservationStatus.valueOf(rs.getString("status"));
    			int customerID  = rs.getInt("customerID");
    			int tableID  = rs.getInt("TableId");
    			int billID  = rs.getInt("BillId");
    			Date reservationPlacedDate  = rs.getDate("reservationPlacedDate"); 
    			Time reservationPlacedTime  = rs.getTime("reservationPlacedTime");

    			
    			//conver date and time to local
    			LocalDate resDate = reservationDate.toLocalDate();
    			LocalTime resTime = reservationTime.toLocalTime();
    			LocalDate placedDate = reservationPlacedDate.toLocalDate();
    			LocalTime placedTime = reservationPlacedTime.toLocalTime();
	        

	            
	            
    			r = new Reservation(reservationID,customerID,tableID,billID,numOfGuests,confirmation_code
    											,resDate,resTime,placedDate,placedTime,status);
	        }
	        	        
		}
					
		 catch (SQLException e) {
			e.printStackTrace();
		}
		
		return r; // There isnt Res with this ID.
	}
	
	
	/**
     * “Which tables are already taken at THIS exact time?”
     *
     * @return a list reservations at the specific date (input)
     */
	// ADDED:
	public List<Reservation> getReservationsAt(LocalDate date, LocalTime time) {
	    List<Reservation> list = new ArrayList<>();
	    Connection con = getConnection();

	    try (PreparedStatement pst = con.prepareStatement(
	        "SELECT * FROM `reservation` " +
	        "WHERE reservationDate = ? AND reservationTime = ? " +
	        "AND status IN ('CONFIRMED','PENDING','SEATED')"
	    )) {
	        pst.setDate(1, Date.valueOf(date));
	        pst.setTime(2, Time.valueOf(time));

	        ResultSet rs = pst.executeQuery();
	        while (rs.next()) {
	            list.add(new Reservation(
	                rs.getInt("reservationID"),
	                rs.getInt("customerID"),
	                rs.getInt("TableId"),
	                rs.getInt("BillId"),
	                rs.getInt("numOfGuests"),
	                rs.getInt("confirmationCode"),
	                rs.getDate("reservationDate").toLocalDate(),
	                rs.getTime("reservationTime").toLocalTime(),
	                rs.getDate("reservationPlacedDate").toLocalDate(),
	                rs.getTime("reservationPlacedTime").toLocalTime(),
	                enums.ReservationStatus.valueOf(rs.getString("status"))
	            ));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return list;
	}

    
	// Retrieves all reservation for a specific customer
	public List<Reservation> getReservationsByCustomer(int customerId) {
	    List<Reservation> list = new ArrayList<>();
	    String sql = "SELECT * FROM reservation " +
	                 "WHERE customerID = ? AND status IN ('CONFIRMED','PENDING','SEATED')";

	    try (Connection con = getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {
	        pst.setInt(1, customerId);

	        ResultSet rs = pst.executeQuery();
	        while (rs.next()) {
	            list.add(new Reservation(
	                rs.getInt("reservationID"),
	                rs.getInt("customerID"),
	                rs.getInt("TableId"),
	                rs.getInt("BillId"),
	                rs.getInt("numOfGuests"),
	                rs.getInt("confirmationCode"),
	                rs.getDate("reservationDate").toLocalDate(),
	                rs.getTime("reservationTime").toLocalTime(),
	                rs.getDate("reservationPlacedDate").toLocalDate(),
	                rs.getTime("reservationPlacedTime").toLocalTime(),
	                enums.ReservationStatus.valueOf(rs.getString("status"))
	            ));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	   
	    return list;
	}
	
    /**
     * Retrieves all reservations from the database.
     *
     * @return a list of all reservations
     */
    public List<Reservation> readAllReservations() {
    	
    	List<Reservation> reservations = new ArrayList<>(); // made new list to return
    	Connection con = getConnection(); //connect to DB
    	
    	try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `reservation`")){ // ask from DB the all Orders 
    		
    		ResultSet rs = pst.executeQuery();
    		
    		while(rs.next()) { // read from DB
    			
    			int reservationID  = rs.getInt("reservationID");
    			Date reservationDate  = rs.getDate("reservationDate"); 
    			Time reservationTime  = rs.getTime("reservationTime");
    			int numOfGuests  = rs.getInt("numOfGuests");
    			int confirmation_code = rs.getInt("confirmationCode");
    			enums.ReservationStatus status = enums.ReservationStatus.valueOf(rs.getString("status"));
    			int customerID  = rs.getInt("customerID");
    			int tableID  = rs.getInt("TableId");
    			int billID  = rs.getInt("BillId");
    			Date reservationPlacedDate  = rs.getDate("reservationPlacedDate"); 
    			Time reservationPlacedTime  = rs.getTime("reservationPlacedTime");

    			
    			//conver date and time to local
    			LocalDate resDate = reservationDate.toLocalDate();
    			LocalTime resTime = reservationTime.toLocalTime();
    			LocalDate placedDate = reservationPlacedDate.toLocalDate();
    			LocalTime placedTime = reservationPlacedTime.toLocalTime();
    			
    			Reservation r = new Reservation(reservationID,customerID,tableID,billID,numOfGuests,confirmation_code
    											,resDate,resTime,placedDate,placedTime,status);
    			
                reservations.add(r);
    			
    		}
    		
    		for (Reservation r : reservations) {
    		    System.out.println(r);
    		}
    		
    		
    	} catch (SQLException e) {

			e.printStackTrace();
		}
    	
    	
		return reservations;    	
    }
    
    public boolean isSubscriberByReservationId(int reservationID) {
    	Connection con = getConnection();
        boolean result = false;

        try (PreparedStatement pst = con.prepareStatement("SELECT u.Role FROM user u JOIN reservation r ON u.UserId = r.CustomerId WHERE r.ReservationId = ?")) {
            pst.setInt(1, reservationID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
            	return "SUBSCRIBER".equals(rs.getString("Role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public int getWaitlistSubscriberReservationsBetween(LocalDate start, LocalDate end) {
    	
    	Connection con = getConnection();

        try ( PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM reservation r JOIN user u ON r.customerID = u.UserId WHERE u.Role = 'SUBSCRIBER' AND r.status = 'WAITLIST' AND r.reservationDate BETWEEN ? AND ?")) {

            pst.setDate(1, java.sql.Date.valueOf(start));
            pst.setDate(2, java.sql.Date.valueOf(end));

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public int getCompletedSubscriberReservationsBetween(LocalDate start, LocalDate end) {
        
        Connection con = getConnection();

        try (PreparedStatement pst = con.prepareStatement("SELECT COUNT(*) FROM reservation r JOIN user u ON r.customerID = u.UserId WHERE u.Role = 'SUBSCRIBER' AND r.status = 'COMPLETED' AND r.reservationDate BETWEEN ? AND ?")) {

            pst.setDate(1, java.sql.Date.valueOf(start));
            pst.setDate(2, java.sql.Date.valueOf(end));

            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    	    

    
    /**
     * Inserts a new reservation into the database.
     * The database auto-generates reservationID.
     * Updates the reservation object with the assigned ID.
     *
     * @param r the reservation to insert
     * @return true if insertion succeeded, false otherwise
     */
    public boolean insertReservation(Reservation r) {
    		try (Connection con = getConnection();

    			PreparedStatement pst = con.prepareStatement(
                "INSERT INTO `reservation` " + 
                "(customerID, tableId, billId, numOfGuests, confirmationCode, reservationDate, reservationTime, reservationPlacedDate, reservationPlacedTime, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                java.sql.Statement.RETURN_GENERATED_KEYS // Allows us to retrieve auto-generated reservationID
        )) {

        	
	        	if (r.getCustomerId() != null) {
	        	    pst.setInt(1, r.getCustomerId());
	        	} else {
	        	    pst.setNull(1, java.sql.Types.INTEGER);
	        	}

            pst.setObject(2, r.getTableID(), java.sql.Types.INTEGER);
            pst.setObject(3, r.getBillID(), java.sql.Types.INTEGER);
            pst.setInt(4, r.getNumOfGuests());
            pst.setInt(5, r.getConfirmationCode());
            pst.setDate(6, java.sql.Date.valueOf(r.getReservationDate()));
            pst.setTime(7, java.sql.Time.valueOf(r.getReservationTime()));
            pst.setDate(8, java.sql.Date.valueOf(r.getReservationPlacedDate()));
            pst.setTime(9, java.sql.Time.valueOf(r.getReservationPlacedTime()));
            pst.setString(10, r.getStatus().name());

            int update_status = pst.executeUpdate();

            // Retrieve the auto-generated reservationID
            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    r.setReservationID(generatedKeys.getInt(1));
                }
            }

            return update_status > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    public int createPendingReservation(int userId, Integer tableId, int numGuests) {
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(
                 "INSERT INTO reservation (customerID, TableId, numOfGuests, status, reservationDate, reservationTime, reservationPlacedDate, reservationPlacedTime) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                 PreparedStatement.RETURN_GENERATED_KEYS)) {

            LocalDate nowDate = LocalDate.now();
            LocalTime nowTime = LocalTime.now();

            pst.setInt(1, userId);
            pst.setInt(2, tableId);
            pst.setInt(3, numGuests);
            pst.setString(4, "PENDING");
            pst.setDate(5, java.sql.Date.valueOf(nowDate));
            pst.setTime(6, java.sql.Time.valueOf(nowTime));
            pst.setDate(7, java.sql.Date.valueOf(nowDate));
            pst.setTime(8, java.sql.Time.valueOf(nowTime));

            pst.executeUpdate();
            ResultSet rs = pst.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);
            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    
    /**
     * Retrieves the count of reservations between two dates.
     *
     * @param start the start date
     * @param end the end date
     * @return number of reservations
     */
    public int getReservationsCountBetween(LocalDate start, LocalDate end) {
        int count = 0;
        try (PreparedStatement pst = getConnection().prepareStatement(
                "SELECT COUNT(*) as cnt FROM reservation WHERE reservationDate BETWEEN ? AND ?")) {
            pst.setDate(1, Date.valueOf(start));
            pst.setDate(2, Date.valueOf(end));
            ResultSet rs = pst.executeQuery();
            if (rs.next()) count = rs.getInt("cnt");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
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
	
	
    /**
     * Retrieves all reservations made by a specific user.
     *
     * @param user the user whose reservations are to be retrieved
     * @return a list of reservations made by the user
     */
	public List<Reservation> GetAllUserReservations(User user) {

	    Connection con = getConnection();
	    List<Reservation> reservations = new ArrayList<>();

	    try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `reservation` WHERE customerID = ?")) {

	        pst.setInt(1, user.getUserId());
	        ResultSet rs = pst.executeQuery();

	        while (rs.next()) {

	            int reservationID = rs.getInt("reservationID");
	            Date reservationDate = rs.getDate("reservationDate");
	            Time reservationTime = rs.getTime("reservationTime");
	            int numOfGuests = rs.getInt("numOfGuests");
	            int confirmationCode = rs.getInt("confirmationCode");
	            enums.ReservationStatus status = enums.ReservationStatus.valueOf(rs.getString("status"));
	            int customerID = rs.getInt("customerID");
	            int tableID = rs.getInt("TableId");
	            int billID = rs.getInt("BillId");
	            Date placedDate = rs.getDate("reservationPlacedDate");
	            Time placedTime = rs.getTime("reservationPlacedTime");

	            Reservation r = new Reservation(reservationID, customerID, tableID, billID, numOfGuests, confirmationCode, reservationDate.toLocalDate(), reservationTime.toLocalTime(), placedDate.toLocalDate(), placedTime.toLocalTime(), status);


	            reservations.add(r);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return reservations;
	}
    
    
    
    


}
