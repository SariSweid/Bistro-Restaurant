package DAO;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import DB.DBController;
import Entities.*;
import enums.Day;
import enums.ReservationStatus;

/**
 * Data Access Object (DAO) for managing restaurant reservations.
 * This class handles all CRUD operations and specialized queries for reservations,
 * including availability checks, status updates, and notification tracking.
 */
public class ReservationDAO extends DBController {
	
    /**
     * Updates an existing reservation's details in the database.
     * Handles null-safe updates for table IDs, arrival times, and departure times.
     *
     * @param reservation the Reservation object containing updated data.
     * @return true if the update was successful, false otherwise.
     */
    public boolean updateReservation(Reservation reservation) {
        Connection con = getConnection();

        try (PreparedStatement pst = con.prepareStatement(
                "UPDATE reservation SET reservationDate = ?, reservationTime = ?, numOfGuests = ?, status = ?, tableID = ?, actualarrivaltime = COALESCE(?, actualarrivaltime), departuretiime = ? WHERE reservationID = ?"
            )) {
            pst.setDate(1, Date.valueOf(reservation.getReservationDate()));
            pst.setTime(2, Time.valueOf(reservation.getReservationTime()));
            pst.setInt(3, reservation.getNumOfGuests());
            pst.setString(4, reservation.getStatus().name());

            if (reservation.getTableID() != null) {
                pst.setInt(5, reservation.getTableID());
            } else {
                pst.setNull(5, java.sql.Types.INTEGER);
            }

            if (reservation.getActualArrivalTime() != null) {
                pst.setTime(6, Time.valueOf(reservation.getActualArrivalTime()));
            } else {
                pst.setNull(6, java.sql.Types.TIME); 
            }

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
    
    /**
     * Filters confirmed reservations for a specific date and time.
     *
     * @param date the date to check.
     * @param time the time to check.
     * @return a list of confirmed reservations at that moment.
     */
    public List<Reservation> getConfirmedReservationsAt(LocalDate date, LocalTime time) {
        List<Reservation> allReservations = readAllReservations(); 
        List<Reservation> result = new ArrayList<>();

        for (Reservation r : allReservations) {
            if (r.getStatus() == ReservationStatus.CONFIRMED &&
                r.getReservationDate().equals(date) &&
                r.getReservationTime().equals(time)) {
                result.add(r);
            }
        }

        return result;
    }
    
    /**
     * Retrieves all non-cancelled reservations for a given date.
     *
     * @param date the target date.
     * @return a list of reservations for that day.
     */
    public List<Reservation> getReservationsByDate(LocalDate date) {
        List<Reservation> reservations = new ArrayList<>();
        Connection con = getConnection();
        
        String sql = "SELECT * FROM reservation WHERE reservationDate = ? AND status != ?";
        
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setDate(1, java.sql.Date.valueOf(date));
            pst.setString(2, ReservationStatus.CANCELLED.name()); 

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                		boolean notified = rs.getInt("isNotified") == 1;
                    Reservation r = new Reservation(
                        rs.getInt("reservationID"),
                        rs.getInt("customerID"),
                        rs.getObject("tableID") != null ? rs.getInt("tableID") : null,
                        rs.getObject("BillId") != null ? rs.getInt("BillId") : null,
                        rs.getInt("numOfGuests"),
                        rs.getInt("confirmationCode"),
                        rs.getDate("reservationDate").toLocalDate(),
                        rs.getTime("reservationTime").toLocalTime(),
                        rs.getDate("reservationPlacedDate").toLocalDate(),
                        rs.getTime("reservationPlacedTime").toLocalTime(),
                        ReservationStatus.valueOf(rs.getString("status")),
                        notified
                    );

                    if (rs.getTime("actualarrivaltime") != null) {
                        r.setActualArrivalTime(rs.getTime("actualarrivaltime").toLocalTime());
                    }
              
                    if (rs.getTime("departuretiime") != null) {
                        r.setExpectedDepartureTime(rs.getTime("departuretiime").toLocalTime());
                    }

                    reservations.add(r);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    /**
     * Retrieves reservations based on the day of the week.
     *
     * @param day the Day enum (e.g., SUNDAY).
     * @return a list of reservations for all dates that fall on this day.
     */
    public List<Reservation> getReservationsByDay(Day day) {
        List<Reservation> reservations = new ArrayList<>();
        Connection con = getConnection();
        
        String sql = "SELECT * FROM reservation WHERE DAYOFWEEK(reservationDate) = ? AND status != ?";

        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, dayToMySQLDay(day));
            pst.setString(2, ReservationStatus.CANCELLED.name());

            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                		boolean notified = rs.getInt("isNotified") == 1;
                    Reservation r = new Reservation(
                        rs.getInt("reservationID"),
                        rs.getInt("customerID"),
                        rs.getObject("tableID") != null ? rs.getInt("tableID") : null,
                        rs.getObject("BillId") != null ? rs.getInt("BillId") : null,
                        rs.getInt("numOfGuests"),
                        rs.getInt("confirmationCode"),
                        rs.getDate("reservationDate").toLocalDate(),
                        rs.getTime("reservationTime").toLocalTime(),
                        rs.getDate("reservationPlacedDate").toLocalDate(),
                        rs.getTime("reservationPlacedTime").toLocalTime(),
                        ReservationStatus.valueOf(rs.getString("status")),
                        notified
                    );

                    if (rs.getTime("actualarrivaltime") != null) {
                        r.setActualArrivalTime(rs.getTime("actualarrivaltime").toLocalTime());
                    }
                    if (rs.getTime("departuretiime") != null) {
                        r.setExpectedDepartureTime(rs.getTime("departuretiime").toLocalTime());
                    }

                    reservations.add(r);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;
    }

    /**
     * Converts the internal Day enum to the corresponding MySQL DAYOFWEEK integer.
     */
    private int dayToMySQLDay(Day day) {
        switch (day) {
            case SUNDAY: return 1;
            case MONDAY: return 2;
            case TUESDAY: return 3;
            case WEDNESDAY: return 4;
            case THURSDAY: return 5;
            case FRIDAY: return 6;
            case SATURDAY: return 7;
            default: return 0;
        }
    }

    /**
     * Extracts time-related data (arrival, departure) for reports within a date range.
     *
     * @param startDate the beginning of the range.
     * @param endDate the end of the range.
     * @return a list of TimeData objects for analysis.
     */
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
    
    /**
     * Cancels a reservation in the database and sets the notification status.
     *
     * @param reservationID the ID of the reservation to cancel.
     * @param needsNotification if true, marks the notification as pending (isNotified = 0).
     * @return true if successful.
     */
    public boolean cancelReservationInDB(int reservationID, boolean needsNotification) {
        int notifiedValue = needsNotification ? 0 : 1;
        String sql = "UPDATE reservation SET status = ?, isNotified = ? WHERE reservationID = ?";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, enums.ReservationStatus.CANCELLED.name());
            pst.setInt(2, notifiedValue);
            pst.setInt(3, reservationID);

            int rows = pst.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            System.err.println("SQL Exception during cancel: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
	/**
	 * Retrieves a single reservation by its unique ID.
	 *
	 * @param ReservationId the reservation ID.
	 * @return the Reservation object if found, null otherwise.
	 */
	public Reservation GetReservation(int ReservationId) {
		Connection con = getConnection();
		Reservation r = null;
		
		try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `reservation` WHERE reservationID = ?")){
			pst.setInt(1, ReservationId);
			ResultSet rs = pst.executeQuery();
			
	        if (rs.next()) {
		        	boolean notified = rs.getInt("isNotified") == 1;
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
	
	    			r = new Reservation(reservationID,customerID,tableID,billID,numOfGuests,confirmation_code
	    											,reservationDate.toLocalDate(),reservationTime.toLocalTime(),
	    											reservationPlacedDate.toLocalDate(),reservationPlacedTime.toLocalTime(),status,notified);
		        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * Retrieves reservations that are active (CONFIRMED, PENDING, or SEATED) at a specific timestamp.
	 * Used primarily for table assignment and real-time occupancy checks.
	 *
	 * @param date the reservation date.
	 * @param time the reservation time.
	 * @return a list of active reservations.
	 */
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
	        		boolean notified = rs.getInt("isNotified") == 1;
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
	                enums.ReservationStatus.valueOf(rs.getString("status")),
	                notified
	            ));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
    /**
     * Checks for reservations that overlap with a proposed new booking period.
     *
     * @param date the date of the new booking.
     * @param startTime the start time of the new booking.
     * @param durationHours the expected duration of the stay.
     * @return a list of existing reservations that conflict with the time slot.
     */
	public List<Reservation> getReservationsOverlapping(LocalDate date, LocalTime startTime, int durationHours) {
	    List<Reservation> overlapping = new ArrayList<>();
	    List<Reservation> all = getReservationsByDate(date);
	    LocalTime newEnd = startTime.plusHours(durationHours);

	    for (Reservation r : all) {
	        if (r.getStatus() != ReservationStatus.CONFIRMED &&
	            r.getStatus() != ReservationStatus.PENDING &&
	            r.getStatus() != ReservationStatus.SEATED) {
	            continue;
	        }

	        LocalTime existingStart = r.getReservationTime();
	        LocalTime existingEnd = existingStart.plusHours(durationHours);

	        if (existingStart.isBefore(newEnd) && existingEnd.isAfter(startTime)) {
	            overlapping.add(r);
	        }
	    }
	    return overlapping;
	}

	/**
	 * Retrieves the full reservation history for a specific customer.
	 *
	 * @param customerId the ID of the customer.
	 * @return a list of their past and future reservations.
	 */
	public List<Reservation> getReservationsByCustomer(int customerId) {
	    List<Reservation> list = new ArrayList<>();
	    String sql = "SELECT * FROM reservation " +
	                 "WHERE customerID = ? AND status IN ('CONFIRMED','COMPLETED','CANCELLED','NOT_SHOWED')";

	    try (Connection con = getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {
	        pst.setInt(1, customerId);

	        ResultSet rs = pst.executeQuery();
	        while (rs.next()) {
	        		boolean notified = rs.getInt("isNotified") == 1;
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
	                enums.ReservationStatus.valueOf(rs.getString("status")),
	                notified
	            ));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
    /**
     * Reads every reservation record in the database.
     *
     * @return a comprehensive list of all reservations.
     */
	public List<Reservation> readAllReservations() {
	    List<Reservation> reservations = new ArrayList<>();
	    Connection con = getConnection();

	    try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `reservation`")) {
	        ResultSet rs = pst.executeQuery();

	        while (rs.next()) {
	        		boolean notified = rs.getInt("isNotified") == 1;
	            int reservationID = rs.getInt("reservationID");
	            Date reservationDate = rs.getDate("reservationDate");
	            Time reservationTime = rs.getTime("reservationTime");
	            int numOfGuests = rs.getInt("numOfGuests");
	            int confirmation_code = rs.getInt("confirmationCode");
	            enums.ReservationStatus status = enums.ReservationStatus.valueOf(rs.getString("status"));
	            int customerID = rs.getInt("customerID");
	            int tableID = rs.getInt("TableId");
	            Integer billID = rs.getObject("BillId", Integer.class);
	            Date reservationPlacedDate = rs.getDate("reservationPlacedDate");
	            Time reservationPlacedTime = rs.getTime("reservationPlacedTime");
	            Time actualArrivalTimeDB = rs.getTime("actualArrivalTime");

	            Reservation r = new Reservation(
	                    reservationID, customerID, tableID, billID, numOfGuests, confirmation_code,
	                    reservationDate.toLocalDate(), reservationTime.toLocalTime(),
	                    reservationPlacedDate.toLocalDate(), reservationPlacedTime.toLocalTime(), status, notified);

	            if (actualArrivalTimeDB != null) {
	                r.setActualArrivalTime(actualArrivalTimeDB.toLocalTime());
	            }
	            reservations.add(r);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return reservations;
	}

    /**
     * Checks if the user associated with a reservation has the SUBSCRIBER role.
     *
     * @param reservationID the reservation to check.
     * @return true if the customer is a subscriber.
     */
    public boolean isSubscriberByReservationId(int reservationID) {
    	Connection con = getConnection();
        try (PreparedStatement pst = con.prepareStatement("SELECT u.Role FROM user u JOIN reservation r ON u.UserId = r.CustomerId WHERE r.ReservationId = ?")) {
            pst.setInt(1, reservationID);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
            	return "SUBSCRIBER".equals(rs.getString("Role"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    /**
     * Counts how many completed reservations were made by subscribers within a date range.
     */
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
     * Inserts a new reservation and retrieves its auto-generated ID.
     *
     * @param r the reservation to insert.
     * @return true if successful.
     */
    public boolean insertReservation(Reservation r) {
    		try (Connection con = getConnection();
    			PreparedStatement pst = con.prepareStatement(
                "INSERT INTO `reservation` (customerID, tableId, billId, numOfGuests, confirmationCode, reservationDate, reservationTime, reservationPlacedDate, reservationPlacedTime, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                java.sql.Statement.RETURN_GENERATED_KEYS)) {

	        	if (r.getCustomerID() != null) {
	        	    pst.setInt(1, r.getCustomerID());
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

    /**
     * Creates a new reservation with a PENDING status for immediate seating/management.
     */
    public int createPendingReservation(int userId, Integer tableId, int numGuests) {
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(
                 "INSERT INTO reservation (customerID, TableId, numOfGuests, status, reservationDate, reservationTime, reservationPlacedDate, reservationPlacedTime) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
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
     * Counts total reservations within a specific date period.
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

    /**
     * Links a bill ID to a specific reservation once payment processing begins.
     */
	public boolean updateReservationBillId(int reservationId, int billId) {
	    Connection con = getConnection();
	    try (PreparedStatement pst = con.prepareStatement("UPDATE reservation SET billID = ? WHERE reservationID = ?")) {
	        pst.setInt(1, billId);
	        pst.setInt(2, reservationId);
	        return pst.executeUpdate() > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}
	
    /**
     * Retrieves all reservations made by a specific User object.
     *
     * @param user the User entity.
     * @return a list of their reservations.
     */
	public List<Reservation> GetAllUserReservations(User user) {
	    Connection con = getConnection();
	    List<Reservation> reservations = new ArrayList<>();

	    try (PreparedStatement pst = con.prepareStatement("SELECT * FROM `reservation` WHERE customerID = ?")) {
	        pst.setInt(1, user.getUserId());
	        ResultSet rs = pst.executeQuery();

	        while (rs.next()) {
	        		boolean notified = rs.getInt("isNotified") == 1;	
	            reservations.add(new Reservation(rs.getInt("reservationID"), rs.getInt("customerID"), rs.getInt("TableId"), rs.getInt("BillId"), rs.getInt("numOfGuests"), rs.getInt("confirmationCode"), rs.getDate("reservationDate").toLocalDate(), rs.getTime("reservationTime").toLocalTime(), rs.getDate("reservationPlacedDate").toLocalDate(), rs.getTime("reservationPlacedTime").toLocalTime(), enums.ReservationStatus.valueOf(rs.getString("status")), notified));
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return reservations;
	}

    /**
     * Updates the database to indicate that the customer has been notified about a change or cancellation.
     *
     * @param reservationID the ID of the reservation.
     * @return true if updated.
     */
	public boolean markAsNotified(int reservationID) {
	    String sql = "UPDATE reservation SET isNotified = 1 WHERE reservationID = ?";
	    try (Connection con = getConnection();
	         PreparedStatement pst = con.prepareStatement(sql)) {
	        pst.setInt(1, reservationID);
	        return pst.executeUpdate() > 0;
	    } catch (SQLException e) {
	        System.err.println("SQL Exception during markAsNotified: " + e.getMessage());
	        return false;
	    }
	}
}