package be.ugent.vopro1.persistence.jdbc.postgresql.rowmapper;

import be.ugent.vopro1.bean.PersistentAssignment;
import be.ugent.vopro1.persistence.jdbc.postgresql.ScheduleDAOImpl;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.AssignmentTable;
import be.ugent.vopro1.util.Interval;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * An implementation of the RowMapper interface, used by the ScheduleDaoImpl
 * to make PersistentAssignments (Assignments) out of queries.
 *
 * @see PersistentAssignment
 * @see ScheduleDAOImpl
 * @see RowMapper
 */
public class AssignmentMapper implements RowMapper<PersistentAssignment> {

    /**
     * Maps a row from a ResultSet to a PersistentAssignment (Assignment) to be
     * used in AssignmentDAOImpl.
     *
     * @param rs     the ResultSet retrieved from the database in which we will
     *               look for data
     * @param rowNum the number of the row of the ResultSet
     * @return the PersistentAssignment containing the data from the rownumber
     * rowNum from ResultSet rs
     * @see PersistentAssignment
     * @see ResultSet
     */
    @Override
    public PersistentAssignment mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PersistentAssignment.PersistentAssignmentBuilder.aPersistentAssignment()
                .userId(rs.getInt(AssignmentTable.AssignmentColumn.USER_ID.repr()))
                .taskId(rs.getInt(AssignmentTable.AssignmentColumn.TASK_ID.repr()))
                .interval(new Interval(
                        new Date(rs.getTimestamp(AssignmentTable.AssignmentColumn.START_DATE.repr()).getTime()),
                        new Date(rs.getTimestamp(AssignmentTable.AssignmentColumn.END_DATE.repr()).getTime())))
                .build();
    }
}
