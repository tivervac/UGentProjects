package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;

import java.util.Map;
import java.util.Optional;

/**
 * An interface for REST layer classes to access and perform database
 * operations.
 * <p>
 * The AdapterManager interface determines the signature of a class which serves
 * as an adapter manager. It contains calls for all sorts of database
 * operations. An instance of an implemented class should suffice for all
 * operations so the interface serves as contact point between the adapter
 * package and the above layers. Any package calling methods from the adapter
 * package should do so through this interface.
 */
public interface AdapterManager {

    /**
     * Adds an object of type 'type' to the database.
     *
     * @param type type of the entity to add
     * @param args the data map containing literal Strings and objects in JSON
     * format
     * @return a Result object containing information about the execution
     * @see Result
     */
    Result add(Class type, Map<String, String> args);

    /**
     * Edits an object of type 'type' in the database.
     *
     * @param type type of the entity to edit
     * @param args the data map containing literal Strings and objects in JSON
     * format
     * @return a Result object containing information about the execution
     * @see Result
     */
    Result edit(Class type, Map<String, String> args);

    /**
     * Removes an object of type 'type' from the database.
     *
     * @param type type of the entity to remove
     * @param args the data map containing literal Strings and objects in JSON
     * format
     * @return a Result object containing information about the execution
     * @see Result
     */
    Result remove(Class type, Map<String, String> args);

    /**
     * Gets an object of type 'type' from the database.
     *
     * @param type type of the entity to retrieve
     * @param args the data map containing literal Strings and objects in JSON
     * format
     * @return a Result object containing information about the execution
     * @see Result
     */
    Result get(Class type, Map<String, String> args);

    /**
     * Retrieves the identifier of the entity with given 'uniqueName' in the database through an Interactor.
     *
     * @param type type of entity to retrieve
     * @param uniqueName unique name of the entity to use for identifier retrieval
     * @return The unique identifier, if an entity with the unique name is found
     */
    Optional<Integer> getId(Class type, String uniqueName);

    /**
     * Gets all objects of type 'type' from the database.
     * <p>
     * When the arguments Map contains a 'name' key, the corresponding value
     * acts as a query parameter. In that case only objects whose name matches
     * this query parameter will be returned.
     *
     * @param type type of the entities to retrieve
     * @param args the data map containing literal Strings and objects in JSON
     * format
     * @return a Result object containing information about the execution
     * @see Result
     */
    Result getAll(Class type, Map<String, String> args);

    /**
     * Performs an action on an object of type 'type' from the database.
     * <p>
     * When the arguments Map contains a 'name' key, the corresponding value
     * acts as a query parameter. In that case only objects whose name matches
     * this query parameter will be returned.
     *
     * @param type type of the entities to retrieve
     * @param args the data map containing literal Strings and objects in JSON
     * format
     * @return a Result object containing information about the execution
     * @see Result
     */
    Result act(Class type, Map<String, String> args);

}
