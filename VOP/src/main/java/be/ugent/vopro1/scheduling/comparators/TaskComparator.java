package be.ugent.vopro1.scheduling.comparators;

import be.ugent.vopro1.model.Task;
import be.ugent.vopro1.scheduling.FirstFitScheduler;

import java.util.Comparator;

/**
 * A comparator for Tasks, sorts them on highest priority first and on
 * highest workload first if the priorities are equal.
 * This allows them to be used in {@link FirstFitScheduler}
 *
 * @see Task
 * @see FirstFitScheduler
 */
public class TaskComparator implements Comparator<Task> {

    /**
     * Compares two Tasks and sorts them on highest priority (or workload if priorities are equal) first.
     *
     * @param task The first task
     * @param otherTask The second task
     * @return 0 if the priorities and workloads are equal,
     * &lt; 0 if otherProcess has a higher priority or the priorities are equal
     * and the workload of otherProcess is higher
     * and &gt; 0 otherwise
     */
    @Override
    public int compare(Task task, Task otherTask) {
        if (task.getPriority() == otherTask.getPriority()) {
            return (int) (task.getWorkload() - otherTask.getWorkload());
        } else {
            return task.getPriority() - otherTask.getPriority();
        }
    }
}
