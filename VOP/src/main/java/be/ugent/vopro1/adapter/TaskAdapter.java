package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.ResultSupplier;
import be.ugent.vopro1.adapter.result.ResultSupplierFactory;
import be.ugent.vopro1.adapter.result.mapper.ResultMapper;
import be.ugent.vopro1.adapter.result.mapper.ResultMapperFactory;
import be.ugent.vopro1.adapter.result.types.NoPermissionResult;
import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.interactor.project.ProjectPermission;
import be.ugent.vopro1.interactor.task.TaskInteractor;
import be.ugent.vopro1.interactor.task.TaskInteractorFactory;
import be.ugent.vopro1.model.Task;

import java.util.Map;
import java.util.Optional;

/**
 * Adapter for CRUD operations on {@link Task}
 * Uses a {@link TaskInteractor} to perform operations
 *
 * @see TaskInteractor
 */
public class TaskAdapter implements CommonAdapter {

    private final static String AUTH_ARG = "auth";
    private final static String PROJECT_ARG = "projectName";
    private final static String USECASE_ARG = "usecaseName";
    private final static String BLOB_ARG = "blob";

    private TaskInteractor interactor;
    private ResultSupplier resultSupplier;
    private ResultMapper resultMapper;
    private ProjectPermission permissionHandler;

    // A converter to convertToString json to objects
    private JsonConverter converter;

    /**
     * Creates a new TaskAdapter
     */
    public TaskAdapter() {
        interactor = TaskInteractorFactory.getInstance();
        resultSupplier = ResultSupplierFactory.getInstance();
        resultMapper = ResultMapperFactory.getInstance();
        permissionHandler = PermissionProvider.get("project");
        converter = JsonConverterFactory.getInstance();
    }

    @Override
    public Result get(Map<String, String> params) {
        Optional<String> projectNameString = Optional.ofNullable(params.get(PROJECT_ARG));
        Optional<String> usecaseNameString = Optional.ofNullable(params.get(USECASE_ARG));
        Optional<String> authString = Optional.ofNullable(params.get(AUTH_ARG));

        try {
            String projectName = projectNameString.get();
            String usecaseName = usecaseNameString.get();

            // TODO extra permission handler method
            if (!permissionHandler.canGet(authString.orElse(""), projectName)) {
                return new Result<>(new NoPermissionResult());
            }

            return resultSupplier.get(interactor::getTask, projectName, usecaseName);
        } catch (Exception e) {
            return resultMapper.mapException(e);
        }
    }

    @Override
    public Result getAll(Map<String, String> params) {
        throw new UnsupportedOperationException("'getAll' is not supported by this adapter");
    }

    @Override
    public Result add(Map<String, String> params) {
        Optional<String> projectName = Optional.ofNullable(params.get(PROJECT_ARG));
        Optional<String> usecaseName = Optional.ofNullable(params.get(USECASE_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> blob = Optional.ofNullable(params.get(BLOB_ARG));

        try {
            if (!permissionHandler.canEdit(auth.orElse(""), projectName.get())){
                return new Result<>(new NoPermissionResult());
            }

            Task task = converter.convert(Task.class, blob.get());
            return resultSupplier.get(interactor::createTask, projectName.get(), usecaseName.get(), task);
        } catch (Exception e) {
            return resultMapper.mapException(e);
        }
    }

    @Override
    public Result remove(Map<String, String> params) {
        Optional<String> projectNameString = Optional.ofNullable(params.get(PROJECT_ARG));
        Optional<String> usecaseNameString = Optional.ofNullable(params.get(USECASE_ARG));
        Optional<String> authString = Optional.ofNullable(params.get(AUTH_ARG));

        try {
            String projectName = projectNameString.get();
            String usecaseName = usecaseNameString.get();

            // TODO extra permission handler method
            if (!permissionHandler.canEdit(authString.orElse(""), projectName)) {
                return new Result<>(new NoPermissionResult());
            }

            return resultSupplier.consume(interactor::deleteTask, projectName, usecaseName);
        } catch (Exception e) {
            return resultMapper.mapException(e);
        }
    }

    @Override
    public Result edit(Map<String, String> params) {
        Optional<String> projectName = Optional.ofNullable(params.get(PROJECT_ARG));
        Optional<String> usecaseName = Optional.ofNullable(params.get(USECASE_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> blob = Optional.ofNullable(params.get(BLOB_ARG));

        try {
            if (!permissionHandler.canEdit(auth.orElse(""), projectName.get())){
                return new Result<>(new NoPermissionResult());
            }

            Task task = converter.convert(Task.class, blob.get());
            return resultSupplier.get(interactor::updateTask, projectName.get(), usecaseName.get(), task);
        } catch (Exception e) {
            return resultMapper.mapException(e);
        }
    }

    @Override
    public Class getType() {
        return Task.class;
    }
}
