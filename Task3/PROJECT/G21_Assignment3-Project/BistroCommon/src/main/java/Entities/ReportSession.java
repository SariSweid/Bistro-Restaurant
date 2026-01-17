package Entities;

/**
 * Holds the current session information for generating reports.
 * This class stores the month and year for which the report should be generated.
 * All fields and methods are static, so the values are shared across the application.
 */
public class ReportSession {

    private static int month;
    private static int year;

    /**
     * Sets the month for the report session.
     *
     * @param m the month (1-12)
     */
    public static void setMonth(int m) {
        month = m;
    }

    /**
     * Sets the year for the report session.
     *
     * @param y the year
     */
    public static void setYear(int y) {
        year = y;
    }

    /**
     * Returns the month of the current report session.
     *
     * @return the month (1-12)
     */
    public static int getMonth() {
        return month;
    }

    /**
     * Returns the year of the current report session.
     *
     * @return the year
     */
    public static int getYear() {
        return year;
    }
}
