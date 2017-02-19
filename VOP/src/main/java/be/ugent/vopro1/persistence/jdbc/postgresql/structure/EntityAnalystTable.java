package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import be.ugent.vopro1.persistence.jdbc.Column;
import be.ugent.vopro1.persistence.jdbc.Table;

import java.util.Arrays;

/**
 * The {@link AbstractTable} for the 'entity_analyst' table.
 *
 * @see AbstractTable
 */
public class EntityAnalystTable extends AbstractTable {

    /**
     * Construct an EntityAnalystTable using
     * {@link AbstractTable#AbstractTable(String, Class, Column[], Column[], Column[])}
     *
     * @see AbstractTable#AbstractTable(String, Class,
     * Column[],
     * Column[],
     * Column[])
     */
    public EntityAnalystTable() {
        super("entity_analyst",
                EntityAnalystColumn.class,
                EntityAnalystColumn.values(),
                Arrays.stream(EntityAnalystColumn.values()).filter(EntityAnalystColumn::isSelect).toArray(EntityAnalystColumn[]::new),
                Arrays.stream(EntityAnalystColumn.values()).filter(EntityAnalystColumn::isInsert).toArray(EntityAnalystColumn[]::new)
        );
    }

    /**
     * The representation of a EntityAnalystColumn.
     * <p>
     * These are the different columns:
     * <ul>
     * <li>entity_id</li>
     * <li>person_id</li>
     * </ul>
     *
     * @see Column
     */
    public enum EntityAnalystColumn implements Column<EntityAnalystTable> {

        ENTITY_ID("entity_id"),
        USER_ID("person_id");

        private String representation;
        private boolean select;
        private boolean insert;
        private static final Table TABLE = new EntityAnalystTable();

        /**
         * Constructs an EntityAnalystColumn.
         * <p>
         * Shorter constructor for when the column is a select, as well as an
         * insert column.
         *
         * @param representation the backend representation (name) of the column
         */
        EntityAnalystColumn(String representation) {
            this(representation, true, true);
        }

        /**
         * Constructs an EntityAnalystColumn.
         *
         * @param representation the backend representation (name) of the column
         * @param select Is this a select column or not
         * @param insert Is this an insert column or not
         */
        EntityAnalystColumn(String representation, boolean select, boolean insert) {
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
