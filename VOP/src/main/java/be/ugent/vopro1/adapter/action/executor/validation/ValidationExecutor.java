package be.ugent.vopro1.adapter.action.executor.validation;

import be.ugent.vopro1.adapter.ActionAdapter;
import be.ugent.vopro1.adapter.action.ProjectActionAdapter;
import be.ugent.vopro1.adapter.action.executor.AbstractExecutor;
import be.ugent.vopro1.adapter.action.executor.Executor;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.types.SuccessResult;
import be.ugent.vopro1.funky.CustomWorkspace;
import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.interactor.entity.EntityPermission;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.model.UsecaseEntity;
import org.aikodi.chameleon.core.validation.Verification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Uses the executor interface to validate a use case.
 *
 * @see Executor
 * @see ProjectActionAdapter
 */
public class ValidationExecutor extends AbstractExecutor {

    private static final String ACTION_TYPE = "validation";
    private static final String PROJECT_NAME_ARG = "projectName";
    private static final String NAME_ARG = "name";
    private static final String AUTH_ARG = "auth";
    private static final String VALID = "valid";
    private EntityPermission entityPermission;
    private CustomWorkspace workspace;

    /**
     * Creates a new ValidationExecutor
     * @param adapter {@inheritDoc}
     */
    public ValidationExecutor(ActionAdapter adapter) {
        super(adapter, ACTION_TYPE);
        this.entityPermission = PermissionProvider.get("entity");
        this.workspace = WorkspaceFactory.getInstance();
    }

    @Override
    public Result execute(Map<String, String> params) throws Exception {
        String origin = params.get(PROJECT_NAME_ARG);
        String usecaseName = params.get(NAME_ARG);

        UsecaseEntity usecaseEntity = workspace.getEntityFromProject(usecaseName, UsecaseEntity.class, origin);
        Verification verification = usecaseEntity.verify();
        List<String> validationMessages = new ArrayList<>();
        validationMessages.addAll(Arrays.asList(verification.message().split("\\n")));

        while (validationMessages.contains(VALID)) {
            // If the use case is valid, "valid" is returned in the valid messages. Normally it but for
            // safety keep trying to remove it as long as a valid message is available.
            validationMessages.remove(VALID);
        }

        return new Result(new SuccessResult(), validationMessages);
    }

    @Override
    public boolean canExecute(Map<String, String> params) throws Exception {
        String origin = params.get(PROJECT_NAME_ARG);
        String auth = params.get(AUTH_ARG);

        return entityPermission.canGet(auth, origin);
    }
}
