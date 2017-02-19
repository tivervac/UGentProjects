package be.ugent.vopro1.model;

import be.ugent.vopro1.util.Interval;

import java.time.LocalDateTime;

/**
 * An Assignment of a Task to a User
 *
 * @see AvailableUser
 * @see Task
 */
public class Assignment {

    private AvailableUser user;
    private Interval interval;
    private Task task;

    /**
     * The constructor for an Assignment
     *
     * @param user The User to perform the Task
     * @param interval The Interval during which the task should be completed
     * @param task The task to be performed by {@link #user} during {@link #interval}
     */
    public Assignment(AvailableUser user, Interval interval, Task task) {
        this.user = user;
        this.interval = interval;
        this.task = task;
    }

    /**
     * A getter for the Interval
     *
     * @return The interval in which the Task should be completed
     */
    public Interval getInterval() {
        return interval;
    }

    /**
     * A setter for the Interval
     *
     * @param interval The interval in which the Task should be completed
     */
    public void setInterval(Interval interval) {
        this.interval = interval;
    }

    /**
     * A getter for the start date of the Interval
     *
     * @return The start date of the Interval
     */
    public LocalDateTime getStartDate() {
        return interval.getStart();
    }

    /**
     * A setter for the start date of the Interval
     *
     * @param start The start date of the Interval
     */
    public void setStartDate(LocalDateTime start) {
        interval.setStart(start);
    }

    /**
     * A getter for the end date of the Interval
     *
     * @return The end date of the Interval
     */
    public LocalDateTime getEnd() {
        return interval.getEnd();
    }

    /**
     * A setter for the end date of the Interval
     *
     * @param end The end date of the Interval
     */
    public void setEndDate(LocalDateTime end) {
        interval.setEnd(end);
    }

    /**
     * A getter for the AvailableUser
     *
     * @return The AvailableUser who should perform the Task
     */
    public AvailableUser getUser() {
        return user;
    }

    /**
     * A setter for the AvailableUser
     *
     * @param user The AvailableUser to perform the Task
     */
    public void setUser(AvailableUser user) {
        this.user = user;
    }

    /**
     * A getter for the Task
     *
     * @return The Task that should be completed
     */
    public Task getTask() {
        return task;
    }

    /**
     * A setter for the Task
     *
     * @param task The Task that should be completed
     */
    public void setTask(Task task) {
        this.task = task;
    }

    /**
     * Checks if the assignments are equal
     *
     * @param o The other Assignment
     * @return If the assignments are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Assignment that = (Assignment) o;

        if (user != null ? !user.equals(that.user) : that.user != null) return false;
        if (interval != null ? !interval.equals(that.interval) : that.interval != null) return false;
        return !(task != null ? !task.equals(that.task) : that.task != null);

    }

    /**
     * Generates a unique code for this Assignment
     *
     * @return The unique code
     */
    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (interval != null ? interval.hashCode() : 0);
        result = 31 * result + (task != null ? task.hashCode() : 0);
        return result;
    }
}
