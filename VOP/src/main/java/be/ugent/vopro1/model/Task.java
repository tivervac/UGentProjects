package be.ugent.vopro1.model;

/**
 * A representation of a Task to be performed by a User
 */
public class Task {

    // Workload in seconds
    private long workload;
    private int priority;
    private UsecaseEntity usecase;

    /**
     * Creates a new Task with no parameters.
     * Required for Jackson deserialization
     */
    public Task() {
    }

    /**
     * A getter for the usecase of this Task
     *
     * @return The usecase of this Task
     */
    public UsecaseEntity getUsecase() {
        return usecase;
    }

    /**
     * A setter for the usecase of this Task with some sanity checking
     *
     * @param usecase The usecase
     */
    public void setUsecase(UsecaseEntity usecase) {
        if (usecase == null) {
            throw new IllegalArgumentException("Usecase can't be null.");
        }

        this.usecase = usecase;
    }

    /**
     * A getter for the priority of the Task
     *
     * @return The priority of the Task
     */
    public int getPriority() {
        return priority;
    }

    /**
     * A setter for the priority of this Task with some sanity checking
     *
     * @param priority The priority of the Task
     */
    public void setPriority(int priority) {
        if (priority < 0) {
            throw new IllegalArgumentException("Priority should be a positive integer.");
        }

        this.priority = priority;
    }

    /**
     * A getter for the workload of the Task
     *
     * @return The workload of the Task
     */
    public long getWorkload() {
        return workload;
    }

    /**
     * A setter for the workload of this Task with some sanity checking
     *
     * @param workload The workload of this Task
     */
    public void setWorkload(long workload) {
        if (workload < 0) {
            throw new IllegalArgumentException("Workload should be a positive integer.");
        }

        this.workload = workload;
    }

    /**
     * Formats all data of this Task into a readable format.
     *
     * @return the string representation of this Task
     */
    @Override
    public String toString() {
        return "Task{"
                + "workload ='" + workload + "'"
                + ", priority='" + priority + "', "
                + usecase.toString()
                + '}';
    }

    /**
     * Checks if the tasks are equal
     *
     * @param o The other Task
     * @return If the tasks are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (workload != task.workload) return false;
        if (priority != task.priority) return false;
        return (usecase != null ? usecase.name().equals(task.usecase.name()) : task.usecase == null);
    }

    /**
     * Generates a unique code for this Task
     *
     * @return The unique code
     */
    @Override
    public int hashCode() {
        int result = (int) (workload ^ (workload >>> 32));
        result = 31 * result + priority;
        result = 31 * result + (usecase != null ? usecase.hashCode() : 0);
        return result;
    }

    /**
     * Provides a Builder for {@link Task}.
     */
    public static class TaskBuilder {

        private UsecaseEntity usecase;
        private long workload;
        private int priority;

        /**
         * Creates a new TaskBuilder
         *
         * @return A new TaskBuilder
         */
        public static TaskBuilder aTask() {
            return new TaskBuilder();
        }

        /**
         * Sets the name
         *
         * @param usecase Usecase to set
         * @return the builder
         */
        public TaskBuilder usecase(UsecaseEntity usecase) {
            this.usecase = usecase;
            return this;
        }

        /**
         * Sets the priority
         *
         * @param priority Priority to set
         * @return the builder
         */
        public TaskBuilder priority(int priority) {
            this.priority = priority;
            return this;
        }

        /**
         * Sets the workload
         *
         * @param workload Workload to set
         * @return the builder
         */
        public TaskBuilder workload(long workload) {
            this.workload = workload;
            return this;
        }

        /**
         * Copies the builder for slightly differing instances
         *
         * @return a new TaskBuilder with the same values as the current
         * one
         */
        public TaskBuilder but() {
            return aTask().priority(priority).usecase(usecase).workload(workload);
        }

        /**
         * Creates a Task with the current values
         *
         * @return Task with the current values
         * @see Task
         */
        public Task build() {
            Task task = new Task();
            task.setPriority(priority);
            task.setUsecase(usecase);
            task.setWorkload(workload);

            return task;
        }
    }
}
