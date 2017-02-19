package be.ugent.vopro1.adapter.action;

import be.ugent.vopro1.adapter.ActionAdapter;
import be.ugent.vopro1.adapter.action.executor.Executor;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.mapper.ResultMapper;
import be.ugent.vopro1.adapter.result.mapper.ResultMapperFactory;
import be.ugent.vopro1.adapter.result.types.NoPermissionResult;
import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract implementation of an {@link ActionAdapter} with a few some common method implementations
 *
 * @see ProjectActionAdapter
 * @see ActionAdapter
 */
public abstract class AbstractActionAdapter implements ActionAdapter {

    private static final String ACTION_ARG = "action";
    private HashMap<String, Executor> executors;

    private JsonConverter converter;
    private ResultMapper resultMapper;

    /**
     * Creates a new AbstractActionAdapter and initializes the private fields.
     */
    public AbstractActionAdapter() {
        this.executors = new HashMap<>();
        this.resultMapper = ResultMapperFactory.getInstance();
        this.converter = JsonConverterFactory.getInstance();
    }

    /**
     * {@inheritDoc}
     *
     * @param params {@inheritDoc}
     * @return {@inheritDoc}
     * @see Action
     * @see Executor
     */
    @Override
    public Result act(Map<String, String> params) {
        Optional<String> actionBlob = Optional.ofNullable(params.get(ACTION_ARG));

        try {
            Action action = converter.convert(Action.class, actionBlob.get());
            Executor actionExecutor = executors.get(action.getAction());

            if (!actionExecutor.canExecute(params)) {
                return new Result<>(new NoPermissionResult());
            }

            return actionExecutor.execute(params);
        } catch (Exception e) {
            return resultMapper.mapException(e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param name     {@inheritDoc}
     * @param executor {@inheritDoc}
     */
    @Override
    public void register(String name, Executor executor) {
        executors.put(name, executor);
    }

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     */
    @Override
    abstract public Class getType();
}
