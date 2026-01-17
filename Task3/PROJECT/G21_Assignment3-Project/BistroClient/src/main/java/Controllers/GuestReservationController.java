package Controllers;

import handlers.ClientHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import util.SceneManager;

/**
 * Controller for guest reservation operations in the UI.
 * Handles navigation for guests including going back to the main menu,
 * opening the cancel reservation popup, and making a new reservation.
 * Maintains the current guest ID for passing to other controllers.
 */
public class GuestReservationController {

    /**
     * The ID of the current guest session.
     */
    private int currentGuestId;

    /**
     * Sets the current guest ID for this session.
     *
     * @param guestId the ID of the guest currently using the application
     */
    public void setCurrentGuestId(int guestId) {
        this.currentGuestId = guestId;
    }

    /**
     * Navigates back to the main menu UI.
     * Triggered by the "Previous" button in the UI.
     */
    @FXML
    private void onPreviousPage() {
        SceneManager.switchTo("MainMenuUI.fxml");
    }

    /**
     * Opens the "Cancel Reservation" popup for the current guest.
     * Loads the GuestCancelReservationController and passes the current guest ID.
     */
    @FXML
    private void onCancelOrder() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/GuestCancelReservationUI.fxml"));
            Parent root = loader.load();

            GuestCancelReservationController controller = loader.getController();
            controller.setGuestId(currentGuestId);

            ClientHandler.getClient().setActiveDisplayController(controller);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Cancel Reservation");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Navigates to the "Make Reservation" UI for the guest.
     * Triggered by the "Order Table" button in the UI.
     */
    @FXML
    private void onOrderTable() {
        SceneManager.switchTo("GuestMakeReservationUI.fxml");
    }

}
