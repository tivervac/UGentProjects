package be.ugent.vopro1.interactor.task;

import be.ugent.vopro1.model.Task;

import java.util.List;

/**
 * An object to interact with the database.
 * <p>
 * An Interactor serves as a handle to the database, many operations such as
 * searching, adding, removing and editing database elements are supported
 * through the use of an ScheduleDAO.
 * <p>
 * Objects retrieved from the database are formatted as PersistentTask, the
 * Interactor uses a Converter to translate these objects to a type that are
 * more usable for the model.
 */
public interface TaskInteractor {

    /**
     * Creates a new task for a use case in a project
     *
     * @param projectName Project that the use case resides in
     * @param useCaseName Use case that the task is for
     * @param task Task (workload, priority) to save
     * @return the created task
     */
    Task createTask(String projectName, String useCaseName, Task task);

    /**
     * Retrieves a task for a project and use case
     *
     * @param projectName Project name of the task
     * @param useCaseName Use case name of the task
     * @return the requested task
     */
    Task getTask(String projectName, String useCaseName);

    /**
     * Retrieves all tasks of a project
     *
     * @param projectName Project name to retrieve tasks from
     * @return the requested tasks
     */
    List<Task> getTasks(String projectName);

    /**
     * Updates the task for a certain use case in a project
     *
     * @param projectName Project name that the use case resides in
     * @param useCaseName Use case that the task is for
     * @param updatedTask Updated task object (priority, workload) to save
     * @return the updated task
     */
    Task updateTask(String projectName, String useCaseName, Task updatedTask);

    /**
     * Removes a task for a use case in a project
     *
     * @param projectName Name of the project that the use case resides in
     * @param useCaseName Name of the use case that the task is for
     */
    void deleteTask(String projectName, String useCaseName);

}
