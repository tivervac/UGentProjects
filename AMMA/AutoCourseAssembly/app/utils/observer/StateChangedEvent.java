package utils.observer;

import models.ParserTask;
import models.ParserTask.Status;

/**
 * @author Titouan Vervack
 */
public class StateChangedEvent {
    private Status status;
    private String message;

    public StateChangedEvent(ParserTask.Status status) {
        this(status, "");
    }
    public StateChangedEvent(ParserTask.Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public ParserTask.Status status() {
        return status;
    }

    public String message() {
        return message;
    }
}
