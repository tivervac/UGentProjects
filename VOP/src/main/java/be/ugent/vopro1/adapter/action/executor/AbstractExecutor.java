package be.ugent.vopro1.adapter.action.executor;

import be.ugent.vopro1.adapter.ActionAdapter;
import be.ugent.vopro1.adapter.result.ResultSupplier;
import be.ugent.vopro1.adapter.result.ResultSupplierFactory;
import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;

/**
 * Provides an AbstractExecutor with a few basic fields that
 * Executor implementations will need.
 *
 * @see Executor
 */
public abstract class AbstractExecutor implements Executor {

    protected JsonConverter converter;
    protected ResultSupplier supplier;

    /**
     * Creates a new AbstractExecutor
     * @param adapter Adapter to register in
     * @param actionType Action type to register for
     */
    public AbstractExecutor(ActionAdapter adapter, String actionType) {
        this.converter = JsonConverterFactory.getInstance();
        this.supplier = ResultSupplierFactory.getInstance();
        adapter.register(actionType, this);
    }
}
