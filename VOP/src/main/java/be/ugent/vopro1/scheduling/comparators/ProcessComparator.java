package be.ugent.vopro1.scheduling.comparators;

import be.ugent.vopro1.model.ProcessEntity;
import be.ugent.vopro1.scheduling.FirstFitScheduler;

import java.util.Comparator;

/**
 * A comparator for ProcessEntities, sorts them on highest priority first.
 * This allows them to be used in {@link FirstFitScheduler}
 *
 * @see ProcessEntity
 * @see FirstFitScheduler
 */
public class ProcessComparator implements Comparator<ProcessEntity> {

    /**
     * Compares two ProcessEntities and sorts them on highest priority first.
     *
     * @param process The first process
     * @param otherProcess The second process
     * @return 0 if the priorities are equal, &lt; 0 if otherProcess has a higher priority and &gt; 0 otherwise
     */
    @Override
    public int compare(ProcessEntity process, ProcessEntity otherProcess) {
        return process.getPriority() - otherProcess.getPriority();
    }
}
