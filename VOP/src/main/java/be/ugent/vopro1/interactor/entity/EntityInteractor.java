package be.ugent.vopro1.interactor.entity;

import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.User;
import org.aikodi.chameleon.core.declaration.Declaration;

import java.util.List;

/**
 * An object to interact with the database.
 * <p>
 * An Interactor serves as a handle to the database, many operations such as
 * searching, adding, removing and editing database elements are supported
 * through the use of an EntityDAO.
 * <p>
 * Objects retrieved from the database are formatted as PersistentObjects, the
 * Interactor uses a Converter to translate these objects to a type that are
 * more usable for the model.
 *
 * @param <E> the type of the objects to interact with the database with
 * @see Declaration
 */
public interface EntityInteractor<E extends Declaration> {

    /**
     * Adds an object to the database.
     * <p>
     * An object of the generic type is added to the database. The object will
     * be associated with a certain EntityProject. Might throw an Exception when
     * execution fails.
     *
     * @param projectName the name of the project containing the entity
     * @param t the object to add to the database
     * @return the added object
     * @see EntityProject
     */
    E add(String projectName, E t);

    /**
     * Checks if an object exists in the project.
     *
     * @param projectName the name of the project to check of entity existence
     * @param t object to check for
     * @return <code>true</code> if the project already contains the entity,
     * <code>false</code> otherwise
     */
    boolean exists(String projectName, E t);

    /**
     * Removes an object from the database.
     * <p>
     * An object of the generic type is removed from the database. The object is
     * associated with a certain EntityProject. Might throw an Exception when
     * execution fails.
     *
     * @param projectName the name of the project containing the entity
     * @param t the object to remove from the database
     * @see EntityProject
     */
    void remove(String projectName, E t);

    /**
     * Retrieves an object from the database.
     * <p>
     * An object of the generic type is retrieved from the database. The object
     * is associated with a certain EntityProject. Might throw an Exception when
     * execution fails.
     *
     * @param projectName the name of the project containing the entity
     * @param t the object to add to the database
     * @return the requested object
     * @see EntityProject
     */
    E get(String projectName, E t);

    /**
     * Edits an object from the database.
     * <p>
     * An object of the generic type is updated in the database. The object is
     * be associated with a certain EntityProject. Might throw an Exception when
     * execution fails.
     *
     * @param projectName the name of the project containing the entity
     * @param old the object to edit
     * @param t the new, updated object
     * @param refactorStrategy strategy to use for refactoring the model
     * @return the updated object
     * @see EntityProject
     */
    E edit(String projectName, E old, E t, RefactorStrategy refactorStrategy);

    /**
     * Retrieves all objects from a project from the database.
     * <p>
     * All objects of the generic type in a certain project are retrieved. Might
     * throw an Exception when execution fails.
     *
     * @param projectName the name of the project containing the entity
     * @param type type of entity to retrieve
     * @return the List of objects
     * @see EntityProject
     */
    List<E> getAll(String projectName, String type);

    /**
     * Finds all objects conforming to a query in the database.
     * <p>
     * All objects of the generic type which satisfy a query are sought and
     * retrieved from the database. The matching is based on Postgresql case
     * insensitive regular expression matching. Might throw an Exception when
     * execution fails.
     *
     * @param projectName the name of the project containing the entity
     * @param query the regex query to match
     * @param type type of entity to retrieve
     * @return the List of found objects
     * @see EntityProject
     * @see
     * <a href="http://www.postgresql.org/docs/8.3/static/functions-matching.html#FUNCTIONS-POSIX-REGEXP">
     * Postgresqlregex matching </a>
     */
    List<E> find(String projectName, String query, String type);

    /**
     * Retrieves all analysts associated with an entity in a project.
     *
     * @param projectName the name of the project
     * @param name the name of the entity to retrieve analysts for
     * @return a list of users
     * @see User
     */
    List<User> getAnalysts(String projectName, String name);

    /**
     * Adds an analyst for an entity in a project to the database.
     *
     * @param projectName the name of the project
     * @param name the name of the entity to add analyst to
     * @param userId the id of the user that is an analyst of the entity
     */
    void addAnalyst(String projectName, String name, int userId);

    /**
     * Removes an analyst for an entity in a project from the database.
     *
     * @param projectName the name of the project
     * @param name the name of the entity to add analyst to
     * @param userId the id of the user that is an analyst of the entity
     */
    void removeAnalyst(String projectName, String name, int userId);
}
