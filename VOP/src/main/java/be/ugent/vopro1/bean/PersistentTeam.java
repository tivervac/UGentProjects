package be.ugent.vopro1.bean;

/**
 * Provides a representation of a persistent Team.
 *
 * @see be.ugent.vopro1.persistence.jdbc.postgresql.TeamDAOImpl
 */
public class PersistentTeam {

    private int id;
    private String name;
    private int leaderId;

    /**
     * Retrieves the unique identifier of the team.
     *
     * @return Identifier of the team
     */
    public int getId() {
        return id;
    }

    /**
     * Sets a new unique identifier for the team. Only use this in DAO classes,
     * nowhere else!
     *
     * @param id Identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Retrieves the name of the team.
     *
     * @return Name of the team
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the team. Call
     * {@link be.ugent.vopro1.persistence.TeamDAO#update(PersistentTeam)} to
     * write this change to the database.
     *
     * @param name new name of the team to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retrieves the leader of the team.
     *
     * @return Leader of the team
     */
    public int getLeaderId() {
        return leaderId;
    }

    /**
     * Sets the leader of the team. Call
     * {@link be.ugent.vopro1.persistence.TeamDAO#update(PersistentTeam)} to
     * write this change to the database.
     *
     * @param leaderId new leader of the team to set
     */
    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    @Override
    public String toString() {
        return "PersistentTeam{"
                + "id=" + id
                + ", name='" + name + '\''
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

        PersistentTeam that = (PersistentTeam) o;

        if (getId() != that.getId()) {
            return false;
        }
        return !(getName() != null ? !getName().equals(that.getName()) : that.getName() != null);

    }

    @Override
    public int hashCode() {
        final int modifier = 31;
        final int zero = 0;
        int result = getId();
        result = modifier * result + (getName() != null ? getName().hashCode() : zero);
        return result;
    }

    /**
     * Provides a Builder for {@link PersistentTeam}.
     *
     * @see PersistentTeam
     */
    public static class PersistentTeamBuilder {

        private int id;
        private String name;
        private int leaderId;

        private PersistentTeamBuilder() {
        }

        /**
         * Creates a new PersistentTeamBuilder.
         *
         * @return A new PersistentTeamBuilder
         */
        public static PersistentTeamBuilder aPersistentTeam() {
            return new PersistentTeamBuilder();
        }

        /**
         * Sets the identifier.
         *
         * @param id Identifier to set
         * @return the builder
         */
        public PersistentTeamBuilder id(int id) {
            this.id = id;
            return this;
        }

        /**
         * Sets the name.
         *
         * @param name Name to set
         * @return the builder
         */
        public PersistentTeamBuilder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the leader.
         *
         * @param leaderId leader to set
         * @return the builder
         */
        public PersistentTeamBuilder leader(int leaderId) {
            this.leaderId = leaderId;
            return this;
        }

        /**
         * Copies the builder for slightly differing instances.
         *
         * @return a new PersistentTeamBuilder with the same values as the
         * current one
         */
        public PersistentTeamBuilder but() {
            return aPersistentTeam().id(id).name(name).leader(leaderId);
        }

        /**
         * Creates a PersistentTeam with the current values.
         *
         * @return PersistentTeam with the current values
         */
        public PersistentTeam build() {
            PersistentTeam persistentTeam = new PersistentTeam();
            persistentTeam.setId(id);
            persistentTeam.setName(name);
            persistentTeam.setLeaderId(leaderId);
            return persistentTeam;
        }
    }
}
