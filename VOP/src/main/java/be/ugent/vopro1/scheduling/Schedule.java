package be.ugent.vopro1.scheduling;

import be.ugent.vopro1.model.Assignment;
import be.ugent.vopro1.model.AvailableUser;
import be.ugent.vopro1.model.Task;
import be.ugent.vopro1.util.Interval;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides a schedule for analysts to work on a project
 */
public class Schedule {

    private Map<AvailableUser, Map<Interval, Task>> schedule = new HashMap<>();

    /**
     * A default constructor for a Schedule
     */
    public Schedule() {
    }

    /**
     * Constructs a Schedule from a list of assignments
     *
     * @param assignments The assignments that will make up the Schedule
     */
    public Schedule(List<Assignment> assignments) {
        assignments.forEach(this::addAssignment);
    }

    /**
     * Add an Assignment to the schedule
     *
     * @param assignment The assignment to add
     * @see Assignment
     */
    public void addAssignment(Assignment assignment) {
        AvailableUser user = assignment.getUser();
        Map<Interval, Task> map = schedule.get(user);
        System.out.println(user.hashCode());
        for (AvailableUser u : schedule.keySet()) {
            System.out.println(u.hashCode());
        }
        if (map == null) {
            map = new HashMap<>();

            schedule.put(user, map);
        }

        map.put(assignment.getInterval(), assignment.getTask());
    }

    /**
     * Check whether the analyst is busy in a certain interval
     *
     * @param analyst  The analyst of whom we want to know if he is busy
     * @param start    The start of the interval
     * @param duration The duration of the interval
     * @return whether {@link} is busy or not
     */
    public boolean isBusy(AvailableUser analyst, LocalDateTime start, long duration) {
        Interval newInterval = new Interval(start, duration);
        // Find the right tasks

        if (schedule.get(analyst) == null) {
            return false;
        }

        for (Interval interval : schedule.get(analyst).keySet()) {
            // New start is after busy start but before busy end
            if (start.isAfter(interval.getStart())
                    && start.isBefore(interval.getEnd())) {
                return true;
            }
            // New end is after busy start but before busy end
            else if (newInterval.getEnd().isAfter(interval.getStart())
                    && newInterval.getEnd().isBefore(interval.getEnd())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if a reschedule of this task is needed given a date at which we start rescheduling
     *
     * @param task  The task we want to evaluate
     * @param start The date at which we start rescheduling
     * @return If the task has to be rescheduled
     */
    public boolean isRescheduleNeeded(Task task, LocalDateTime start) {
        Interval interval = null;
        AvailableUser availableUser = null;
        for (AvailableUser key : schedule.keySet()) {
            // We found something and broke out of the inner loop
            if (interval != null) {
                break;
            }
            for (Interval intervalKey : schedule.get(key).keySet()) {
                if (schedule.get(key).get(intervalKey).equals(task)) {
                    interval = intervalKey;
                    availableUser = key;
                    break;
                }
            }
        }

        // Not yet assigned
        if (availableUser == null) {
            return true;
        }
        // Not yet processed
        else if (interval == null || interval.getStart() == null) {
            return true;
        }
        // Should've been done already
        else if (interval.getEnd().isBefore(start) || interval.getEnd().isEqual(start)) {
            return false;
        }
        // Is still ongoing
        else if (interval.getEnd().isAfter(start) && start.isBefore(interval.getStart())) {
            return false;
        }

        return true;
    }

    /**
     * Merges all Assignments from otherSchedule that start before start into this schedule.
     *
     * @param otherSchedule The schedule who's unique Assignments we want
     * @param start         The date that shows when this schedule starts
     * @see Assignment
     */
    public void mergeSchedule(Schedule otherSchedule, LocalDateTime start) {
        if (otherSchedule != null) {
            if (otherSchedule.isEmpty()) {
                otherSchedule.getAssignments().forEach(this::addAssignment);
            } else {
                otherSchedule.getAssignmentsBefore(start).forEach(this::addAssignment);
            }
        }
    }

    /**
     * A getter for the assignments in this schedule
     *
     * @return The assignments in this schedule
     */
    public List<Assignment> getAssignments() {
        return getAssignmentsBefore(LocalDateTime.MAX);
    }

    /**
     * Returns assignments that start before start
     *
     * @param start The maximum date that assignments can start
     * @return The assignments that start before start
     */
    public List<Assignment> getAssignmentsBefore(LocalDateTime start) {
        List<Assignment> assignments = new ArrayList<>();

        for (AvailableUser user : schedule.keySet()) {
            Set<Interval> intervals = schedule.get(user).keySet();
            assignments.addAll(intervals.stream()
                            .filter(
                                    interval -> interval.getStart().isBefore(start)
                                            || interval.getStart().isEqual(start)
                            )
                            .map(interval -> new Assignment(user, interval, schedule.get(user).get(interval)))
                    .collect(Collectors.toList()));
        }

        return assignments;
    }

    /**
     * Checks if their are any assignments in this Schedule
     *
     * @return If the schedule is empty or not
     */
    public boolean isEmpty() {
        return schedule.isEmpty();
    }
}
