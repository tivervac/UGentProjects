package be.ugent.vopro1.adapter.action.executor;

import be.ugent.vopro1.adapter.result.Result;

import java.util.Map;

/**
 * An interface for executors, based on the 'commando'-pattern.
 */
public interface Executor {

    /**
     * Executes a command given a Map of basic parameters.
     *
     * @param params the map containing the parameters
     * @return a Result object containing information about the execution
     * @throws Exception if something goes wrong during the execution
     */
    Result execute(Map<String, String> params) throws Exception;

    /**
     * Checks if a command can be executed by checking the given credentials
     * contained in the params argument.
     *
     * @param params the map containing the parameters
     * @return <code>true</code> if the command can be executed, else
     * <code>false</code>
     * @throws Exception if something goes wrong during the execution
     */
    boolean canExecute(Map<String, String> params) throws Exception;
}
