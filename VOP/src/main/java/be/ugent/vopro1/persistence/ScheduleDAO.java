package be.ugent.vopro1.persistence;

import be.ugent.vopro1.bean.PersistentAssignment;
import be.ugent.vopro1.bean.PersistentTask;

import java.util.List;

/**
 * Interface for communication with the database
 *
 * Provides a way to persist schedules and tasks
 */
public interface ScheduleDAO {

    /**
     * Saves a task
     *
     * @param task Task to save
     * @return Saved task
     */
    PersistentTask saveTaskForUseCase(PersistentTask task);

    /**
     * Retrieves the task for given use case
     *
     * @param useCaseId Identifier of the use case
     * @return Persistent task for this use case
     */
    PersistentTask getTaskForUseCase(int useCaseId);

    /**
     * Updates a task
     *
     * @param updated Updated task
     * @return Updated task
     */
    PersistentTask updateTaskForUseCase(PersistentTask updated);

    /**
     * Checks if a task exists for given use case
     *
     * @param useCaseId Identifier of the use case
     * @return <code>true</code> if a task exists, <code>false</code> otherwise
     */
    boolean existsTaskForUseCase(int useCaseId);

    /**
     * Removes a task
     *
     * @param useCaseId Identifier of the use case to delete task for
     */
    void deleteTaskForUseCase(int useCaseId);

    /**
     * Retrieves the schedule for given project
     *
     * @param projectId Identifier of the project
     * @return Persistent schedule for this project
     */
    List<PersistentAssignment> getAssignmentsForProject(int projectId);

    /**
     * Saves the assignments for given project
     *
     * @param projectId Identifier of the project
     * @param assignments Schedule to save
     */
    void saveAssignmentsForProject(int projectId, List<PersistentAssignment> assignments);

    /**
     * Removes a schedule
     *
     * @param projectId The project who's schedule is going down
     */
    void removeSchedule(int projectId);
}
