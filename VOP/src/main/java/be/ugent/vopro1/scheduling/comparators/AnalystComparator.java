package be.ugent.vopro1.scheduling.comparators;

import be.ugent.vopro1.model.AvailableUser;
import be.ugent.vopro1.scheduling.FirstFitScheduler;

import java.util.Comparator;

/**
 * A comparator for analysts, sorts them on lowest work first.
 * This allows them to be used in {@link FirstFitScheduler}
 *
 * @see AvailableUser
 * @see FirstFitScheduler
 */
public class AnalystComparator implements Comparator<AvailableUser> {

    /**
     * Compares two users and sorts them on lowest work first.
     *
     * @param user      The first user
     * @param otherUser The second user
     * @return 0 if the work is equal, &lt; 0 if otherUser has more work and &gt; 0 otherwise
     */
    @Override
    public int compare(AvailableUser user, AvailableUser otherUser) {
        return (int) (user.getWork() - otherUser.getWork());
    }
}
