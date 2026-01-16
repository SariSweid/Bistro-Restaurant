package handlers;

import java.util.List;

import Controllers.SubscriberInformationController;
import Entities.Subscriber;
import Entities.User;
import javafx.application.Platform;

/**
 * Handles the server response containing a list of all users.
 * Filters the list to include only subscribers and forwards the result
 * to the active SubscriberInformationController for display.
 */
public class GetAllUsersHandler implements ResponseHandler {

    /**
     * Processes the server response data.
     * Expects a list of User objects, filters only Subscriber instances,
     * and updates the UI through the active SubscriberInformationController.
     *
     * @param data the server response data, expected to be a List of User objects
     */
    @Override
    public void handle(Object data) {
        if (!(data instanceof List<?> list)) return;

        @SuppressWarnings("unchecked")
        List<User> users = (List<User>) list;

        Platform.runLater(() -> {
            var controller = ClientHandler.getClient().getActiveDisplayController();
            if (controller instanceof SubscriberInformationController subController) {

                List<Subscriber> subscribers = users.stream()
                        .filter(u -> u instanceof Subscriber)
                        .map(u -> (Subscriber) u)
                        .toList();

                subController.showUsers(subscribers);
            }
        });
    }
}
