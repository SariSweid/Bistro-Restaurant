package logicControllers;

import DAO.TableDAO;

public class TableSettingsController {

    private final TableDAO db = new TableDAO();

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
