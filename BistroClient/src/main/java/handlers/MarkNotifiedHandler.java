package handlers;

public class MarkNotifiedHandler implements ResponseHandler {
    @Override
    public void handle(Object data) {
        // We just log this or do nothing 
        // because the UI already updated after the Alert
        System.out.println("Server confirmed: Reservation marked as notified.");
    }
}