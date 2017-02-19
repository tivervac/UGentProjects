package be.ugent.vopro1.interactor.user;

import be.ugent.vopro1.model.AvailableUser;
import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.User;

import java.util.List;
import java.util.Optional;

/**
 * An object to interact with the database, concerning UserEntities.
 * <p>
 * A UserInteractor serves as a handle to the database, many operations such as
 * searching, adding, removing and editing database elements are supported
 * through the use of an {@link be.ugent.vopro1.persistence.UserDAO}.
 * <p>
 * Users retrieved from the database are formatted as PersistentUsers, the
 * Interactor uses a Converter to translate these objects to UserEntities.
 */
public interface UserInteractor {

    /**
     * Adds a User to the database.
     *
     * @param entity the user you wish to add
     * @return the added user
     * @see User
     */
    User addUser(User entity);

    /**
     * Edits a User from the database by giving the 'updated' User
     * the old user's id.
     *
     * @param id the user's id to edit
     * @param updated the edited user
     * @return the new user with id 'id'
     * @see User
     */
    User editUser(int id, User updated);

    /**
     * Edits a User from the database by giving the 'updated' User
     * the old user's id.
     *
     * @param id the user's id to edit
     * @param updated the edited user
     * @return the new user with id 'id'
     * @see User
     */
    User upgradeUser(int id, User updated);

    /**
     * Retrieves a User from the database.
     *
     * @param id the id of the user you wish to retrieve
     * @return the retrieved user
     * @see User
     */
    User getUser(int id);

    /**
     * Retrieves a User from the database.
     *
     * @param email the email of the user you wish to retrieve
     * @return the retrieved user
     * @see User
     */
    User getUser(String email);

    /**
     * Retrieves all UserEntities from the database.
     *
     * @return the UserEntities
     * @see User
     */
    List<User> getAllUsers();

    /**
     * Removes a user from the database.
     *
     * @param id the id of the user you wish to remove
     * @see User
     */
    void removeUser(int id);

    /**
     * Retrieve the identifier of a user with given e-mail address
     *
     * @param email E-mail of the user to retrieve identifier of
     * @return Identifier of the user if it exists
     */
    Optional<Integer> getId(String email);

    /**
     * A getter for the workhours on a project of an analyst. Used to create an AvailableUser.
     *
     * @param userEmail The email of the analyst
     * @param projectName The project on which the analyst works
     * @return The hours an analyst can work on the project
     * @see AvailableUser
     */
    long getWorkhours(String userEmail, String projectName);

    /**
     * Retrieves all teams this user belongs to.
     *
     * @param id the id of the user
     * @param analystOnly limit results to teams that the user is analyst in
     * @return all teams this user belongs to
     */
    List<Team> getTeams(int id, boolean analystOnly);

    /**
     * Retrieves all projects that this user is an analyst for.
     *
     * @param id Identifier of the user
     * @return all projects that this user is an analyst for
     */
    List<EntityProject> getAnalystProjects(int id);

}
