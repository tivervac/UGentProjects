package be.ugent.vopro1.bean;

/**
 * Provides a representation of a persistent Task.
 *
 * @see be.ugent.vopro1.persistence.jdbc.postgresql.ScheduleDAOImpl
 */
public class PersistentTask {

    private long workload;
    private int priority;
    private int useCaseId;

    /**
     * Creates a new PersistentTask with given identifier. This should only
     * be used in DAO classes, nowhere else!
     *
     * @param workload  The workload of this task
     * @param priority  The priority of this task
     * @param useCaseId The id of this task's usecase
     */
    public PersistentTask(int useCaseId, long workload, int priority) {
        this.useCaseId = useCaseId;
        this.workload = workload;
        this.priority = priority;
    }

    /**
     * A getter for the workload of this task
     *
     * @return the workload of this task in seconds
     */
    public long getWorkload() {
        return workload;
    }

    /**
     * A getter for the priority of this task
     *
     * @return The priority of this task
     */
    public int getPriority() {
        return priority;
    }

    /**
     * A getter for this task's usecase
     *
     * @return The identifier of this task's usecase
     */
    public int getUseCaseId() {
        return useCaseId;
    }

    /**
     * Sets the workload
     *
     * @param workload workload, in seconds, to set
     * @return the builder
     */
    public PersistentTask workload(long workload) {
        return new PersistentTask(this.useCaseId, workload, this.priority);
    }

    /**
     * Sets the priority
     *
     * @param priority priority to set
     * @return the builder
     */
    public PersistentTask priority(int priority) {
        return new PersistentTask(this.useCaseId, this.workload, priority);
    }

    /**
     * Sets the usecase identifier.
     *
     * @param useCaseId UseCase identifier to set
     * @return the builder
     */
    public PersistentTask useCaseId(int useCaseId) {
        return new PersistentTask(useCaseId, this.workload, this.priority);
    }

    /**
     * Provides a Builder for {@link PersistentTask}.
     *
     * @see PersistentTask
     */
    public static class PersistentTaskBuilder {

        private long workload;
        private int priority;
        private int useCaseId;

        private PersistentTaskBuilder() {
        }

        /**
         * Creates a new PersistentTaskBuilder.
         *
         * @return A new PersistentTaskBuilder
         */
        public static PersistentTaskBuilder aPersistentTask() {
            return new PersistentTaskBuilder();
        }

        /**
         * Sets the usecase identifier.
         *
         * @param useCaseId UseCase identifier to set
         * @return the builder
         */
        public PersistentTaskBuilder useCaseId(int useCaseId) {
            this.useCaseId = useCaseId;
            return this;
        }

        /**
         * Sets the workload
         *
         * @param workload workload to set in seconds
         * @return the builder
         */
        public PersistentTaskBuilder workload(long workload) {
            this.workload = workload;
            return this;
        }

        /**
         * Sets the priority
         *
         * @param priority priority to set
         * @return the builder
         */
        public PersistentTaskBuilder priority(int priority) {
            this.priority = priority;
            return this;
        }

        /**
         * Copies the builder for slightly differing instances.
         *
         * @return a new PersistentTaskBuilder with the same values as the
         * current one
         */
        public PersistentTaskBuilder but() {
            return aPersistentTask().useCaseId(useCaseId).workload(workload).priority(priority);
        }

        /**
         * Creates a PersistentTask with the current values.
         *
         * @return PersistentTask with the current values
         */
        public PersistentTask build() {
            return new PersistentTask(useCaseId, workload, priority);
        }
    }
}
