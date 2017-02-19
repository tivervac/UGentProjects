package be.ugent.vopro1.persistence.jdbc;

/**
 * Provides a representation of a database table
 */
public interface Table {

    /**
     * Returns the backend representation (name) of the table
     * <p>
     * For example: ProjectTable -&gt; 'project'
     *
     * @return Backend representation of the table
     */
    String repr();

    /**
     * Returns a full list of columns of this table
     *
     * @return List of columns
     */
    Column[] columns();

    /**
     * Returns a list of columns that should be used in "insert" queries.
     * <p>
     * For example: "id" is not an insert column, because you don't know this in
     * advance
     *
     * @return List of insert columns
     */
    Column[] inserts();

    /**
     * Returns a list of columns that should be used in "select" queries.
     * <p>
     * Generally all columns are also select columns
     *
     * @return List of select columns
     */
    Column[] selects();

    /**
     * Checks if a column is part of this table
     *
     * @param column Column to check
     */
    void check(Column column);

    /**
     * Checks if a list of columns are all part of this table
     *
     * @param columns List of columns to check
     */
    void check(Column... columns);
}
