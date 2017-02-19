package be.ugent.vopro1.model;

/**
 * A representation of a Project as returned by the database.
 */
public class EntityProject {

    private String name;
    private User leader;

    /**
     * Creates a new project with no parameters.
     * Required for Jackson deserialization
     */
    public EntityProject() {
    }

    /**
     * Create a new Project with the given (unique) name
     *
     * @param name the name of this Project
     */
    public EntityProject(String name) {
        this(name, null);
    }

    /**
     * Create a new Project with the given (unique) name and leader
     *
     * @param name the name of this Project
     * @param leader leader of this Project
     */
    public EntityProject(String name, User leader) {
        if (name == null){
            throw new IllegalArgumentException();
        }

        this.name = name;
        this.leader = leader;
    }

    /**
     * A getter for the name of this Project.
     *
     * @return the name of this project
     */
    public String getName() {
        return name;
    }

    /**
     * A setter for the name of this Project.
     *
     * @param name the name of this Project.
     */
    public void setName(String name) {
        if (name == null){
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    /**
     * A getter for the leader of this Project.
     *
     * @return the name of this project
     */
    public User getLeader() {
        return leader;
    }

    /**
     * A setter for the leader of this Project.
     *
     * @param leader the new leader of this Project.
     */
    public void setLeader(User leader) {
        this.leader = leader;
    }

    /**
     * Formats all data of this EntityProject into a readable format.
     *
     * @return the string representation of this EntityProject
     */
    @Override
    public String toString() {
        return "Project{\"name\":\"" + getName() + "\"}";
    }

    /**
     * Provides a Builder for {@link EntityProject}.
     */
    public static class EntityProjectBuilder {

        private String name;
        private User leader;

        private EntityProjectBuilder() {
            name = "";
        }

        /**
         * Creates a new EntityProjectBuilder
         *
         * @return A new EntityProjectBuilder
         */
        public static EntityProjectBuilder anEntityProject() {
            return new EntityProjectBuilder();
        }

        /**
         * Sets the name
         *
         * @param name Name to set
         * @return the builder
         */
        public EntityProjectBuilder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the leader
         *
         * @param leader Leader to set
         * @return the builder
         */
        public EntityProjectBuilder leader(User leader) {
            this.leader = leader;
            return this;
        }

        /**
         * Copies the builder for slightly differing instances
         *
         * @return a new EntityProjectBuilder with the same values as the
         * current one
         */
        public EntityProjectBuilder but() {
            return anEntityProject().name(name).leader(leader);
        }

        /**
         * Creates a EntityProject with the current values
         *
         * @return EntityProject with the current values
         * @see EntityProject
         */
        public EntityProject build() {
            return new EntityProject(name, leader);
        }
    }
}
