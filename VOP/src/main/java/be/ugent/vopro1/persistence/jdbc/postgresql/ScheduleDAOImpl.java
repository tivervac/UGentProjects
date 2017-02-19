package be.ugent.vopro1.persistence.jdbc.postgresql;

import be.ugent.vopro1.bean.PersistentAssignment;
import be.ugent.vopro1.bean.PersistentTask;
import be.ugent.vopro1.persistence.ScheduleDAO;
import be.ugent.vopro1.persistence.jdbc.AbstractDAO;
import be.ugent.vopro1.persistence.jdbc.Column;
import be.ugent.vopro1.persistence.jdbc.Table;
import be.ugent.vopro1.persistence.jdbc.postgresql.rowmapper.RowMapperProvider;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.AssignmentTable;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.TableFactory;
import be.ugent.vopro1.persistence.jdbc.postgresql.structure.TaskTable;
import org.springframework.jdbc.core.RowMapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * Implementation of the {@link ScheduleDAO} for PostgreSQL databases
 */
public class ScheduleDAOImpl extends AbstractDAO implements ScheduleDAO {

    private static final RowMapper<PersistentAssignment> ASSIGNMENT_MAPPER = RowMapperProvider.get(PersistentAssignment.class);
    private static final RowMapper<PersistentTask> TASK_MAPPER = RowMapperProvider.get(PersistentTask.class);
    private final QueryBuilderImpl queryBuilder = new QueryBuilderImpl();
    private final Table assignmentTable = TableFactory.getInstance("assignment");
    private final Table taskTable = TableFactory.getInstance("task");

    /**
     * {@inheritDoc}
     *
     * @param task {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public PersistentTask saveTaskForUseCase(PersistentTask task) {
        String query = queryBuilder.insertQuery(taskTable);
        Object[] args = {
                task.getUseCaseId(),
                task.getWorkload(),
                task.getPriority()
        };

        jdbcTemplate.update(query, args);
        return getTaskForUseCase(task.getUseCaseId());
    }

    /**
     * {@inheritDoc}
     *
     * @param useCaseId {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public PersistentTask getTaskForUseCase(int useCaseId) {
        String query = queryBuilder.selectQuery(
                taskTable,
                new Column[] {
                        TaskTable.TaskColumn.USE_CASE_ID
                }
        );

        return jdbcTemplate.queryForObject(query, TASK_MAPPER, useCaseId);
    }

    /**
     * {@inheritDoc}
     *
     * @param updated {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public PersistentTask updateTaskForUseCase(PersistentTask updated) {
        String query = queryBuilder.updateQuery(
                taskTable,
                new TaskTable.TaskColumn[] {
                        TaskTable.TaskColumn.WORKLOAD,
                        TaskTable.TaskColumn.PRIORITY
                },
                new TaskTable.TaskColumn[] {
                        TaskTable.TaskColumn.USE_CASE_ID
                }
        );
        Object[] args = {
                updated.getWorkload(),
                updated.getPriority(),
                updated.getUseCaseId()
        };

        jdbcTemplate.update(query, args);
        return getTaskForUseCase(updated.getUseCaseId());
    }

    /**
     * {@inheritDoc}
     *
     * @param useCaseId {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean existsTaskForUseCase(int useCaseId) {
        String query = queryBuilder.existsQuery(taskTable,
                new TaskTable.TaskColumn[] {
                        TaskTable.TaskColumn.USE_CASE_ID
                }
        );
        Object[] args = new Object[]{
                useCaseId
        };

        Integer count = jdbcTemplate.queryForObject(query, Integer.class, args);
        return count != null && count > 0;
    }

    /**
     * {@inheritDoc}
     *
     * @param useCaseId {@inheritDoc}
     */
    @Override
    public void deleteTaskForUseCase(int useCaseId) {
        String query = queryBuilder.deleteQuery(
                taskTable,
                new TaskTable.TaskColumn[]{
                        TaskTable.TaskColumn.USE_CASE_ID
                }
        );
        Object[] args = {
                useCaseId
        };

        jdbcTemplate.update(query, args);
    }

    /**
     * {@inheritDoc}
     *
     * @param projectId {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<PersistentAssignment> getAssignmentsForProject(int projectId) {
        String query = queryBuilder.selectQuery(
                assignmentTable,
                new Column[]{
                        AssignmentTable.AssignmentColumn.PROJECT_ID
                }
        );

        return jdbcTemplate.query(query, ASSIGNMENT_MAPPER, projectId);
    }

    /**
     * {@inheritDoc}
     *
     * @param projectId   {@inheritDoc}
     * @param assignments {@inheritDoc}
     */
    @Override
    public void saveAssignmentsForProject(int projectId, List<PersistentAssignment> assignments) {
        for (PersistentAssignment assignment : assignments) {
            String query = queryBuilder.insertQuery(assignmentTable);
            Object[] args = new Object[]{
                    assignment.getTaskId(),
                    assignment.getUserId(),
                    projectId,
                    new Timestamp(assignment.getInterval().getStartDate().getTime()),
                    new Timestamp(assignment.getInterval().getEndDate().getTime())
            };

            jdbcTemplate.update(query, args);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param projectId {@inheritDoc}
     */
    @Override
    public void removeSchedule(int projectId) {
        String query = queryBuilder.deleteQuery(assignmentTable, new Column[]{AssignmentTable.AssignmentColumn.PROJECT_ID});
        Object[] args = new Object[] {projectId};

        jdbcTemplate.update(query, args);
    }
}
