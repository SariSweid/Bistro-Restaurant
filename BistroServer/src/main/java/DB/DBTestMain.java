package DB;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import Entities.*;
import enums.ReservationStatus;
import enums.ReportType;
import enums.Day;

public class DBTestMain {

    public static void main(String[] args) {

        DBController db = new DBController();

        System.out.println("====== DB TEST START ======");

        /* ---------- Reservation ---------- */
        System.out.println("\n--- Reservation test ---");

        Reservation r = new Reservation(
        	    1001,                   // reservationID
        	    2001,                   // customerID
        	    1,                   // tableID עדיין לא הוקצה
        	    1,                   // billID עדיין לא קיים
        	    4,                      // numOfGuests
        	    5555,                   // confirmationCode
        	    LocalDate.now().plusDays(1),  // reservationDate
        	    LocalTime.of(19, 30),        // reservationTime
        	    LocalDate.now(),              // reservationPlacedDate
        	    LocalTime.now(),              // reservationPlacedTime
        	    ReservationStatus.CONFIRMED   // status
        	);

        boolean inserted = db.insertReservation(r);
        System.out.println("Insert reservation: " + inserted);

        List<Reservation> allRes = db.readAllReservations();
        System.out.println("Total reservations: " + allRes.size());

        Reservation fetched = db.GetReservation(1001);
        System.out.println("Fetched reservation: " + (fetched != null));

        r.setNumOfGuests(5);
        boolean updated = db.updateReservation(r);
        System.out.println("Update reservation: " + updated);

        /* ---------- Report ---------- */
        System.out.println("\n--- Report test ---");

        Report report = new Report(
                1,
                ReportType.SCHEDULE,
                LocalDate.now().minusDays(7),
                LocalDate.now(),
                LocalDateTime.now(),
                "Test report content"
        );

        boolean reportAdded = db.AddReport(report);
        System.out.println("Add report: " + reportAdded);

        List<Report> reports = db.GetAllReports();
        System.out.println("Reports count: " + reports.size());

        /* ---------- RestaurantSettings ---------- */
        System.out.println("\n--- RestaurantSettings test ---");

        RestaurantSettings settings = RestaurantSettings.getInstance();
        settings.setDay(Day.MONDAY);
        settings.setOpeningTime(LocalTime.of(9, 0));
        settings.setClosingTime(LocalTime.of(22, 0));
        settings.setMaxTables(20);

        System.out.println("Update opening hours: " + db.updateOpeningHours(settings));
        System.out.println("Update closing hours: " + db.updateClosingHours(settings));
        System.out.println("Update max tables: " + db.updateMaxTable(settings));

        /* ---------- SpecialDates ---------- */
        System.out.println("\n--- SpecialDates test ---");

        SpecialDates sd = new SpecialDates(
                LocalDate.of(2026, 1, 1),
                LocalTime.of(12, 0),
                LocalTime.of(18, 0),
                "Holiday hours"
        );

        System.out.println("Add special date: " + db.addSpecialDates(sd));
        System.out.println("Update special date: " + db.updateSpecialDates(sd));

        /* ---------- Bill ---------- */
        System.out.println("\n--- Bill test ---");

        Bill bill = new Bill(
                3001,
                1001,
                450.0,
                LocalDateTime.now(),
                false
        );

        System.out.println("Add bill: " + db.AddBill(bill));

        Bill fetchedBill = db.GetBill(3001);
        System.out.println("Fetched bill: " + (fetchedBill != null));

        /* ---------- Tables ---------- */
        System.out.println("\n--- Table test ---");

        List<Table> tables = db.GetAllTables();
        System.out.println("Tables count: " + tables.size());

        if (!tables.isEmpty()) {
            Table t = tables.get(0);
            t.release();
            System.out.println("Update table availability: " + db.UpdateTable(t));
        }

        /* ---------- Waiting List ---------- */
        System.out.println("\n--- Waiting list test ---");

        WaitingListEntry w = new WaitingListEntry(
        	    2001,
        	    "050-1234567",
        	    4,
        	    9999
        	);

        System.out.println("Add to waiting list: " + db.addToWitingList(w));
        System.out.println("Notify table available: " + safeNotify(db));
        System.out.println("Remove from waiting list: " + db.removeFromWitingList(w));

        System.out.println("\n====== DB TEST END ======");
    }

    private static boolean safeNotify(DBController db) {
        try {
            return db.notifyTableIsAvailable();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
