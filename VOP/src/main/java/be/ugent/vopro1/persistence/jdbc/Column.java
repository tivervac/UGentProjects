package be.ugent.vopro1.persistence.jdbc;

/**
 * Provides a representation of a Column
 *
 * @param <T> Table that this column is associated with
 * @see Table
 */
public interface Column<T extends Table> {

    /**
     * Returns the backend representation (name) of the column
     * <p>
     * For example: Name of a project -&gt; 'name'
     *
     * @return Backend representation of the column
     */
    String repr();

    /**
     * Returns the full backend representation (table.name) of the column
     * <p>
     * For example: Name of a project -&gt; 'project.name'
     *
     * @return Full backend representation of the column
     */
    String fullRepr();

    /**
     * Is this a select column or not
     *
     * @return <code>true</code> if this is a select column <code>false</code>
     * otherwise
     */
    boolean isSelect();

    /**
     * Is this an insert column or not
     *
     * @return <code>true</code> if this is an insert column <code>false</code>
     * otherwise
     */
    boolean isInsert();

}
