package messages;

import java.io.Serializable;

import Entities.User;

@SuppressWarnings("serial")
public class AddUserRequest implements Serializable {
	private User user;

    public AddUserRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}
