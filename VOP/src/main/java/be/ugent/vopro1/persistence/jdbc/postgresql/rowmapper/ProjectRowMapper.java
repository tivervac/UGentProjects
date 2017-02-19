package be.ugent.vopro1.persistence.jdbc.postgresql.rowmapper;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.persistence.jdbc.postgresql.ProjectDAOImpl;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.ProjectTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An implementation of the RowMapper interface, used by the ProjectDaoImpl
 * to make PersistentProjects (Projects) out of queries.
 *
 * @see PersistentProject
 * @see ProjectDAOImpl
 * @see RowMapper
 */
public class ProjectRowMapper implements RowMapper<PersistentProject> {

    /**
     * Maps a row from a ResultSet to a PersistentProject (Project) to be
     * used in ProjectDAOImpl.
     *
     * @param rs the ResultSet retrieved from the database in which we will
     * look for data
     * @param rowNum the number of the row of the ResultSet
     * @return the PersistentProject containing the data from the rownumber
     * rowNum from ResultSet rs
     * @see PersistentProject
     * @see ResultSet
     */
    @Override
    public PersistentProject mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PersistentProject.PersistentProjectBuilder.aPersistentProject()
                .id(rs.getInt(ProjectTable.ProjectColumn.ID.repr()))
                .name(rs.getString(ProjectTable.ProjectColumn.NAME.repr()))
                .leader(rs.getInt(ProjectTable.ProjectColumn.LEADER_ID.repr()))
                .build();
    }
}
