package handlers;

import java.util.List;

import Controllers.SubscriberInformationController;
import Entities.Subscriber;
import Entities.User;
import javafx.application.Platform;

public class GetAllUsersHandler implements ResponseHandler {

	@Override
	public void handle(Object data) {
	    if (!(data instanceof List<?> list)) return;

	    @SuppressWarnings("unchecked")
		List<User> users = (List<User>) list;

	    Platform.runLater(() -> {
	        var controller = ClientHandler.getClient().getActiveDisplayController();
	        if (controller instanceof SubscriberInformationController subController) {

	            // FILTER ONLY SUBSCRIBERS
	            List<Subscriber> subscribers = users.stream()
	                .filter(u -> u instanceof Subscriber)
	                .map(u -> (Subscriber) u)
	                .toList();

	            subController.showUsers(subscribers);
	        }
	    });
	}

}
