package common;

import java.io.Serializable;

import enums.ActionType;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private ActionType action;
    private Object data;

    public Message(ActionType action, Object data) {
        this.action = action;
        this.data = data;
    }

    public ActionType getAction() { return action; }
    public Object getData() { return data; }
}
