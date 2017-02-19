package be.ugent.vopro1.persistence;

import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTeam;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.jdbc.postgresql.TeamDAOImpl;

import java.util.List;

/**
 * The interface that defines the methods to be implemented by the TeamDAOImpl.
 *
 * @see TeamDAOImpl
 */
public interface TeamDAO {

    /**
     * Saves a team to the database
     *
     * @param team Team to be saved
     * @return Saved team, with the identifier given to it by the database
     * @see PersistentTeam
     */
    PersistentTeam save(PersistentTeam team);

    /**
     * Retrieves a team by its identifier
     *
     * @param id Id of the team to retrieve
     * @return Requested team
     * @see PersistentTeam
     */
    PersistentTeam getById(int id);

    /**
     * Retrieves a team by its name
     *
     * @param name Name of the team to retrieve
     * @return Requested team
     * @see PersistentTeam
     */
    PersistentTeam getByName(String name);

    /**
     * Checks if a team with given id exists
     *
     * @param id Identifier of the team to check
     * @return <code>true</code> if a team with given id exists,
     * <code>false</code> otherwise
     */
    boolean exists(int id);

    /**
     * Checks if a team with given e-mail exists
     *
     * @param name Name of the team to check
     * @return <code>true</code> if a team with name exists,
     * <code>false</code> otherwise
     */
    boolean exists(String name);

    /**
     * Retrieves a list of all teams in the database
     *
     * @return All active teams in the database
     * @see PersistentTeam
     * @see List
     */
    List<PersistentTeam> getAll();

    /**
     * Updates a team in the database. Only the team's <code>name</code> can be
     * updated.
     *
     * @param team Team to update
     * @see PersistentTeam
     */
    void update(PersistentTeam team);

    /**
     * Removes a team from the database
     *
     * @param id Identifier of the team to remove
     * @see PersistentTeam
     */
    void deleteById(int id);

    /**
     * Removes a team from the database
     *
     * @param name Name of the team to remove
     * @see PersistentTeam
     */
    void deleteByName(String name);

    /**
     * Returns all members of the requested team
     *
     * @param id Identifier of the team you wish to retrieve users for
     * @return Users that are part of this team
     * @see PersistentTeam
     * @see PersistentUser
     * @see List
     */
    List<PersistentUser> getAllMembersById(int id);

    /**
     * Returns all members of the requested team
     *
     * @param name Name of the team you wish to retrieve users for
     * @return Users that are part of this team
     * @see PersistentTeam
     * @see PersistentUser
     * @see List
     */
    List<PersistentUser> getAllMembersByName(String name);

    /**
     * Adds a user to a team
     *
     * @param teamId Identifier of the team
     * @param userId Identifier of the user that should be added
     */
    void addMember(int teamId, int userId);

    /**
     * Removes a user from a team
     *
     * @param teamId Identifier of the team
     * @param userId Identifier of the user that should be removed
     */
    void deleteMember(int teamId, int userId);

    /**
     * Returns all projects of the requested team
     *
     * @param id Identifier of the team you wish to retrieve projects for
     * @return Projects that are part of this team
     * @see PersistentTeam
     * @see PersistentTeam
     * @see List
     */
    List<PersistentProject> getAllProjectsById(int id);

    /**
     * Returns all projects of the requested team
     *
     * @param name Name of the team you wish to retrieve projects for
     * @return Projects that are part of this team
     * @see PersistentTeam
     * @see PersistentTeam
     * @see List
     */
    List<PersistentProject> getAllProjectsByName(String name);

    /**
     * Adds a project to a team
     *
     * @param teamId Identifier of the team
     * @param projectId Identifier of the project that should be added
     */
    void addProject(int teamId, int projectId);

    /**
     * Removes a project from a team
     *
     * @param teamId Identifier of the team
     * @param projectId Identifier of the project that should be removed
     */
    void deleteProject(int teamId, int projectId);
}
