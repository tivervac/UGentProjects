package be.ugent.vopro1.scheduling;

import be.ugent.vopro1.model.AvailableUser;
import be.ugent.vopro1.model.Task;

import java.time.LocalDateTime;
import java.util.List;

/**
 * An interface for a Scheduler. A scheduler can create Schedule's and can return the current Schedule.
 * @see Schedule
 */
public interface Scheduler {

    /**
     * Schedules analysts to tasks from a certain date
     *
     * @param analysts The analysts to schedule to tasks
     * @param tasks The tasks to be scheduled
     * @param start The date at which we start scheduling
     * @return The constructed schedule
     */
    Schedule schedule(List<AvailableUser> analysts, List<Task> tasks, LocalDateTime start);
}
