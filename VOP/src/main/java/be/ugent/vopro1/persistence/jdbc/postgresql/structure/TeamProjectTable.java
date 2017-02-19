package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import be.ugent.vopro1.persistence.jdbc.Column;
import be.ugent.vopro1.persistence.jdbc.Table;

import java.util.Arrays;

/**
 * The {@link AbstractTable} for the 'team_project' table.
 *
 * @see AbstractTable
 */
public class TeamProjectTable extends AbstractTable {

    /**
     * Construct an TeamProjectTable using
     * {@link AbstractTable#AbstractTable(java.lang.String, java.lang.Class, be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[])}
     *
     * @see AbstractTable#AbstractTable(java.lang.String, java.lang.Class,
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[])
     */
    public TeamProjectTable() {
        super("team_project",
                TeamProjectColumn.class,
                TeamProjectColumn.values(),
                Arrays.stream(TeamProjectColumn.values()).filter(TeamProjectColumn::isSelect).toArray(TeamProjectColumn[]::new),
                Arrays.stream(TeamProjectColumn.values()).filter(TeamProjectColumn::isInsert).toArray(TeamProjectColumn[]::new)
        );
    }

    /**
     * The representation of a TeamProjectColumn.
     * <p>
     * These are the different columns:
     * <ul>
     * <li>team_id</li>
     * <li>project_id</li>
     * </ul>
     *
     * @see Column
     */
    public enum TeamProjectColumn implements Column<TeamProjectTable> {

        TEAM_ID("team_id"),
        PROJECT_ID("project_id");

        private String representation;
        private boolean select;
        private boolean insert;
        private static final Table TABLE = new TeamProjectTable();

        /**
         * Constructs an TeamProjectColumn.
         * <p>
         * Shorter constructor for when the column is a select, as well as an
         * insert column.
         *
         * @param representation the backend representation (name) of the column
         */
        TeamProjectColumn(String representation) {
            this(representation, true, true);
        }

        /**
         * Constructs an TeamProjectColumn.
         *
         * @param representation the backend representation (name) of the column
         * @param select Is this a select column or not
         * @param insert Is this an insert column or not
         */
        TeamProjectColumn(String representation, boolean select, boolean insert) {
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
