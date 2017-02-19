package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.action.executor.Executor;
import be.ugent.vopro1.adapter.result.Result;

import java.util.Map;

/**
 * An interface to provide the <code>act</code> and <code>register</code>
 * methods to all the actionAdapters.
 *
 * @see CommonAdapter
 */
public interface ActionAdapter extends CommonAdapter {

    /**
     * Performs an action through an Interactor, through an executor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see Result
     */
    Result act(Map<String, String> params);

    /**
     * Registers a {@link Executor} for an action with a certain name.
     *
     * @param name Name of the action that should be executed
     * @param executor Executor to be called for this action
     */
    void register(String name, Executor executor);

    /**
     * "get" is not supported in the ActionAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    default Result get(Map<String, String> params) {
        throw new UnsupportedOperationException("'get' is not supported by actionadapters");
    }

    /**
     * "getAll" is not supported in the ActionAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    default Result getAll(Map<String, String> params) {
        throw new UnsupportedOperationException("'getAll' is not supported by actionadapters");
    }

    /**
     * "add" is not supported in the ActionAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    default Result add(Map<String, String> params) {
        throw new UnsupportedOperationException("'add' is not supported by actionadapters");
    }

    /**
     * "remove" is not supported in the ActionAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    default Result remove(Map<String, String> params) {
        throw new UnsupportedOperationException("'remove' is not supported by actionadapters");
    }

    /**
     * "edit" is not supported in the ActionAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    default Result edit(Map<String, String> params) {
        throw new UnsupportedOperationException("'edit' is not supported by actionadapters");
    }
}
