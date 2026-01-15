package messages;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ForgotCodeRequest implements Serializable {

    private Integer userId;

    public ForgotCodeRequest(Integer userId) {
        this.userId = userId;
    }

    public Integer getUserId() {
        return userId;
    }
}
