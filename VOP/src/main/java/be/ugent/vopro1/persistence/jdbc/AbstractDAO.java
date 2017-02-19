package be.ugent.vopro1.persistence.jdbc;

import be.ugent.vopro1.persistence.jdbc.postgresql.EntityDAOImpl;
import be.ugent.vopro1.persistence.jdbc.postgresql.ProjectDAOImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Super class of all DAOImplementations.
 *
 * @see EntityDAOImpl
 * @see ProjectDAOImpl
 */
public class AbstractDAO {

    protected DataSource dataSource;
    protected JdbcTemplate jdbcTemplate;

    /**
     * Sets the data source from which the DAO will extract data.
     * <p>
     * Also generates a new JdbcTemplate using the dataSource, to be used by the
     * DAO implementations.
     *
     * @param dataSource the dataSource, used to generate a JdbcTemplate
     * @see JdbcTemplate
     * @see DataSource
     */
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
}
