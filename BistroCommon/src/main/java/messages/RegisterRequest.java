package messages;

import java.io.Serializable;

import enums.UserRole;

public class RegisterRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private final int userID;
    private final String name;
    private final String email;
    private final String phone;
    private final String username;
    private final int membershipCode;
    private final UserRole role;

    // Constructor
    public RegisterRequest(int userID, String name, String email, String phone,
                           String username, int membershipCode,UserRole role) {
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.username = username;
        this.membershipCode = membershipCode;
		this.role = role;
    }

    // Getters
    public int getUserID() { return userID; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getUsername() { return username; }
    public int getMembershipCode() { return membershipCode; }
	public UserRole getRole() {return role;}
    
}
