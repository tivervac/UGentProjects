package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import be.ugent.vopro1.persistence.jdbc.Column;
import be.ugent.vopro1.persistence.jdbc.Table;

import java.util.Arrays;

/**
 * The {@link AbstractTable} for the 'project' table.
 *
 * @see AbstractTable
 */
public class ProjectTable extends AbstractTable {

    /**
     * Construct an ProjectTable using
     * {@link AbstractTable#AbstractTable(java.lang.String, java.lang.Class, be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[])}
     *
     * @see AbstractTable#AbstractTable(java.lang.String, java.lang.Class,
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[])
     */
    public ProjectTable() {
        super("project",
                ProjectColumn.class,
                ProjectColumn.values(),
                Arrays.stream(ProjectColumn.values()).filter(ProjectColumn::isSelect).toArray(ProjectColumn[]::new),
                Arrays.stream(ProjectColumn.values()).filter(ProjectColumn::isInsert).toArray(ProjectColumn[]::new)
        );
    }

    /**
     * The representation of a ProjectColumn.
     * <p>
     * These are the different columns:
     * <ul>
     * <li>id</li>
     * <li>name</li>
     * </ul>
     *
     * @see Column
     */
    public enum ProjectColumn implements Column<ProjectAnalystTable> {

        ID("id", true, false),
        NAME("name"),
        LEADER_ID("leader_id");

        private String representation;
        private boolean select;
        private boolean insert;
        private static final Table TABLE = new ProjectTable();

        /**
         * Constructs an ProjectColumn.
         * <p>
         * Shorter constructor for when the column is a select, as well as an
         * insert column.
         *
         * @param representation the backend representation (name) of the column
         */
        ProjectColumn(String representation) {
            this(representation, true, true);
        }

        /**
         * Constructs an ProjectColumn.
         *
         * @param representation the backend representation (name) of the column
         * @param select Is this a select column or not
         * @param insert Is this an insert column or not
         */
        ProjectColumn(String representation, boolean select, boolean insert) {
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
