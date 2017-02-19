package be.ugent.vopro1.persistence.jdbc.postgresql.rowmapper;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.persistence.jdbc.postgresql.ProjectDAOImpl;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.ProjectAnalystTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An implementation of the RowMapper interface, used by the ProjectDaoImpl
 * to make Longs out of workhour queries.
 *
 * @see PersistentProject
 * @see ProjectDAOImpl
 * @see RowMapper
 */
public class ProjectAnalystMapper implements RowMapper<Long> {

    /**
     * Maps a row from a ResultSet to a Long to be used in ProjectDAOImpl.
     *
     * @param rs the ResultSet retrieved from the database in which we will
     * look for data
     * @param rowNum the number of the row of the ResultSet
     * @return the Long representing an analyst's workhours
     * @see ResultSet
     */
    @Override
    public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
        return rs.getLong(ProjectAnalystTable.ProjectAnalystColumn.WORKHOURS.repr());
    }
}
