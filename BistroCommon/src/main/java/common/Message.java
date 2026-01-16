package common;

import java.io.Serializable;
import enums.ActionType;

/**
 * Represents a message sent between the client and server.
 * Contains an action type indicating the purpose of the message
 * and an optional data object with associated information.
 */
public class Message implements Serializable {

    /**
     * Serialization identifier for version control.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The type of action this message represents.
     */
    private ActionType action;

    /**
     * Optional data associated with the action.
     */
    private Object data;

    /**
     * Constructs a new Message with the specified action and data.
     *
     * @param action the action type of the message
     * @param data   the data associated with the action
     */
    public Message(ActionType action, Object data) {
        this.action = action;
        this.data = data;
    }

    /**
     * Returns the action type of this message.
     *
     * @return the action type
     */
    public ActionType getAction() {
        return action;
    }

    /**
     * Returns the data associated with this message.
     *
     * @return the data object
     */
    public Object getData() {
        return data;
    }
}
