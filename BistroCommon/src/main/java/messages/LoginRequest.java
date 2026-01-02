package messages;

import java.io.Serializable;

public class LoginRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int userID;
    private final int membershipCode;

    public LoginRequest(int userID, int membershipCode) {
        this.userID = userID;
        this.membershipCode = membershipCode;
    }

    public int getUserID() { return userID; }
    public int getMembershipCode() { return membershipCode; }
}
