package be.ugent.vopro1.persistence.jdbc;

import be.ugent.vopro1.persistence.jdbc.postgresql.QueryBuilderImpl;

/**
 * The JdbcQueryBuilder interface defines all the different kinds of queries
 * that we'll be able to use.
 *
 * @see QueryBuilderImpl
 */
public interface JdbcQueryBuilder {

    /**
     * Builds a query to insert a row into table.
     *
     * @param table the table in which we would like to execute the query
     * @return the query
     * @see Table
     */
    String insertQuery(Table table);

    /**
     * Builds a query to select rows from table where the whereColumns contain a
     * certain value.
     *
     * @param table the table in which we would like to execute the query
     * @param whereColumns the specified columns that will have to contain a
     * certain value
     * @return the query
     * @see Table
     * @see Column
     */
    String selectQuery(Table table, Column[] whereColumns);

    /**
     * Builds a query to check if at least one record in the table exists
     * with given whereColumns
     *
     * @param table the table in which we would like to execute the query
     * @param whereColumns the specified columns that will have to contain
     * a certain value
     * @return the query
     * @see Table
     * @see Column
     */
    String existsQuery(Table table, Column[] whereColumns);

    /**
     * Builds a query to select rows from table where findColumn can contain a
     * regex and whereColumns contain a certain value.
     *
     * @param table the table in which we would like to execute the query
     * @param findColumn the specified column where we will search with a regex
     * @param whereColumns the specified columns that will have to contain a
     * certain value
     * @return the query
     * @see Table
     * @see Column
     */
    String regexQuery(Table table, Column findColumn, Column[] whereColumns);

    /**
     * Builds a query to delete a row from table.
     *
     * @param table the table in which we would like to execute the query
     * @param whereColumns the specified columns that will have to contain a
     * certain value
     * @return the query
     * @see Table
     * @see Column
     */
    String deleteQuery(Table table, Column[] whereColumns);

    /**
     * Builds a query to update a row from table.
     *
     * @param table the table in which we would like to execute the query
     * @param updateColumns the columns you wish to update
     * @param whereColumns the specified columns that will have to contain a
     * certain value
     * @return the query
     * @see Table
     * @see Column
     */
    String updateQuery(Table table, Column[] updateColumns, Column[] whereColumns);

    /**
     * Builds a query to select rows from result-table with certain columns
     * matching the columns from the gather-table.
     *
     * @param gather the table which we gather data from
     * @param result the table we wish to select rows from, using the
     * gather-tables data
     * @param gatherMatcher the columns from the gather-table that have to match
     * those from the result-table
     * @param resultMatcher the columns from the result-table that have to match
     * those from the gather-table
     * @param whereColumns the specified columns that will have to contain a
     * certain value
     * @return the query
     * @see Table
     * @see Column
     */
    String crossoverQuery(Table gather, Table result, Column gatherMatcher, Column resultMatcher, Column[] whereColumns);
}
