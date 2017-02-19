package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import be.ugent.vopro1.persistence.jdbc.Column;
import be.ugent.vopro1.persistence.jdbc.Table;

import java.util.Arrays;

/**
 * The {@link AbstractTable} for the 'person' table.
 *
 * @see AbstractTable
 */
public class UserTable extends AbstractTable {

    /**
     * Construct a UserTable using
     * {@link AbstractTable#AbstractTable(java.lang.String, java.lang.Class, be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[], be.ugent.vopro1.persistence.jdbc.Column[])}
     *
     * @see AbstractTable#AbstractTable(java.lang.String, java.lang.Class,
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[],
     * be.ugent.vopro1.persistence.jdbc.Column[])
     */
    public UserTable() {
        super("person",
                UserColumn.class,
                UserColumn.values(),
                Arrays.stream(UserColumn.values()).filter(UserColumn::isSelect).toArray(UserColumn[]::new),
                Arrays.stream(UserColumn.values()).filter(UserColumn::isInsert).toArray(UserColumn[]::new)
        );
    }

    /**
     * The representation of a UserColumn.
     * <p>
     * These are the different columns:
     * <ul>
     * <li>id</li>
     * <li>first_name</li>
     * <li>last_name</li>
     * <li>email</li>
     * <li>password</li>
     * <li>is_admin</li>
     * <li>is_analyst</li>
     * </ul>
     *
     * @see Column
     */
    public enum UserColumn implements Column<UserTable> {

        ID("id", true, false),
        FIRST_NAME("first_name"),
        LAST_NAME("last_name"),
        EMAIL("email"),
        PASSWORD("password"),
        IS_ADMIN("is_admin");

        private String representation;
        private boolean select;
        private boolean insert;
        private static final Table TABLE = new UserTable();

        /**
         * Constructs an UserColumn.
         * <p>
         * Shorter constructor for when the column is a select, as well as an
         * insert column.
         *
         * @param representation the backend representation (name) of the column
         */
        UserColumn(String representation) {
            this(representation, true, true);
        }

        /**
         * Constructs an UserColumn.
         *
         * @param representation the backend representation (name) of the column
         * @param select Is this a select column or not
         * @param insert Is this an insert column or not
         */
        UserColumn(String representation, boolean select, boolean insert) {
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
