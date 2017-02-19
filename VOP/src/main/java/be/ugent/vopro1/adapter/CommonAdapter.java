package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;

import java.util.Map;
import java.util.Optional;

/**
 * Interface that all adapters should adhere to
 *
 * @see EntityAdapter
 * @see MemberTeamAdapter
 * @see ProjectAdapter
 * @see ProjectAnalystAdapter
 * @see TeamAdapter
 * @see TeamMemberAdapter
 * @see TeamProjectAdapter
 * @see UserAdapter
 */
public interface CommonAdapter {

    /**
     * Retrieves something from the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see Result
     */
    Result get(Map<String, String> params);

    /**
     * Retrieves everything from the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see Result
     */
    Result getAll(Map<String, String> params);

    /**
     * Adds something to the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see Result
     */
    Result add(Map<String, String> params);

    /**
     * Removes something from the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see Result
     */
    Result remove(Map<String, String> params);

    /**
     * Edits something in the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see Result
     */
    Result edit(Map<String, String> params);

    /**
     * Retrieves the identifier of the entity with given name in the database through an Interactor.
     * By default, this method throws an {@link UnsupportedOperationException}. Adapters where this
     * functionality is required should override it.
     *
     * @param uniqueName Unique name of the entity to use for identifier retrieval
     * @return The unique identifier, if an entity with the unique name is found
     * @throws UnsupportedOperationException if the implementing adapter does not support or require
     * this functionality
     */
    default Optional<Integer> getId(String uniqueName) {
        throw new UnsupportedOperationException("'getId' is not supported by this adapter");
    }

    /**
     * Retrieves the type of object that this adapter can be used for
     *
     * @return Type of object that this adapter can be used for
     */
    Class getType();
}
