package be.ugent.vopro1.adapter.action.executor.copy;

import be.ugent.vopro1.adapter.ActionAdapter;
import be.ugent.vopro1.adapter.action.ProjectActionAdapter;
import be.ugent.vopro1.adapter.action.executor.AbstractExecutor;
import be.ugent.vopro1.adapter.action.executor.Executor;
import be.ugent.vopro1.reference.LookupConfig;
import be.ugent.vopro1.reference.DefaultLookupStrategy;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.funky.CustomWorkspace;
import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.interactor.entity.EntityInteractor;
import be.ugent.vopro1.interactor.entity.EntityPermission;
import be.ugent.vopro1.interactor.entity.InteractorFactory;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.model.ProcessEntity;
import be.ugent.vopro1.model.UsecaseEntity;
import be.ugent.vopro1.reference.LookupStrategy;
import be.ugent.vopro1.reference.LookupStrategyProvider;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.lang.funky.concept.Concept;
import org.aikodi.lang.funky.executors.Actor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Uses the executor interface to copy a usecase from one project to another.
 *
 * @see Executor
 * @see ProjectActionAdapter
 */
public class CopyExecutor extends AbstractExecutor {

    private static final String ACTION_TYPE = "copy";
    private static final String PROJECT_NAME_ARG = "projectName";
    private static final String NAME_ARG = "name";
    private static final String AUTH_ARG = "auth";
    private static final String ACTION_ARG = "action";
    private EntityPermission entityPermission;
    private Map<String, EntityInteractor<?>> interactorMap;

    /**
     * The constructor of this CopyExecutor.
     * <p>
     * Register the copier in the action adapter.
     *
     * @param adapter the adapter this executor should be registered to
     * @see ActionAdapter
     */
    public CopyExecutor(ActionAdapter adapter) {
        super(adapter, ACTION_TYPE);
        this.entityPermission = PermissionProvider.get("entity");

        EntityInteractor<UsecaseEntity> useCaseInteractor = InteractorFactory.getInstance();
        EntityInteractor<Actor> actorInteractor = InteractorFactory.getInstance();
        EntityInteractor<Concept> conceptInteractor = InteractorFactory.getInstance();
        EntityInteractor<ProcessEntity> processInteractor = InteractorFactory.getInstance();
        this.interactorMap = new HashMap<>();
        this.interactorMap.put("usecase", useCaseInteractor);
        this.interactorMap.put("actor", actorInteractor);
        this.interactorMap.put("concept", conceptInteractor);
        this.interactorMap.put("process", processInteractor);
    }

    /**
     * Copies a use case from the <code>origin</code> project to the
     * <code>destination</code> project.
     *
     * @param params {@inheritDoc}
     * @return {@inheritDoc}
     * @throws Exception thrown in {@link ProjectActionAdapter}
     * @see CopyAction
     * @see UsecaseEntity
     * @see EntityInteractor
     */
    @Override
    public Result execute(Map<String, String> params) throws Exception {
        CopyAction action = converter.convert(CopyAction.class, params.get(ACTION_ARG));
        String origin = params.get(PROJECT_NAME_ARG);
        String entityName = params.get(NAME_ARG);

        CustomWorkspace workspace = WorkspaceFactory.getInstance();
        UsecaseEntity useCase = workspace.getEntityFromProject(entityName, UsecaseEntity.class, origin);

        return supplier.consume(this::copyWithDescendants, useCase, action.getDestination());
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the credentials allow the user to get a use case from the
     * <code>origin</code> project and add an entity to the
     * <code>destination</code> project, this method will return
     * <code>true</code>.
     *
     * @param params {@inheritDoc}
     * @return {@inheritDoc}
     * @throws Exception thrown in {@link ProjectActionAdapter}
     * @see CopyAction
     * @see EntityPermission
     */
    @Override
    public boolean canExecute(Map<String, String> params) throws Exception {
        CopyAction action = converter.convert(CopyAction.class, params.get(ACTION_ARG));
        String origin = params.get(PROJECT_NAME_ARG);
        String auth = params.get(AUTH_ARG);

        return entityPermission.canGet(auth, origin) && entityPermission.canAdd(auth, action.getDestination());
    }

    /**
     * Private method that copies an entity and all its descendants recursively into a different project
     *
     * @param useCase Use case to copy to a different project
     * @param destination Name of the project that the use case and its descendants should be copied to
     */
    private void copyWithDescendants(UsecaseEntity useCase, String destination) {
        copyDescendants(useCase.name(), useCase.project().getName(), destination);

        EntityInteractor<UsecaseEntity> interactor = InteractorFactory.getInstance();
        interactor.add(destination, useCase);
    }

    /**
     * Private method that copies all descendants of entity recursively into a different project
     *
     * @param entityName Name of the entity whose descendants that should be copied
     * @param projectName Name of the project that the entity currently resides in
     * @param destination Name of the project that the descendants should be copied to
     */
    private void copyDescendants(String entityName, String projectName, String destination) {
        LookupConfig config = new LookupConfig();
        config = config.entityName(entityName).projectName(projectName).clazz(Declaration.class);

        LookupStrategy strategy = LookupStrategyProvider.get();
        Map<String, List<? extends Declaration>> references = strategy.findDescendants(config);

        for (String entityType : this.interactorMap.keySet()) {
            EntityInteractor interactor = interactorMap.get(entityType);
            for (Declaration declaration : references.get(entityType)) {
                if (! interactor.exists(destination, declaration)) {
                    interactor.add(destination, declaration);
                    copyDescendants(declaration.name(), projectName, destination);
                }
            }
        }
    }
}
