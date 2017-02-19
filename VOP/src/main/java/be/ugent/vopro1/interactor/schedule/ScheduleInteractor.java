package be.ugent.vopro1.interactor.schedule;

import be.ugent.vopro1.model.Assignment;
import be.ugent.vopro1.model.Task;
import be.ugent.vopro1.scheduling.Schedule;
import org.aikodi.lang.funky.usecase.UseCase;

import java.util.List;

/**
 * An object to interact with the database.
 * <p>
 * An Interactor serves as a handle to the database, many operations such as
 * searching, adding, removing and editing database elements are supported
 * through the use of an ScheduleDAO.
 * <p>
 * Objects retrieved from the database are formatted as PersistentSchedule
 * or PersistentAssignment, the
 * Interactor uses a Converter to translate these objects to a type that are
 * more usable for the model.
 */
public interface ScheduleInteractor {

    /**
     * Retrieves the schedule for a project
     *
     * @param projectName Name of the project to retrieve schedule of
     * @return Schedule for a given project
     */
    Schedule getSchedule(String projectName);

    /**
     * Retrieves the schedule for a project as a list of Assignments
     *
     * @param projectName Name of the project to retrieve schedule of
     * @return List of Assignments representing the Schedule for a given project
     */
    List<Assignment> getScheduleAsAssignments(String projectName);


    /**
     * Saves the schedule for a project
     *
     * @param projectName The name of the project this schedule belongs to
     * @param assignments List of assignments for the schedule
     */
    void saveSchedule(String projectName, List<Assignment> assignments);

    /**
     * Retrieves the tasks associated with each of the given use cases
     *
     * @param projectName Project name that the use cases reside in
     * @param useCases List of use cases to retrieve tasks for
     * @return Tasks for given use cases
     */
    List<Task> getTasks(String projectName, List<String> useCases);

}
