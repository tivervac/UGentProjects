package be.ugent.vopro1.bean;

import java.util.Objects;

/**
 * Provides a representation of a persistent Project.
 *
 * @see be.ugent.vopro1.persistence.jdbc.postgresql.ProjectDAOImpl
 */
public class PersistentProject {

    private int id;
    private String name;
    private int leaderId;

    /**
     * Empty constructor for Jackson
     */
    public PersistentProject() {
    }

    /**
     * Creates a new PersistentProject with given identifier. This should only
     * be used in DAO classes, nowhere else!
     *
     * @param id Identifier of the project
     * @param name Name of the project
     * @param leaderId Identifier of the leader of the project
     * @see be.ugent.vopro1.persistence.jdbc.postgresql.ProjectDAOImpl
     */
    public PersistentProject(int id, String name, int leaderId) {
        this.id = id;
        this.name = name;
        this.leaderId = leaderId;
    }

    /**
     * Creates a new PersistentProject. Call
     * {@link be.ugent.vopro1.persistence.ProjectDAO#save} to create it in the
     * database. After saving, the correct identifier will be set automatically.
     *
     * @param name Name of the project
     * @param leaderId Identifier of the leader of the project
     */
    public PersistentProject(String name, int leaderId) {
        this.id = -1;
        this.name = name;
        this.leaderId = leaderId;
    }

    /**
     * Retrieve the identifier of the project.
     *
     * @return Identifier of the project
     */
    public int getId() {
        return id;
    }

    /**
     * Sets a new unique identifier for the project. Only use this in DAO
     * classes, nowhere else!
     *
     * @param id Identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the project.
     *
     * @return Name of the project
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the project. Call
     * {@link be.ugent.vopro1.persistence.ProjectDAO#update} to write this
     * change to the database.
     *
     * @param name New name of the project to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the leader identifier of the project.
     *
     * @return Identifier of the leader of the project
     */
    public int getLeaderId() {
        return leaderId;
    }

    /**
     * Sets the leader identifier of the project. Call
     * {@link be.ugent.vopro1.persistence.ProjectDAO#update} to write this
     * change to the database.
     *
     * @param leaderId New leader id of the project to set
     */
    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    /**
     * Formats all data of this PersistentProject into a readable format.
     *
     * @return the string representation of this PersistentProject
     */
    @Override
    public String toString() {
        return "PersistentProject{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", leaderId='" + leaderId + '\''
                + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PersistentProject that = (PersistentProject) o;

        if (getId() != that.getId()) {
            return false;
        }
        if (getLeaderId() != that.getLeaderId()) {
            return false;
        }
        return !(getName() != null ? !getName().equals(that.getName()) : that.getName() != null);

    }

    @Override
    public int hashCode() {
        final int modifier = 31;
        final int zero = 0;
        int result = getId();
        result = modifier * result + (Objects.hashCode(getLeaderId()));
        result = modifier * result + (getName() != null ? getName().hashCode() : zero);
        return result;
    }

    /**
     * Provides a Builder for {@link PersistentProject}.
     *
     * @see PersistentProject
     */
    public static class PersistentProjectBuilder {

        private int id;
        private String name;
        private int leaderId;

        private PersistentProjectBuilder() {
        }

        /**
         * Creates a new PersistentProjectBuilder.
         *
         * @return A new PersistentProjectBuilder
         */
        public static PersistentProjectBuilder aPersistentProject() {
            return new PersistentProjectBuilder();
        }

        /**
         * Sets the identifier.
         *
         * @param id Identifier to set
         * @return the builder
         */
        public PersistentProjectBuilder id(int id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the name.
         *
         * @param name Name to set
         * @return the builder
         */
        public PersistentProjectBuilder name(String name) {
            this.name = name;
            return this;
        }


        /**
         * Sets the leader identifier.
         *
         * @param leaderId Leader identifier to set
         * @return the builder
         */
        public PersistentProjectBuilder leader(int leaderId) {
            this.leaderId = leaderId;
            return this;
        }

        /**
         * Copies the builder for slightly differing instances.
         *
         * @return a new PersistentProjectBuilder with the same values as the
         * current one
         */
        public PersistentProjectBuilder but() {
            return aPersistentProject().id(id).name(name).leader(leaderId);
        }

        /**
         * Creates a PersistentProject with the current values.
         *
         * @return PersistentProject with the current values
         */
        public PersistentProject build() {
            PersistentProject persistentProject = new PersistentProject();
            persistentProject.setId(id);
            persistentProject.setName(name);
            persistentProject.setLeaderId(leaderId);
            return persistentProject;
        }
    }
}
