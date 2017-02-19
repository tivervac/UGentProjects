package be.ugent.vopro1.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A representation of a Team as returned by the database.
 */
public class Team {

    private String name;
    private User leader;
    private List<User> members;

    /**
     * A getter for the name of this Team.
     *
     * @return the name of this Team
     */
    public String getName() {
        return name;
    }

    /**
     * A setter for the name of this Team.
     *
     * @param name the name this Team should have
     */
    public void setName(String name) {
        if (name == null){
            throw new IllegalArgumentException();
        }

        this.name = name;
    }

    /**
     * A getter for the leader of this Team.
     *
     * @return the leader of this Team
     */
    public User getLeader() {
        return leader;
    }

    /**
     * A setter for the leader of this Team.
     *
     * @param leader the name this Team should have
     */
    public void setLeader(User leader) {
        this.leader = leader;
    }

    /**
     * A getter for the members of this Team.
     * <p>
     * This method will return a deep copy of the internal iterable.
     *
     * @return the members of this Team
     */
    public List<User> getMembers() {
        return members.stream().map(User::clone).collect(Collectors.toList());
    }

    /**
     * A setter for the members of this Team.
     * <p>
     * This method will use a deep copy of the members argument internally.
     *
     * @param members the name this Team should have
     */
    public void setMembers(List<User> members) {
        this.members = members.stream().map(User::clone).collect(Collectors.toList());
    }

    /**
     *
     * Formats all data of this Team into a readable format.
     *
     * @return the string representation of this Team
     */
    @Override
    public String toString() {
        return "Team{"
                + ", name='" + name + "'"
                + '}';
    }

    /**
     * Provides a Builder for {@link Team}.
     */
    public static class TeamBuilder {

        private String name;
        private User leader;
        private List<User> members = new ArrayList<>();

        private TeamBuilder() {
        }

        /**
         * Creates a new TeamBuilder
         *
         * @return A new TeamBuilder
         */
        public static TeamBuilder aTeam() {
            return new TeamBuilder();
        }

        /**
         * Sets the name
         *
         * @param name Name to set
         * @return the builder
         */
        public TeamBuilder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the leader
         *
         * @param leader Leader to set
         * @return the builder
         */
        public TeamBuilder leader(User leader) {
            this.leader = leader;
            return this;
        }

        /**
         * Sets the team members
         *
         * @param members Team members to set
         * @return the builder
         */
        public TeamBuilder members(List<User> members) {
            this.members = members.stream().map(User::clone).collect(Collectors.toList());
            return this;
        }

        /**
         * Copies the builder for slightly differing instances
         *
         * @return a new TeamBuilder with the same values as the current
         * one
         */
        public TeamBuilder but() {
            return aTeam().name(name).leader(leader).members(members);
        }

        /**
         * Creates a Team with the current values
         *
         * @return Team with the current values
         * @see Team
         */
        public Team build() {
            Team team = new Team();
            team.setName(name);
            team.setLeader(leader);
            team.setMembers(members);
            return team;
        }
    }

}
