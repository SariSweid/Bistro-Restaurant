package messages;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class AddToWaitingListRequest implements Serializable {

    private Integer userID;    
    private String email;      
    private String phone;      
    private int numOfGuests;
    private LocalDate date;   
    private LocalTime time;   

    public AddToWaitingListRequest(Integer userID, String email, String phone,
                                   int numOfGuests, LocalDate date, LocalTime time) {
        this.userID = userID;
        this.email = email;
        this.phone = phone;
        this.numOfGuests = numOfGuests;
        this.date = date;
        this.time = time;
    }

    // Getters
    public Integer getUserID() { return userID; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public int getNumOfGuests() { return numOfGuests; }
    public LocalDate getDate() { return date; }
    public LocalTime getTime() { return time; }
}
