package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import be.ugent.vopro1.persistence.jdbc.Column;
import be.ugent.vopro1.persistence.jdbc.Table;

import java.util.Arrays;

/**
 * The {@link AbstractTable} for the 'project_analyst' table.
 *
 * @see AbstractTable
 */
public class ProjectAnalystTable extends AbstractTable {

    /**
     * Construct an ProjectAnalystTable using
     * {@link AbstractTable#AbstractTable(java.lang.String, java.lang.Class, be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[])}
     *
     * @see AbstractTable#AbstractTable(java.lang.String, java.lang.Class,
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[])
     */
    public ProjectAnalystTable() {
        super("project_analyst",
                ProjectAnalystColumn.class,
                ProjectAnalystColumn.values(),
                Arrays.stream(ProjectAnalystColumn.values()).filter(ProjectAnalystColumn::isSelect).toArray(ProjectAnalystColumn[]::new),
                Arrays.stream(ProjectAnalystColumn.values()).filter(ProjectAnalystColumn::isInsert).toArray(ProjectAnalystColumn[]::new)
        );
    }

    /**
     * The representation of a ProjectAnalystColumn.
     * <p>
     * These are the different columns:
     * <ul>
     * <li>project_id</li>
     * <li>person_id</li>
     * </ul>
     *
     * @see Column
     */
    public enum ProjectAnalystColumn implements Column<ProjectAnalystTable> {

        PROJECT_ID("project_id"),
        USER_ID("person_id"),
        WORKHOURS("workhours");

        private String representation;
        private boolean select;
        private boolean insert;
        private static final Table TABLE = new ProjectAnalystTable();

        /**
         * Constructs an ProjectAnalystColumn.
         * <p>
         * Shorter constructor for when the column is a select, as well as an
         * insert column.
         *
         * @param representation the backend representation (name) of the column
         */
        ProjectAnalystColumn(String representation) {
            this(representation, true, true);
        }

        /**
         * Constructs an ProjectAnalystColumn.
         *
         * @param representation the backend representation (name) of the column
         * @param select Is this a select column or not
         * @param insert Is this an insert column or not
         */
        ProjectAnalystColumn(String representation, boolean select, boolean insert) {
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
