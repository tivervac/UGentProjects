package be.ugent.vopro1.persistence;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.jdbc.postgresql.ProjectDAOImpl;

import java.util.List;

/**
 * The interface that defines the methods to be implemented by the
 * ProjectDAOImpl.
 *
 * @see ProjectDAOImpl
 */
public interface ProjectDAO {

    /**
     * Saves a project to the database.
     *
     * @param persistentProject Project to be saved
     * @return Saved project, with the identifier given to it in the database
     * @see PersistentProject
     */
    PersistentProject save(PersistentProject persistentProject);

    /**
     * Retrieves a project by its name.
     *
     * @param name Name of the project to retrieve
     * @return Requested project
     * @see PersistentProject
     */
    PersistentProject getByName(String name);

    /**
     * Retrieves a project by its identifier.
     *
     * @param id Identifier of the project to retrieve
     * @return Requested project
     * @see PersistentProject
     */
    PersistentProject getById(int id);

    /**
     * Updates a project in the database. Only the project's <code>name</code>
     * can be updated.
     *
     * @param persistentProject Project to update
     * @see PersistentProject
     */
    void update(PersistentProject persistentProject);

    /**
     * Removes a project from the database.
     *
     * @param name Name of the project to delete
     * @see PersistentProject
     */
    void deleteByName(String name);

    /**
     * Removes a project from the database.
     *
     * @param id Identifier of the project to delete
     * @see PersistentProject
     */
    void deleteById(int id);

    /**
     * Retrieves a list of all projects in the database.
     *
     * @return All active projects in the database
     * @see PersistentProject
     */
    List<PersistentProject> getAll();

    /**
     * Checks if a project with given id exists
     *
     * @param id Identifier of the project to check
     * @return <code>true</code> if a project with given id exists,
     * <code>false</code> otherwise
     */
    boolean exists(int id);

    /**
     * Checks if a project with given name exists
     *
     * @param name Name of the team to check
     * @return <code>true</code> if a project with name exists,
     * <code>false</code> otherwise
     */
    boolean exists(String name);

    /**
     * Retrieves a list of all projects matching the given query.
     *
     * @param query a query that describes the PersistentProjects you wish to
     * find
     * @return a list of all PersistentProjects described by the query
     * @see PersistentProject
     * @see ProjectDAOImpl#search(String query)
     */
    List<PersistentProject> find(String query);

    /**
     * Retrieves a list of all teams that work on this project.
     *
     * @param id Identifier of the project
     * @return List of teams
     * @see PersistentTeam
     * @see List
     */
    List<PersistentTeam> getAllTeamsById(int id);

    /**
     * Retrieves a list of all teams that work on this project.
     *
     * @param name Name of the project
     * @return List of teams
     * @see PersistentTeam
     * @see List
     */
    List<PersistentTeam> getAllTeamsByName(String name);

    /**
     * Retrieves a list of all analysts that work on this project.
     *
     * @param id Identifier of the project
     * @return List of analysts (users)
     * @see PersistentUser
     * @see List
     */
    List<PersistentUser> getAllAnalystsById(int id);

    /**
     * Retrieves a list of all analysts that work on this project.
     *
     * @param name Name of the project
     * @return List of analysts (users)
     * @see PersistentUser
     * @see List
     */
    List<PersistentUser> getAllAnalystsByName(String name);

    /**
     * Adds an analyst to a project.
     *
     * @param projectId Identifier of the project
     * @param userId Identifier of the analyst
     * @param workload Time an analyst can work on the project, in seconds
     */
    void addAnalyst(int projectId, int userId, long workload);

    /**
     * Edits an analyst from a project.
     *
     * @param projectId Identifier of the project
     * @param userId Identifier of the analyst
     * @param workload Time an analyst can work on the project, in seconds
     */
    void editAnalyst(int projectId, int userId, long workload);

    /**
     * Removes an analyst from a project.
     *
     * @param projectId Identifier of the project
     * @param userId Identifier of the analyst
     */
    void deleteAnalyst(int projectId, int userId);

}
