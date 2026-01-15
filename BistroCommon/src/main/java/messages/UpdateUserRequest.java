package messages;

import java.io.Serializable;

import Entities.User;

@SuppressWarnings("serial")
public class UpdateUserRequest implements Serializable {
	private final User user;
	
	public UpdateUserRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
