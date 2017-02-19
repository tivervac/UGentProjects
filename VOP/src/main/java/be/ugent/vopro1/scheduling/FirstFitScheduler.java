package be.ugent.vopro1.scheduling;

import be.ugent.vopro1.model.Assignment;
import be.ugent.vopro1.model.AvailableUser;
import be.ugent.vopro1.model.Task;
import be.ugent.vopro1.scheduling.comparators.AnalystComparator;
import be.ugent.vopro1.util.Interval;
import be.ugent.vopro1.util.error.RequirementNotMetException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

import static be.ugent.vopro1.util.LocalConstants.*;

/**
 * A first-fit approach to the use-case schedule problem.
 * The algorithm will try to schedule the analysts with the fewest hours left
 * to the use-case with the highest workload.
 */
public class FirstFitScheduler implements Scheduler {

    private final AnalystComparator analystComparator;
    private boolean locked;

    /**
     * Creates a new FirstFitScheduler
     */
    public FirstFitScheduler() {
        analystComparator = new AnalystComparator();
        locked = false;
    }

    /**
     * {@inheritDoc}
     *
     * @param analysts {@inheritDoc}
     * @param tasks    {@inheritDoc}
     * @param start    {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Schedule schedule(List<AvailableUser> analysts, List<Task> tasks, LocalDateTime start) {
        Schedule schedule = new Schedule();
        LocalDateTime oldStart = null;
        // Amount of analysts that are unable to work on a task
        int retiredAnalysts = 0;

        // Start at the start of the workday and make sure we're not trying to schedule the past
        if (start.isBefore(LocalDateTime.now()) || start.getHour() * 60 + start.getMinute() < START_WORK_DAY) {
            start = start.withHour((int) Math.floor(START_WORK_DAY / 60));
            start = start.withMinute((int) (START_WORK_DAY % 60));
            // No working in weekends
            if (start.getDayOfWeek() == DayOfWeek.SATURDAY) {
                start = start.plusDays(2);
            } else if (start.getDayOfWeek() == DayOfWeek.SUNDAY) {
                start = start.plusDays(1);
            }
        }

        // Sort analysts on the time they can work on the project
        analysts.sort(analystComparator);

        // Schedule the tasks
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            int tiredAnalysts = 0;
            boolean assigned = false;

            // Look for an analyst for this use-case
            for (int j = 0; j < analysts.size(); j++) {
                AvailableUser analyst = analysts.get(j);
                // Check if there is anyone left that is able to do this use-case
                if (analyst.getWork() < task.getWorkload()) {
                    tiredAnalysts++;
                } else {
                    // Try to schedule the use-case
                    long workload = task.getWorkload();
                    if (!schedule.isBusy(analyst, start, workload)) {
                        // Calculate the overtime
                        long overTime = start.getHour() * 3600
                                + start.getMinute() * 60
                                + start.getSecond()
                                + workload
                                - STOP_WORK_DAY * 60;

                        // Do a part of the work
                        if (overTime > 0) {
                            // Check how much work we can still do today
                            workload = workload - overTime;
                            // Make sure this analyst finishes this task
                            locked = true;

                            // Remember the old start
                            if (oldStart == null) {
                                oldStart = start;
                            }
                            // Make sure the analysts can finish his task
                            j--;
                        }

                        analyst.reduceWork(workload);
                        schedule.addAssignment(new Assignment(analyst, new Interval(start, workload), task));
                        task.setWorkload(task.getWorkload() - workload);

                        // The analyst can not work anymore
                        analysts.removeIf(AvailableUser::isTired);
                        // TODO: Get rid of this giant overhead
                        // Sort all over again so we can continue our best fit
                        analysts.sort(analystComparator);
                        // No more need to look for other analysts
                        if (!locked) {
                            assigned = true;
                            break;
                        }
                        // The locked task has been completed
                        else if (task.getWorkload() <= 0) {
                            start = oldStart;
                            oldStart = null;
                            locked = false;
                            assigned = true;
                            break;
                        } // A task is locked and it's not done yet, so increment the day
                        else {
                            start = setNewDay(start);
                        }
                    }
                }
            }

            // There are analysts that can work on this task but they are busy right now.
            // Try again SCHEDULE_RETRY_TIME minutes later
            if (tiredAnalysts < analysts.size() && !assigned) {
                i--;
                start = start.plusMinutes(SCHEDULE_RETRY_TIME);
                // The workday is over
                if (start.getHour() * 60 + start.getMinute() > STOP_WORK_DAY) {
                    start = setNewDay(start);
                }
            } else if(tiredAnalysts == analysts.size() && !assigned) {
                retiredAnalysts++;
            }
        }

        if (retiredAnalysts == analysts.size()) {
            throw new RequirementNotMetException("None of the analysts can work on any of the use cases. " +
                    "Try increasing the analysts' work time.");
        }

        return schedule;
    }

    private LocalDateTime setNewDay(LocalDateTime start) {
        // Increment to the next day, skipping the weekend
        start = start.plusDays((start.getDayOfWeek() == DayOfWeek.FRIDAY) ? 3 : 1);
        start = start.withHour((int) Math.floor(START_WORK_DAY / 60));
        return start.withMinute((int) (START_WORK_DAY % 60));
    }
}
