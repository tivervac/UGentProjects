package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import be.ugent.vopro1.persistence.jdbc.Column;
import be.ugent.vopro1.persistence.jdbc.Table;

import java.util.Arrays;

/**
 * The {@link AbstractTable} for the 'team_member' table.
 *
 * @see AbstractTable
 */
public class TeamMemberTable extends AbstractTable {

    /**
     * Construct an TeamMemberTable using
     * {@link AbstractTable#AbstractTable(java.lang.String, java.lang.Class, be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[])}
     *
     * @see AbstractTable#AbstractTable(java.lang.String, java.lang.Class,
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[])
     */
    public TeamMemberTable() {
        super("team_member",
                TeamMemberColumn.class,
                TeamMemberColumn.values(),
                Arrays.stream(TeamMemberColumn.values()).filter(TeamMemberColumn::isSelect).toArray(TeamMemberColumn[]::new),
                Arrays.stream(TeamMemberColumn.values()).filter(TeamMemberColumn::isInsert).toArray(TeamMemberColumn[]::new)
        );
    }

    /**
     * The representation of a TeamMemberColumn.
     * <p>
     * These are the different columns:
     * <ul>
     * <li>team_id</li>
     * <li>person_id</li>
     * </ul>
     *
     * @see Column
     */
    public enum TeamMemberColumn implements Column<TeamMemberTable> {

        TEAM_ID("team_id"),
        USER_ID("person_id");

        private String representation;
        private boolean select;
        private boolean insert;
        private static final Table TABLE = new TeamMemberTable();

        /**
         * Constructs an TeamMemberColumn.
         * <p>
         * Shorter constructor for when the column is a select, as well as an
         * insert column.
         *
         * @param representation the backend representation (name) of the column
         */
        TeamMemberColumn(String representation) {
            this(representation, true, true);
        }

        /**
         * Constructs an TeamMemberColumn.
         *
         * @param representation the backend representation (name) of the column
         * @param select Is this a select column or not
         * @param insert Is this an insert column or not
         */
        TeamMemberColumn(String representation, boolean select, boolean insert) {
            this.representation = representation;
            this.select = select;
            this.insert = insert;
        }

        /**
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         */
        @Override
        public String repr() {
            return representation;
        }

        /**
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         */
        @Override
        public String fullRepr() {
            return TABLE.repr() + "." + repr();
        }

        /**
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         */
        @Override
        public boolean isSelect() {
            return select;
        }

        /**
         * {@inheritDoc}
         *
         * @return {@inheritDoc}
         */
        @Override
        public boolean isInsert() {
            return insert;
        }
    }
}
