package be.ugent.vopro1.persistence;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.jdbc.postgresql.EntityDAOImpl;

import java.util.List;

/**
 * The interface that defines the methods to be implemented by the
 * EntityDAOImpl.
 *
 * @see EntityDAOImpl
 */
public interface EntityDAO {

    /**
     * Saves an Entity(project_id, name, type, blob) in the database.
     *
     * @param persistentObject the PersistentObject to be saved
     * @return a PersistentObject to check if the correct PersistentObject was
     * updated
     * @see PersistentObject
     */
    PersistentObject save(PersistentObject persistentObject);

    /**
     * Search for an Entity in the database by its name and projectid.
     *
     * @param name the name of the PersistentObject to be searched
     * @param projectId the project_id of the project where the PersistentObject
     * is saved
     * @return contains the PersistentObject with the specified name and
     * projectid
     * @see PersistentObject
     */
    PersistentObject getByName(String name, int projectId);

    /**
     * Search for an Entity in the database by its name and projectName.
     *
     * @param name the name of the PersistentObject to be searched
     * @param projectName the name of the project where the PersistentObject is
     * saved
     * @return contains the PersistentObject with the specified name and
     * projectName
     * @see PersistentObject
     */
    PersistentObject getByName(String name, String projectName);

    /**
     * Checks if an entity with given id exists
     *
     * @param id Identifier of the entity to check
     * @return <code>true</code> if an entity with given id exists,
     * <code>false</code> otherwise
     */
    boolean exists(int id);

    /**
     * Checks if a entity with given name exists in a given project
     *
     * @param name Name of the entity to check
     * @param projectName Name of the project to check in
     * @return <code>true</code> if an entity with given name exists in the project,
     * <code>false</code> otherwise
     */
    boolean exists(String name, String projectName);


    /**
     * Checks if a entity with given name exists in a given project
     *
     * @param name Name of the entity to check
     * @param projectId Identifier of the project to check in
     * @return <code>true</code> if an entity with given name exists in the project,
     * <code>false</code> otherwise
     */
    boolean exists(String name, int projectId);

    /**
     * Search for an Entity in the database by its unique id.
     *
     * @param id the id of the PersistentObject in the database
     * @return contains the PersistentObject with the specified id
     * @see PersistentObject
     */
    PersistentObject getById(int id);

    /**
     * Update the information of an Entity in the database.
     *
     * @param persistentObject the PersistentObject containing updated
     * information
     * @see PersistentObject
     */
    void update(PersistentObject persistentObject);

    /**
     * Delete an Entity from the database by its name and projectid.
     *
     * @param name the name of the PersistentObject to be deleted
     * @param projectId the project_id of the project where the PersistentObject
     * is saved
     * @see PersistentObject
     */
    void deleteByName(String name, int projectId);

    /**
     * Removes an entity from the database.
     *
     * @param name Name of the entity to delete
     * @param projectName Name of the project the entity resides in
     */
    void deleteByName(String name, String projectName);

    /**
     * Delete an Entity from the database by its unique id.
     *
     * @param id the id of the PersistentObject in the database
     * @see PersistentObject
     */
    void deleteById(int id);

    /**
     * Request all Entities in the database.
     *
     * @return a list of all the PersistentObjects in the document database
     * @see PersistentObject
     */
    List<PersistentObject> getAll();

    /**
     * Search for all Entities in the database from the specified project.
     *
     * @param projectName the name of the project you wish the retrieve all
     * PersistentObjects from
     * @return a list of all the PersistentObjects in the document database from
     * the specified project
     * @see PersistentObject
     */
    List<PersistentObject> getAllForProject(String projectName);

    /**
     * Retrieves a list of all Entities matching the given query.
     *
     * @param query a query that describes the PersistentObjects you wish to
     * find
     * @return a list of all PersistentObjects described by the query
     * @see PersistentObject
     * @see EntityDAOImpl#find(String query, String projectName)
     */
    List<PersistentObject> find(String query);

    /**
     * Retrieves a list of all Entities in a project matching the given query.
     *
     * @param query a query that describes the PersistentObjects you wish to
     * find
     * @param projectName the name of the project in which you want to execute
     * the query
     * @return a list of all PersistentObjects described by the query and in the
     * project described by projectName
     * @see PersistentObject
     * @see EntityDAOImpl#find(String query, String projectName)
     */
    List<PersistentObject> findInProject(String projectName, String query);

    /**
     * Retrieves a list of all analysts that work on this entity.
     *
     * @param name Name of the entity
     * @param projectName Name of the project
     * @return List of analysts (users)
     * @see PersistentUser
     * @see List
     */
    List<PersistentUser> getAllAnalysts(String name, String projectName);

    /**
     * Adds an analyst to an entity.
     *
     * @param name Name of the entity
     * @param projectName Name of the project
     * @param userId Identifier of the analyst
     */
    void addAnalyst(String name, String projectName, int userId);

    /**
     * Removes an analyst from an entity.
     *
     * @param name Name of the entity
     * @param projectName Name of the project
     * @param userId Identifier of the analyst
     */
    void deleteAnalyst(String name, String projectName, int userId);
}
