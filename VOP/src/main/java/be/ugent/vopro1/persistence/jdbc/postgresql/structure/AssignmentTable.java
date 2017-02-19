package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import be.ugent.vopro1.persistence.jdbc.Column;
import be.ugent.vopro1.persistence.jdbc.Table;

import java.util.Arrays;

/**
 * The {@link AbstractTable} for the 'assignment' table.
 *
 * @see AbstractTable
 */
public class AssignmentTable extends AbstractTable {

    /**
     * Construct a AssignmentTable using
     * {@link AbstractTable#AbstractTable(java.lang.String, java.lang.Class, be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[])}
     *
     * @see AbstractTable#AbstractTable(java.lang.String, java.lang.Class,
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[])
     */
    public AssignmentTable() {
        super("assignment",
                AssignmentColumn.class,
                AssignmentColumn.values(),
                Arrays.stream(AssignmentColumn.values()).filter(AssignmentColumn::isSelect).toArray(AssignmentColumn[]::new),
                Arrays.stream(AssignmentColumn.values()).filter(AssignmentColumn::isInsert).toArray(AssignmentColumn[]::new)
        );
    }

    /**
     * The representation of a AssignmentColumn.
     * <p>
     * These are the different columns:
     * <ul>
     * <li>id</li>
     * <li>name</li>
     * </ul>
     *
     * @see Column
     */
    public enum AssignmentColumn implements Column<AssignmentTable> {

        TASK_ID("task_id", true, true),
        USER_ID("person_id"),
        PROJECT_ID("project_id"),
        START_DATE("start_date"),
        END_DATE("end_date");

        private String representation;
        private boolean select;
        private boolean insert;
        private static final Table TABLE = new AssignmentTable();

        /**
         * Constructs a AssignmentColumn.
         * <p>
         * Shorter constructor for when the column is a select, as well as an
         * insert column.
         *
         * @param representation the backend representation (name) of the column
         */
        AssignmentColumn(String representation) {
            this(representation, true, true);
        }

        /**
         * Constructs a AssignmentColumn.
         *
         * @param representation the backend representation (name) of the column
         * @param select Is this a select column or not
         * @param insert Is this an insert column or not
         */
        AssignmentColumn(String representation, boolean select, boolean insert) {
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
