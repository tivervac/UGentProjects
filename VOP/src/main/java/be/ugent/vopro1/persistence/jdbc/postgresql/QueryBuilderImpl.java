package be.ugent.vopro1.persistence.jdbc.postgresql;

import be.ugent.vopro1.persistence.jdbc.Column;
import be.ugent.vopro1.persistence.jdbc.JdbcQueryBuilder;
import be.ugent.vopro1.persistence.jdbc.Table;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * An Implementation of the {@link JdbcQueryBuilder} interface.
 *
 * @see JdbcQueryBuilder
 */
public class QueryBuilderImpl implements JdbcQueryBuilder {

    private static final String SELECT = "select ";
    private static final String FROM = " from ";
    private static final String WHERE = " where ";
    private static final String DELIMITER = ", ";
    private static final String AND = " and ";

    /**
     * {@inheritDoc}
     *
     * @param table{@inheritDoc}
     * @return {@inheritDoc}
     * @see Table
     */
    @Override
    public String insertQuery(Table table) {
        StringBuilder builder = new StringBuilder()
                .append("insert into ")
                .append(table.repr())
                .append(" (")
                .append(String.join(DELIMITER, mapColumns(table.inserts(), Column::repr)))
                .append(") values (")
                .append(String.join(DELIMITER, mapColumns(table.inserts(), c -> "?")))
                .append(")");

        return builder.toString();
    }

    /**
     * {@inheritDoc}
     *
     * @param table {@inheritDoc}
     * @param findColumn {@inheritDoc}
     * @param whereColumns {@inheritDoc}
     * @return {@inheritDoc}
     * @see Table
     * @see Column
     */
    @Override
    public String regexQuery(Table table, Column findColumn, Column[] whereColumns) {
        table.check(findColumn);
        table.check(whereColumns);

        StringBuilder builder = new StringBuilder()
                .append(SELECT)
                .append(String.join(DELIMITER, mapColumns(table.selects(), Column::repr)))
                .append(FROM)
                .append(table.repr())
                .append(WHERE)
                .append(findColumn.repr())
                .append(" ~* ?")
                .append(buildColumns(AND, whereColumns));

        return builder.toString();
    }

    /**
     * {@inheritDoc}
     *
     * @param table {@inheritDoc}
     * @param whereColumns {@inheritDoc}
     * @return {@inheritDoc}
     * @see Table
     * @see Column
     */
    @Override
    public String selectQuery(Table table, Column[] whereColumns) {
        table.check(whereColumns);

        StringBuilder builder = new StringBuilder()
                .append(SELECT)
                .append(String.join(DELIMITER, mapColumns(table.selects(), Column::repr)))
                .append(FROM)
                .append(table.repr())
                .append(buildColumns(WHERE, whereColumns));

        return builder.toString();
    }

    /**
     * {@inheritDoc}
     *
     * @param table {@inheritDoc}
     * @param whereColumns {@inheritDoc}
     * @return {@inheritDoc}
     * @see Table
     * @see Column
     */
    @Override
    public String existsQuery(Table table, Column[] whereColumns) {
        table.check(whereColumns);

        StringBuilder builder = new StringBuilder()
                .append("select count(*) from ")
                .append(table.repr())
                .append(buildColumns(WHERE, whereColumns));

        return builder.toString();
    }

    /**
     * {@inheritDoc}
     *
     * @param table {@inheritDoc}
     * @param whereColumns {@inheritDoc}
     * @return {@inheritDoc}
     * @see Table
     * @see Column
     */
    @Override
    public String deleteQuery(Table table, Column[] whereColumns) {
        table.check(whereColumns);

        StringBuilder builder = new StringBuilder()
                .append("delete from ")
                .append(table.repr())
                .append(buildColumns(WHERE, whereColumns));

        return builder.toString();
    }

    /**
     * {@inheritDoc}
     *
     * @param table {@inheritDoc}
     * @param updateColumns {@inheritDoc}
     * @param whereColumns {@inheritDoc}
     * @return {@inheritDoc}
     * @see Table
     * @see Column
     */
    @Override
    public String updateQuery(Table table, Column[] updateColumns, Column[] whereColumns) {
        table.check(updateColumns);
        table.check(whereColumns);

        StringBuilder builder = new StringBuilder()
                .append("update ")
                .append(table.repr())
                .append(buildColumns(" set ", DELIMITER, updateColumns, Column::repr))
                .append(buildColumns(WHERE, whereColumns));

        return builder.toString();
    }

    /**
     * {@inheritDoc}
     *
     * @param gather {@inheritDoc}
     * @param result {@inheritDoc}
     * @param gatherMatcher {@inheritDoc}
     * @param resultMatcher {@inheritDoc}
     * @param whereColumns {@inheritDoc}
     * @return {@inheritDoc}
     * @see Table
     * @see Column
     */
    @Override
    public String crossoverQuery(Table gather, Table result, Column gatherMatcher, Column resultMatcher, Column[] whereColumns) {
        gather.check(gatherMatcher);
        result.check(resultMatcher);

        StringBuilder builder = new StringBuilder()
                .append(SELECT)
                .append(String.join(DELIMITER, mapColumns(result.selects(), Column::fullRepr)))
                .append(FROM)
                .append(gather.repr())
                .append(DELIMITER)
                .append(result.repr())
                .append(WHERE)
                .append(gatherMatcher.fullRepr())
                .append(" = ")
                .append(resultMatcher.fullRepr())
                .append(buildColumns(AND, AND, whereColumns, Column::fullRepr));

        return builder.toString();
    }

    /**
     * Helpmethod that uses the {@link Column#fullRepr()} (or any other
     * function) to map arrays of Columns onto a list of Strings containing
     * column names.
     *
     * @param columns The array containing the columns that need to be mapped
     * @param mapper The function to use for mapping
     * @return a list of string containing the column names
     * @see Column#fullRepr()
     */
    private List<String> mapColumns(Column[] columns, Function<Column, String> mapper) {
        return Arrays.stream(columns).map(mapper).collect(Collectors.toList());
    }

    /**
     * Helpmethod that just calls
     * {@link #buildColumns(java.lang.String, java.lang.String, be.ugent.vopro1.persistence.jdbc.Column[], java.util.function.Function)}
     * with "and" as the seperator and {@link Column#repr()} as the representer
     * function.
     *
     * @param start the first word of the result String to build
     * @param columns the columns that need to be 'iterated' to build the result
     * String
     * @return the result String to be used in the queries
     * @see Column#repr()
     * @see #buildColumns(java.lang.String, java.lang.String,
     * be.ugent.vopro1.persistence.jdbc.Column[], java.util.function.Function)
     */
    private String buildColumns(String start, Column[] columns) {
        return buildColumns(start, AND, columns, Column::repr);
    }

    /**
     *
     * @param start the first word of the result String to build
     * @param separator the seperator between the columns
     * @param columns the columns that need to be 'iterated' to build the result
     * String
     * @param representer the function that will map a Column to its
     * representation in the result String (name)
     * @return the result String to be used in the queries
     */
    private String buildColumns(String start, String separator, Column[] columns, Function<Column, String> representer) {
        if (columns != null && columns.length > 0) {
            return start + String.join(separator, mapColumns(columns, c -> representer.apply(c) + " = ?"));
        } else {
            return "";
        }
    }

}
