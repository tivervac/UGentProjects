package be.ugent.vopro1.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * Creates an Interval from a start date and an end date
 */
public class Interval {

    public static final String START_SHOULD_BE_BEFORE_END = "Start should be before end.";
    private LocalDateTime start;
    private LocalDateTime end;

    /**
     * Constructor for an Interval
     *
     * @param start The start of the Interval
     * @param end   The end of the Interval
     */
    public Interval(LocalDateTime start, LocalDateTime end) {
        setStartAndEnd(start, end);
    }

    /**
     * Constructor for an Interval
     *
     * @param start The start of the Interval
     * @param end   The end of the Interval
     */
    public Interval(Date start, Date end) {
        setStartAndEnd(toLDT(start), toLDT(end));
    }

    /**
     * Constructor for an Interval
     *
     * @param start    The start of the Interval
     * @param duration The duration of the Interval in seconds
     */
    public Interval(LocalDateTime start, long duration) {
        LocalDateTime end = start.plus(duration, ChronoUnit.SECONDS);
        setStartAndEnd(start, end);
    }

    /**
     * A getter for the start date as {@link Date}
     *
     * @return The start date
     */
    public Date getStartDate() {
        return toDate(start);
    }

    /**
     * A getter for the start date
     *
     * @return The start date
     */
    public LocalDateTime getStart() {
        return start;
    }

    /**
     * A setter for the start date with some sanity checking
     *
     * @param start The start date
     */
    public void setStart(LocalDateTime start) {
        if (start == null) {
            throw new IllegalArgumentException("Start can not be null.");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException(START_SHOULD_BE_BEFORE_END);
        }

        this.start = start;
    }

    /**
     * A setter for both start and end
     *
     * @param start The start date
     * @param end   The end date
     */
    private void setStartAndEnd(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start nor end can be null");
        }

        if (start.isAfter(end)) {
            throw new IllegalArgumentException(START_SHOULD_BE_BEFORE_END);
        }

        this.start = start;
        this.end = end;
    }

    /**
     * A getter for the end date as {@link Date}
     *
     * @return The end date
     */
    public Date getEndDate() {
        return toDate(end);
    }

    /**
     * A getter for the end date
     *
     * @return The end date
     */
    public LocalDateTime getEnd() {
        return end;
    }

    /**
     * A setter for the end date with some sanity checking
     *
     * @param end The end date
     */
    public void setEnd(LocalDateTime end) {
        if (end == null) {
            throw new IllegalArgumentException("End can not be null.");
        }

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End should be after start.");
        }

        this.end = end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Interval interval = (Interval) o;

        if (getStart() != null ? !getStart().equals(interval.getStart()) : interval.getStart() != null) {
            return false;
        }
        return !(getEnd() != null ? !getEnd().equals(interval.getEnd()) : interval.getEnd() != null);

    }

    @Override
    public int hashCode() {
        int result = getStart() != null ? getStart().hashCode() : 0;
        result = 31 * result + (getEnd() != null ? getEnd().hashCode() : 0);
        return result;
    }

    /**
     * Converts Date to LocalDateTime
     *
     * @param date The date to convert
     * @return The Date converted to LocalDateTime
     */
    private LocalDateTime toLDT(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Converts LocalDateTime to Date
     *
     * @param localDateTime The date to convert
     * @return The LocalDateTime converted to Date
     */
    private Date toDate(LocalDateTime localDateTime) {
        Instant instant = localDateTime.atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
}
