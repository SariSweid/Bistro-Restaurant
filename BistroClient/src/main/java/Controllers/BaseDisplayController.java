package Controllers;

import java.util.List;
import Entities.Reservation;

public abstract class BaseDisplayController {
    // Called by ClientHandler when reservations are received
    public abstract void showReservations(List<Reservation> list);
}
