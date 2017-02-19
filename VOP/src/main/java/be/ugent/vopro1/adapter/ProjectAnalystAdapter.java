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
import be.ugent.vopro1.interactor.project.ProjectInteractor;
import be.ugent.vopro1.interactor.project.ProjectInteractorFactory;
import be.ugent.vopro1.interactor.project.ProjectPermission;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.model.stub.ProjectAnalystEntity;
import be.ugent.vopro1.persistence.exception.DBInvalidQueryException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Translates calls from REST and passes it to Interactors, thus operating the
 * database for ProjectEntity objects. Specifically, it is responsible for
 * handling actions related to project analysts.
 * <p>
 * The class ProjectAnalystAdapter communicates with Interactors, its main goal
 * is to accept complex String objects as defined in the REST API and pass the
 * appropriate objects to the correct Interactor. Through these interactors the
 * database will be manipulated for instance by storing, editing or removing
 * objects from it. The ProjectAdapter class is responsible for directing these
 * objects to the right Interactor and calling the operation suited for the
 * required database operation. This class mainly handles Project objects.
 *
 * @see AdapterManager
 * @see ProjectInteractor
 * @see ProjectAnalystEntity
 * @see Result
 */
public class ProjectAnalystAdapter implements CommonAdapter {

    private static final String AUTH_ARG = "auth";
    private static final String PROJECT_NAME_ARG = "projectName";
    private static final String USER_ID_ARG = "userId";
    private static final String SPEC = "specification";
    private static final String WORK_ARG = "work";
    private ProjectPermission permissionHandler;
    private ProjectInteractor interactor;
    private ResultSupplier supplier;
    private ResultMapper resultMapper;

    /**
     * Constructs a ProjectAnalystAdapter.
     *
     * @see ProjectInteractorFactory
     * @see ResultSupplierFactory
     */
    public ProjectAnalystAdapter() {
        interactor = ProjectInteractorFactory.getInstance();
        supplier = ResultSupplierFactory.getInstance();
        permissionHandler = PermissionProvider.get("project");
        resultMapper = ResultMapperFactory.getInstance();
    }

    /**
     * "get" is not supported in the ProjectAnalystAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    @Override
    public Result get(Map<String, String> params) {
        return resultMapper.mapException(new UnsupportedOperationException("'get' is not supported by this adapter"));
    }

    /**
     * Retrieves all project analysts from the database through an Interactor.
     * <p>
     * If 'spec' contains "eligible" then it retrieves all users that are
     * eligible to become a projectAnalyst.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see ProjectInteractor
     * @see be.ugent.vopro1.model.EntityProject
     * @see User
     * @see Result
     */
    @Override
    public Result getAll(Map<String, String> params) {
        Optional<String> projectName = Optional.ofNullable(params.get(PROJECT_NAME_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> spec = Optional.ofNullable(params.get(SPEC));

        try {
            if (!permissionHandler.canGetAnalysts(auth.orElse(""), projectName.orElseThrow(DBInvalidQueryException::new))) {
                return new Result<>(new NoPermissionResult());
            }

            if (spec.isPresent() && spec.get().equals("eligible")) {
                return supplier.get(interactor::getEligibleAnalysts, projectName.get());
            } else {
                return supplier.get(interactor::getAnalysts, projectName.get());
            }
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Adds an analyst to a Project through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see ProjectInteractor
     * @see be.ugent.vopro1.model.EntityProject
     * @see User
     * @see Result
     */
    @Override
    public Result add(Map<String, String> params) {
        Optional<String> projectName = Optional.ofNullable(params.get(PROJECT_NAME_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> userIdString = Optional.ofNullable(params.get(USER_ID_ARG));
        Optional<String> workString = Optional.ofNullable(params.get(WORK_ARG));

        try {
            int userId = Integer.valueOf(userIdString.orElseThrow(DBInvalidQueryException::new));
            if (!permissionHandler.canAddAnalyst(auth.orElse(""), projectName.get())) {
                return new Result<>(new NoPermissionResult());
            }

            Map<String, Object> map = JsonConverterFactory.getInstance().convert(HashMap.class, workString.get());
            long work = (long) (int) map.get(WORK_ARG);

            return supplier.consume(interactor::addAnalyst, projectName.get(), userId, work);
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Removes an analyst from a Project through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see ProjectInteractor
     * @see be.ugent.vopro1.model.EntityProject
     * @see User
     * @see Result
     */
    @Override
    public Result remove(Map<String, String> params) {
        Optional<String> projectName = Optional.ofNullable(params.get(PROJECT_NAME_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> userIdString = Optional.ofNullable(params.get(USER_ID_ARG));

        try {
            int userId = Integer.valueOf(userIdString.orElseThrow(DBInvalidQueryException::new));
            if (!permissionHandler.canRemoveAnalyst(auth.orElse(""), projectName.get())) {
                return new Result<>(new NoPermissionResult());
            }

            return supplier.consume(interactor::removeAnalyst, projectName.get(), userId);
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Edits the work an analyst can put into the project.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     */
    @Override
    public Result edit(Map<String, String> params) {
        Optional<String> projectName = Optional.ofNullable(params.get(PROJECT_NAME_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> userIdString = Optional.ofNullable(params.get(USER_ID_ARG));
        Optional<String> workString = Optional.ofNullable(params.get(WORK_ARG));

        try {
            int userId = Integer.valueOf(userIdString.orElseThrow(DBInvalidQueryException::new));
            if (!permissionHandler.canEditAnalyst(auth.orElse(""), projectName.get())) {
                return new Result<>(new NoPermissionResult());
            }

            Map<String, Object> map = JsonConverterFactory.getInstance().convert(HashMap.class, workString.get());
            long work = (long) (int) map.get(WORK_ARG);

            return supplier.consume(interactor::editAnalyst, projectName.get(), userId, work);
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return Type of object that this adapter can be used for
     */
    @Override
    public Class getType() {
        return ProjectAnalystEntity.class;
    }

}
