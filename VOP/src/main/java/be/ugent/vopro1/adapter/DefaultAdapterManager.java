package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;

import java.util.Map;
import java.util.Optional;

/**
 * An implementation of the AdapterManager.
 *
 * @see AdapterManager
 */
public class DefaultAdapterManager implements AdapterManager {

    /**
     * {@inheritDoc}
     *
     * @param type type of the entity to add
     * @param args the data map containing literal Strings and objects in JSON
     * format
     * @return a Result object containing information about the execution
     * @see Result
     */
    @Override
    public Result add(Class type, Map<String, String> args) {
        CommonAdapter adapter = CommonAdapterFactory.getInstance(type);
        return adapter.add(args);
    }

    /**
     * {@inheritDoc}
     *
     * @param type type of the entity to edit
     * @param args the data map containing literal Strings and objects in JSON
     * format
     * @return a Result object containing information about the execution
     * @see Result
     */
    @Override
    public Result edit(Class type, Map<String, String> args) {
        CommonAdapter adapter = CommonAdapterFactory.getInstance(type);
        return adapter.edit(args);
    }

    /**
     * {@inheritDoc}
     *
     * @param type type of the entity to remove
     * @param args the data map containing literal Strings and objects in JSON
     * format
     * @return a Result object containing information about the execution
     * @see Result
     */
    @Override
    public Result remove(Class type, Map<String, String> args) {
        CommonAdapter adapter = CommonAdapterFactory.getInstance(type);
        return adapter.remove(args);
    }

    /**
     * {@inheritDoc}
     *
     * @param type type of the entity to retrieve
     * @param args the data map containing literal Strings and objects in JSON
     * format
     * @return a Result object containing information about the execution
     * @see Result
     */
    @Override
    public Result get(Class type, Map<String, String> args) {
        CommonAdapter adapter = CommonAdapterFactory.getInstance(type);
        return adapter.get(args);
    }

    @Override
    public Optional<Integer> getId(Class type, String uniqueName) {
        CommonAdapter adapter = CommonAdapterFactory.getInstance(type);
        return adapter.getId(uniqueName);
    }

    /**
     * {@inheritDoc}
     *
     * @param type type of the entities to retrieve
     * @param args the data map containing literal Strings and objects in JSON
     * format
     * @return a Result object containing information about the execution
     * @see Result
     */
    @Override
    public Result getAll(Class type, Map<String, String> args) {
        CommonAdapter adapter = CommonAdapterFactory.getInstance(type);
        return adapter.getAll(args);
    }

    /**
     * {@inheritDoc}
     *
     * @param type {@inheritDoc}
     * @param args {@inheritDoc}
     * @return {@inheritDoc}
     * @see Result
     */
    @Override
    public Result act(Class type, Map<String, String> args) {
        ActionAdapter adapter = (ActionAdapter) CommonAdapterFactory.getInstance(type);
        return adapter.act(args);
    }
}
