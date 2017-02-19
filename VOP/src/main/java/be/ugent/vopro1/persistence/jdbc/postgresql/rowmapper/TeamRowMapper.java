package be.ugent.vopro1.persistence.jdbc.postgresql.rowmapper;

import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.persistence.jdbc.postgresql.TeamDAOImpl;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.TeamTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An implementation of the RowMapper interface, used by the ProjectDaoImpl
 * to make PersistentTeams (Teams) out of queries.
 *
 * @see PersistentTeam
 * @see TeamDAOImpl
 * @see RowMapper
 */
public class TeamRowMapper implements RowMapper<PersistentTeam> {

    /**
     * Maps a row from a ResultSet to a PersistentTeam (team) to be used in
     * TeamDAOImpl.
     *
     * @param rs the ResultSet retrieved from the database in which we will
     * look for data
     * @param rowNum the number of the row of the ResultSet
     * @return the PersistentTeam containing the data from the rownumber
     * rowNum from ResultSet rs
     * @see PersistentTeam
     * @see ResultSet
     */
    @Override
    public PersistentTeam mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PersistentTeam.PersistentTeamBuilder.aPersistentTeam()
                .id(rs.getInt(TeamTable.TeamColumn.ID.repr()))
                .name(rs.getString(TeamTable.TeamColumn.NAME.repr()))
                .leader(rs.getInt(TeamTable.TeamColumn.LEADER_ID.repr()))
                .build();
    }
}
