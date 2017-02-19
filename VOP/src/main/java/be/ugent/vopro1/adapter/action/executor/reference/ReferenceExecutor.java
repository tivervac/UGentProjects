package be.ugent.vopro1.adapter.action.executor.reference;

import be.ugent.vopro1.adapter.ActionAdapter;
import be.ugent.vopro1.adapter.action.ProjectActionAdapter;
import be.ugent.vopro1.adapter.action.executor.AbstractExecutor;
import be.ugent.vopro1.adapter.action.executor.Executor;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.types.SuccessResult;
import be.ugent.vopro1.interactor.entity.EntityPermission;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.model.ProcessEntity;
import be.ugent.vopro1.model.UsecaseEntity;
import be.ugent.vopro1.reference.LookupConfig;
import be.ugent.vopro1.reference.LookupStrategy;
import be.ugent.vopro1.reference.DefaultLookupStrategy;
import be.ugent.vopro1.reference.LookupStrategyProvider;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.lang.funky.concept.Concept;
import org.aikodi.lang.funky.executors.Actor;

import java.util.*;

/**
 * Uses the executor interface to retrieve use cases referring to an entity.
 *
 * @see Executor
 * @see ProjectActionAdapter
 */
public class ReferenceExecutor extends AbstractExecutor implements Executor {

    private static final String ACTION_TYPE = "reference";
    private static final String PROJECT_NAME_ARG = "projectName";
    private static final String NAME_ARG = "name";
    private static final String AUTH_ARG = "auth";
    private static final String ENTITY_TYPE_ARG = "type";
    private static BiMap<String, Class<? extends Declaration>> declarations = HashBiMap.create();
    static {
        declarations.put("actor", Actor.class);
        declarations.put("concept", Concept.class);
        declarations.put("usecase", UsecaseEntity.class);
        declarations.put("process", ProcessEntity.class);
    }

    private EntityPermission entityPermission;

    /**
     * The constructor of this ReferenceExecutor.
     * <p>
     * Register the reference finder in the action adapter.
     *
     * @param adapter the adapter this executor should be registered to
     * @see ActionAdapter
     */
    public ReferenceExecutor(ActionAdapter adapter) {
        super(adapter, ACTION_TYPE);

        this.entityPermission = PermissionProvider.get("entity");
    }

    @Override
    public Result execute(Map<String, String> params) throws Exception {
        String origin = params.get(PROJECT_NAME_ARG);
        String name = params.get(NAME_ARG);
        Class<? extends Declaration> clazz = declarations.get(params.get(ENTITY_TYPE_ARG));

        LookupConfig config = new LookupConfig();
        config = config.entityName(name).projectName(origin).clazz(clazz);

        LookupStrategy strategy = LookupStrategyProvider.get();
        return new Result(new SuccessResult(), strategy.findAncestors(config));
    }

    @Override
    public boolean canExecute(Map<String, String> params) throws Exception {
        String origin = params.get(PROJECT_NAME_ARG);
        String auth = params.get(AUTH_ARG);

        return entityPermission.canGet(auth, origin);
    }
}
