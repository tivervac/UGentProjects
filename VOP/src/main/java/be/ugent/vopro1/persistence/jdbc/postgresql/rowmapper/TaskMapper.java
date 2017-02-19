package be.ugent.vopro1.persistence.jdbc.postgresql.rowmapper;

import be.ugent.vopro1.bean.PersistentTask;
import be.ugent.vopro1.persistence.jdbc.postgresql.ScheduleDAOImpl;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.TaskTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An implementation of the RowMapper interface, used by the ScheduleDaoImpl
 * to make PersistentTasks (Tasks) out of queries.
 *
 * @see PersistentTask
 * @see ScheduleDAOImpl
 * @see RowMapper
 */
public class TaskMapper implements RowMapper<PersistentTask> {

    /**
     * Maps a row from a ResultSet to a PersistentTask (Task) to be
     * used in TaskDAOImpl.
     *
     * @param rs     the ResultSet retrieved from the database in which we will
     *               look for data
     * @param rowNum the number of the row of the ResultSet
     * @return the PersistentTask containing the data from the rownumber
     * rowNum from ResultSet rs
     * @see PersistentTask
     * @see ResultSet
     */
    @Override
    public PersistentTask mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PersistentTask.PersistentTaskBuilder.aPersistentTask()
                .priority(rs.getInt(TaskTable.TaskColumn.PRIORITY.repr()))
                .workload(rs.getInt(TaskTable.TaskColumn.WORKLOAD.repr()))
                .useCaseId(rs.getInt(TaskTable.TaskColumn.USE_CASE_ID.repr()))
                .build();
    }
}
