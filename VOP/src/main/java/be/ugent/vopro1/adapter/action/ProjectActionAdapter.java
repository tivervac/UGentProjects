package be.ugent.vopro1.adapter.action;

import be.ugent.vopro1.adapter.ActionAdapter;
import be.ugent.vopro1.adapter.action.executor.Executor;
import be.ugent.vopro1.adapter.result.mapper.ResultMapper;
import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.model.stub.ProjectActionEntity;

/**
 * Translates calls from REST and passes it to Interactors, thus operating the
 * database for Actions in projects.
 * <p>
 * The class ProjectActionAdapter communicates with Executors, its main goal is
 * to accept complex String objects as defined in the REST API and pass the
 * appropriate objects to the correct Executor. Through these executors the
 * database will be manipulated by different actions. The ProjectActionAdapter
 * class is responsible for directing these objects to the right Executor and
 * calling the operation suited for the required database operation. This class
 * mainly handles ProjectActionEntity objects.
 *
 * @see ActionAdapter
 * @see JsonConverter
 * @see ResultMapper
 * @see Action
 * @see Executor
 */
public class ProjectActionAdapter extends AbstractActionAdapter {

    /**
     * {@inheritDoc}
     *
     * @return {@inheritDoc}
     * @see ProjectActionEntity
     */
    @Override
    public Class getType() {
        return ProjectActionEntity.class;
    }
}
