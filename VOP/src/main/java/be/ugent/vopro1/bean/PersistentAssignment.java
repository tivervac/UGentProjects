package be.ugent.vopro1.bean;

import be.ugent.vopro1.util.Interval;

/**
 * Provides a representation of a persistent Task.
 *
 * @see be.ugent.vopro1.persistence.jdbc.postgresql.ScheduleDAOImpl
 */
public class PersistentAssignment {

    private int userId;
    private Interval interval;
    private int taskId;

    /**
     * Creates a new PersistentAssignment with given identifier. This should only
     * be used in DAO classes, nowhere else!
     *
     * @param userId  The user of assigned to a task
     * @param taskId  The task assigned to a user
     * @param interval The date interval in which the task should be completed by the user
     */
    public PersistentAssignment(int userId, int taskId, Interval interval) {
        this.userId = userId;
        this.taskId = taskId;
        this.interval = interval;
    }

    /**
     * A getter for the user of this assignment
     *
     * @return the user identifier of this assingment
     */
    public int getUserId() {
        return userId;
    }

    /**
     * A getter for the date interval of this assignment
     *
     * @return the date interval of this assignment
     */
    public Interval getInterval() {
        return interval;
    }

    /**
     * A getter for the task of this assignment
     *
     * @return the task identifier of this assignment
     */
    public int getTaskId() {
        return taskId;
    }

    /**
     * Sets the user identifier.
     *
     * @param userId User identifier to set
     * @return the builder
     */
    public PersistentAssignment userId(int userId) {
        return new PersistentAssignment(userId, this.taskId, this.interval);
    }

    /**
     * Sets the date interval
     *
     * @param interval Date interval to set
     * @return the builder
     */
    public PersistentAssignment interval(Interval interval) {
        return new PersistentAssignment(this.userId, this.taskId, interval);
    }

    /**
     * Sets the task id
     *
     * @param taskId task to set
     * @return the builder
     */
    public PersistentAssignment taskId(int taskId) {
        return new PersistentAssignment(this.userId, taskId, this.interval);
    }

    /**
     * Provides a Builder for {@link PersistentAssignment}.
     *
     * @see PersistentAssignment
     */
    public static class PersistentAssignmentBuilder {

        private int userId;
        private Interval interval;
        private int taskId;

        private PersistentAssignmentBuilder() {
        }

        /**
         * Creates a new PersistentAssignmentBuilder.
         *
         * @return A new PersistentAssignmentBuilder
         */
        public static PersistentAssignmentBuilder aPersistentAssignment() {
            return new PersistentAssignmentBuilder();
        }

        /**
         * Sets the user identifier.
         *
         * @param userId User identifier to set
         * @return the builder
         */
        public PersistentAssignmentBuilder userId(int userId) {
            this.userId = userId;
            return this;
        }

        /**
         * Sets the task id
         *
         * @param taskId task to set
         * @return the builder
         */
        public PersistentAssignmentBuilder taskId(int taskId) {
            this.taskId = taskId;
            return this;
        }

        /**
         * Sets the date interval
         *
         * @param interval Date interval to set
         * @return the builder
         */
        public PersistentAssignmentBuilder interval(Interval interval) {
            this.interval = interval;
            return this;
        }

        /**
         * Copies the builder for slightly differing instances.
         *
         * @return a new PersistentAssignmentBuilder with the same values as the
         * current one
         */
        public PersistentAssignmentBuilder but() {
            return aPersistentAssignment().userId(userId).taskId(taskId).interval(interval);
        }

        /**
         * Creates a PersistentAssignment with the current values.
         *
         * @return PersistentAssignment with the current values
         */
        public PersistentAssignment build() {
            return new PersistentAssignment(userId,taskId,interval);
        }
    }
}
