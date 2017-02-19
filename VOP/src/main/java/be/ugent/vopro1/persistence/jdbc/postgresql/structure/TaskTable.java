package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import be.ugent.vopro1.persistence.jdbc.Column;
import be.ugent.vopro1.persistence.jdbc.Table;

import java.util.Arrays;

/**
 * The {@link AbstractTable} for the 'task' table.
 *
 * @see AbstractTable
 */
public class TaskTable extends AbstractTable {

    /**
     * Construct a TaskTable using
     * {@link AbstractTable#AbstractTable(java.lang.String, java.lang.Class, be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[])}
     *
     * @see AbstractTable#AbstractTable(java.lang.String, java.lang.Class,
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[])
     */
    public TaskTable() {
        super("task",
                TaskColumn.class,
                TaskColumn.values(),
                Arrays.stream(TaskColumn.values()).filter(TaskColumn::isSelect).toArray(TaskColumn[]::new),
                Arrays.stream(TaskColumn.values()).filter(TaskColumn::isInsert).toArray(TaskColumn[]::new)
        );
    }

    /**
     * The representation of a TaskColumn.
     * <p>
     * These are the different columns:
     * <ul>
     * <li>id</li>
     * <li>name</li>
     * </ul>
     *
     * @see Column
     */
    public enum TaskColumn implements Column<TaskTable> {

        USE_CASE_ID("document_id", true, true),
        //USER_ID("person_id"),
        WORKLOAD("workload"),
        PRIORITY("priority");

        private String representation;
        private boolean select;
        private boolean insert;
        private static final Table TABLE = new TaskTable();

        /**
         * Constructs a TaskColumn.
         * <p>
         * Shorter constructor for when the column is a select, as well as an
         * insert column.
         *
         * @param representation the backend representation (name) of the column
         */
        TaskColumn(String representation) {
            this(representation, true, true);
        }

        /**
         * Constructs a TaskColumn.
         *
         * @param representation the backend representation (name) of the column
         * @param select Is this a select column or not
         * @param insert Is this an insert column or not
         */
        TaskColumn(String representation, boolean select, boolean insert) {
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
