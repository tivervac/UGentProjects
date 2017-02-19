package be.ugent.vopro1.interactor.project;

import be.ugent.vopro1.model.AvailableUser;
import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.User;

import java.util.List;

/**
 * An object to interact with the database, concerning EntityProjects.
 * <p>
 * A ProjectInteractor serves as a handle to the database, many operations such
 * as searching, adding, removing and editing database elements are supported
 * through the use of an {@link be.ugent.vopro1.persistence.ProjectDAO}.
 * <p>
 * Projects retrieved from the database are formatted as PersistentObjects, the
 * Interactor uses a Converter to translate these objects to EntityProjects.
 */
public interface ProjectInteractor {

    /**
     * Adds an EntityProject to the database.
     *
     * @param project the EntityProject to add to the database
     * @return the added project
     * @see EntityProject
     */
    EntityProject addProject(EntityProject project);

    /**
     * Edits an EntityProject from the database which basically renames the
     * Project.
     *
     * @param oldProject the EntityProject to edit
     * @param newProject the new, updated EntityProject
     * @return the updated project
     * @see EntityProject
     */
    EntityProject editProject(EntityProject oldProject, EntityProject newProject);

    /**
     * Retrieves an EntityProject from the database.
     *
     * @param project the EntityProject you wish to retrieve
     * @return the retrieved project
     * @see EntityProject
     */
    EntityProject getProject(EntityProject project);

    /**
     * Retrieves all EntityProjects from the database.
     *
     * @param filterTeamAssignable If <code>true</code>, only returns projects that have not reached the maximum
     *                             amount of teams assigned to them.
     *                             (currently {@value be.ugent.vopro1.util.LocalConstants#MAX_TEAMS_PER_PROJECT})
     * @return the EntityProjects
     * @see EntityProject
     * @see be.ugent.vopro1.util.LocalConstants
     */
    List<EntityProject> getAllProjects(boolean filterTeamAssignable);

    /**
     * Finds all EntityProjects conforming to a query in the database.
     * <p>
     * All EntityProjects which satisfy a query are sought and retrieved from
     * the database. The matching is based on Postgresql case insensitive
     * regular expression matching. Might throw an Exception when execution
     * fails.
     *
     * @param query the regex query to match
     * @return the found EntityProjects
     * @see EntityProject
     * @see
     * <a href="http://www.postgresql.org/docs/8.3/static/functions-matching.html#FUNCTIONS-POSIX-REGEXP">
     * Postgresql regex matching</a>
     */
    List<EntityProject> findProjects(String query);

    /**
     * Removes an EntityProject from the database.
     *
     * @param project the EntityProject to remove from the database
     * @return nothing
     * @see EntityProject
     */
    Void removeProject(EntityProject project);

    /**
     * Retrieves all teams associated with a project.
     *
     * @param id the id of the project we want to retrieve the teams from
     * @return a list of teams
     * @see Team
     */
    List<Team> getTeams(int id);

    /**
     * Retrieves all teams associated with a project.
     *
     * @param projectName the name of the project we want to retrieve the teams from
     * @return a list of teams
     * @see Team
     */
    List<Team> getTeams(String projectName);

    /**
     * Retrieves all analysts associated with a project.
     *
     * @param projectName the name of the project we want to retrieve the
     * analysts from
     * @return a list of users
     * @see User
     */
    List<AvailableUser> getAnalysts(String projectName);
    
    /**
     * Retrieves all users, eligible to be an analyst for a project.
     *
     * @param projectName the name of the project we want to retrieve the
     * eligible analysts from
     * @return a list of users
     * @see User
     */
    List<User> getEligibleAnalysts(String projectName);

    /**
     * Adds an analyst to the database.
     *
     * @param projectName the name of the project we want to add an analyst to
     * @param userId the id of the user that is an analyst of projectName
     * @param work the time an analyst can put into this project, in seconds
     */
    void addAnalyst(String projectName, int userId, long work);


    /**
     * Edits an analyst in the database.
     *
     * @param projectName the name of the project we want to edit an analyst from
     * @param userId the id of the user that is an analyst of projectName
     * @param workload the time an analyst can put into this project, in seconds
     */
    void editAnalyst(String projectName, int userId, long workload);

    /**
     * Removes an analyst from the database
     *
     * @param projectName the name of the project we want to remove an analyst
     * from
     * @param userId the id of the to-be-removed analyst
     */
    void removeAnalyst(String projectName, int userId);
}
