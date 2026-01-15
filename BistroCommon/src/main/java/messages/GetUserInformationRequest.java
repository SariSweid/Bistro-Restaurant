package messages;

import java.io.Serializable;

@SuppressWarnings("serial")
public class GetUserInformationRequest implements Serializable {
	private final int userID;

    public GetUserInformationRequest(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
    }
}
