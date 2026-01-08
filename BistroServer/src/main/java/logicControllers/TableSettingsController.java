package logicControllers;

import DB.DBController;

public class TableSettingsController {

    private final DBController db = new DBController();

    public boolean insertTable(int id, int seats) {
        try {
            return db.insertTable(id, seats);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateTable(int id, int seats) {
        try {
            return db.updateTable(id, seats);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteTable(int id) {
        try {
            return db.deleteTable(id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
