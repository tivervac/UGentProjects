package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import be.ugent.vopro1.persistence.jdbc.Column;
import be.ugent.vopro1.persistence.jdbc.Table;

import java.util.Arrays;

/**
 * The {@link AbstractTable} for the 'document' table.
 *
 * @see AbstractTable
 */
public class EntityTable extends AbstractTable {

    /**
     * Construct an EntityTable using
     * {@link AbstractTable#AbstractTable(java.lang.String, java.lang.Class, be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[])}
     *
     * @see AbstractTable#AbstractTable(java.lang.String, java.lang.Class,
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[])
     */
    public EntityTable() {
        super("document",
                EntityColumn.class,
                EntityColumn.values(),
                Arrays.stream(EntityColumn.values()).filter(EntityColumn::isSelect).toArray(EntityColumn[]::new),
                Arrays.stream(EntityColumn.values()).filter(EntityColumn::isInsert).toArray(EntityColumn[]::new)
        );
    }

    /**
     * The representation of an EntityColumn.
     * <p>
     * These are the different columns:
     * <ul>
     * <li>id</li>
     * <li>project_id</li>
     * <li>name</li>
     * <li>type</li>
     * <li>blob</li>
     * </ul>
     *
     * @see Column
     */
    public enum EntityColumn implements Column<EntityTable> {

        ID("id", true, false),
        PROJECT("project_id"),
        NAME("name"),
        TYPE("type"),
        BLOB("blob");

        private String representation;
        private boolean select;
        private boolean insert;
        private static final Table TABLE = new EntityTable();

        /**
         * Constructs an EntityColumn.
         * <p>
         * Shorter constructor for when the column is a select, as well as an
         * insert column.
         *
         * @param representation the backend representation (name) of the column
         */
        EntityColumn(String representation) {
            this(representation, true, true);
        }

        /**
         * Constructs an EntityColumn.
         *
         * @param representation the backend representation (name) of the column
         * @param select Is this a select column or not
         * @param insert Is this an insert column or not
         */
        EntityColumn(String representation, boolean select, boolean insert) {
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
