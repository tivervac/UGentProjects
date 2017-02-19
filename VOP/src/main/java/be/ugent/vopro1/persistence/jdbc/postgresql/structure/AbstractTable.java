package be.ugent.vopro1.persistence.jdbc.postgresql.structure;

import be.ugent.vopro1.persistence.jdbc.Column;
import be.ugent.vopro1.persistence.jdbc.Table;

import java.util.Arrays;

/**
 * AbstractTable functions as a bridge between the effective Tables and the
 * {@link Table} interface.
 * <p>
 * It's all about its constructor; it uses the type parameter, which has to be a
 * Class that extends {@link Column}.
 *
 * @see Table
 */
public abstract class AbstractTable implements Table {

    private String representation;
    private Class<? extends Column> type;
    private Column[] columns;
    private Column[] selectColumns;
    private Column[] insertColumns;

    /**
     * Construct a new AbstractTable.
     *
     * @param repr the backend representation of the table
     * @param type the type of columns that will fill this table
     * @param columns all the columns of this table
     * @param selects all the columns that need to be selected for select
     * operations
     * @param inserts all the columns that need to be selected for insert
     * operations
     */
    public AbstractTable(String repr,
            Class<? extends Column> type,
            Column[] columns,
            Column[] selects,
            Column[] inserts) {
        this.representation = repr;
        this.type = type;
        this.columns = columns;
        this.selectColumns = selects;
        this.insertColumns = inserts;
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
    public Column[] columns() {
        return columns;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Column[] selects() {
        return selectColumns;
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    public Column[] inserts() {
        return insertColumns;
    }

    /**
     * {@inheritDoc}
     *
     * @param column {@inheritDoc}
     */
    @Override
    public void check(Column column) {
        if (column == null) {
            return;
        }

        if (column.getClass() != type) {
            throw new RuntimeException("Attempting to build query with unrelated columns. Problematic column: "
                    + column.fullRepr() + " in table " + repr());
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param columns {@inheritDoc}
     */
    @Override
    public void check(Column... columns) {
        if (columns == null) {
            return;
        }

        Arrays.stream(columns).forEach(this::check);
    }

}
