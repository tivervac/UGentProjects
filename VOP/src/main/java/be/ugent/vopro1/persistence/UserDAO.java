package be.ugent.vopro1.persistence;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.jdbc.postgresql.UserDAOImpl;

import java.util.List;

/**
 * The interface that defines the methods to be implemented by the UserDAOImpl.
 *
 * @see UserDAOImpl
 */
public interface UserDAO {

    /**
     * Saves a user to the database
     *
     * @param user User to be saved
     * @return Saved user, with the identifier given to it by the database
     * @see PersistentUser
     */
    PersistentUser save(PersistentUser user);

    /**
     * Retrieves a user by its identifier
     *
     * @param id Identifier of the user to retrieve
     * @return Requested user
     * @see PersistentUser
     */
    PersistentUser getById(int id);

    /**
     * Retrieves a user by its e-mail
     *
     * @param email E-mail of the user to retrieve
     * @return Requested user
     * @see PersistentUser
     */
    PersistentUser getByEmail(String email);

    /**
     * Checks if a user with given id exists
     *
     * @param id Identifier of the user to check
     * @return <code>true</code> if a user with given id exists,
     * <code>false</code> otherwise
     */
    boolean exists(int id);

    /**
     * Checks if a user with given e-mail exists
     *
     * @param email E-mail of the user to check
     * @return <code>true</code> if a user with given e-mail exists,
     * <code>false</code> otherwise
     */
    boolean exists(String email);

    /**
     * Retrieves a list of all users in the database
     *
     * @return All active users in the database
     * @see PersistentUser
     * @see List
     */
    List<PersistentUser> getAll();

    /**
     * Updates a user in the database. The following properties should be
     * updatable:
     * <ul>
     * <li>firstName</li>
     * <li>lastName</li>
     * <li>admin</li>
     * <li>analyst</li>
     * </ul>
     * Other properties can be updatable should the implementor prefer that.
     *
     * @param user User to update
     * @see PersistentUser
     */
    void update(PersistentUser user);

    /**
     * Removes a user from the database
     *
     * @param id Identifier of the user to remove
     * @see PersistentUser
     */
    void deleteById(int id);

    /**
     * Removes a user from the database
     *
     * @param email Email of the user to remove
     * @see PersistentUser
     */
    void deleteByEmail(String email);

    /**
     * Retrieves a list of all teams that a user is part of
     *
     * @param id Identifier of the user to retrieve teams for
     * @return List of teams
     * @see PersistentTeam
     * @see List
     */
    List<PersistentTeam> getAllTeamsById(int id);

    /**
     * Retrieves a list of all teams that a user is part of
     *
     * @param email E-mail of the user to retrieve teams for
     * @return List of teams
     * @see PersistentTeam
     * @see List
     */
    List<PersistentTeam> getAllTeamsByEmail(String email);

    /**
     * Retrieves a list of all projects that a user is an analyst for
     *
     * @param id Identifier of the user to retrieve analyst projects for
     * @return List of projects
     * @see be.ugent.vopro1.bean.PersistentProject
     * @see List
     */
    List<PersistentProject> getAllAnalystProjectsById(int id);

    /**
     * Gets the hours an analyst wants to work on a project
     *
     * @param userId the id of the analyst
     * @param projectId the id of the project
     * @return the workhours in seconds
     */
    long getWorkhours(int userId, int projectId);

    /**
     * Retrieves a list of all projects that a user is an analyst for
     *
     * @param email E-mail of the user to retrieve analyst projects for
     * @return List of projects
     * @see be.ugent.vopro1.bean.PersistentProject
     * @see List
     */
    List<PersistentProject> getAllAnalystProjectsByEmail(String email);
}
