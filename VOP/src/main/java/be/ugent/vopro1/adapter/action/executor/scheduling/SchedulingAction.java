package be.ugent.vopro1.adapter.action.executor.scheduling;

import be.ugent.vopro1.adapter.action.Action;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * The representation of a scheduling request
 * TODO: add Jackson module (JSR-310) to deserialize LocalDateTime
 */
public class SchedulingAction extends Action {
    private Date start;

    /**
     * A getter for the start date
     *
     * @return The date from which the scheduling needs to happen
     */
    public LocalDateTime getStart() {
        return LocalDateTime.ofInstant(start.toInstant(), ZoneId.systemDefault());
    }

    /**
     * A setter for the start date
     *
     * @param start The date from which the scheduling needs to happen
     */
    public void setDate(Date start) {
        this.start = start;
    }
}
