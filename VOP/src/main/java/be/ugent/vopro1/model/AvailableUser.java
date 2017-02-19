package be.ugent.vopro1.model;

/**
 * Wrapper for User that offers the possibility to work
 *
 * @see User
 */
public class AvailableUser {

    private User user;
    private long work;

    /**
     * Constructor for AvailableUser
     *
     * @param user The user to make workable
     * @param work The work a user wants to put into a project
     */
    public AvailableUser(User user, long work) {
        setUser(user);
        setWork(work);
    }

    /**
     * A getter for the User
     *
     * @return The User
     */
    public User getUser() {
        return user;
    }

    /**
     * A setter for the User
     *
     * @param user The User
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * A getter for the amount of work a User can do
     *
     * @return The amount of work a User can do, in seconds
     */
    public long getWork() {
        return work;
    }

    /**
     * A setter for the amount of work a User can do
     *
     * @param work The amount of work a User can do, in seconds
     */
    public void setWork(long work) {
        this.work = work;
    }

    /**
     * Checks if the user can still work
     *
     * @return If the user can still work or not
     */
    public boolean isTired() {
        return work <= 0;
    }

    /**
     * Reduces the time a user can work
     *
     * @param reduction The reduction in seconds
     */
    public void reduceWork(long reduction) {
        work -= reduction;
    }

    /**
     * Checks if two AvailableUser's are the same
     *
     * @param o The other AvailableUser
     * @return If the users are the same or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AvailableUser that = (AvailableUser) o;

        if (work != that.work) {
            return false;
        }

        return user.equals(that.user);

    }

    /**
     * Creates a unique code for this class
     *
     * @return The unique code
     */
    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result;
        return result;
    }
}
