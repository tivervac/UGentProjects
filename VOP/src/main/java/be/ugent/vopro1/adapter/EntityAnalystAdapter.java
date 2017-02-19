package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.ResultSupplier;
import be.ugent.vopro1.adapter.result.ResultSupplierFactory;
import be.ugent.vopro1.adapter.result.mapper.ResultMapper;
import be.ugent.vopro1.adapter.result.mapper.ResultMapperFactory;
import be.ugent.vopro1.adapter.result.types.NoPermissionResult;
import be.ugent.vopro1.interactor.entity.EntityInteractor;
import be.ugent.vopro1.interactor.entity.EntityPermission;
import be.ugent.vopro1.interactor.entity.InteractorFactory;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.model.stub.EntityAnalystEntity;
import be.ugent.vopro1.persistence.exception.DBInvalidQueryException;

import java.util.Map;
import java.util.Optional;

/**
 * Translates calls from REST and passes it to Interactors, thus operating the
 * database for ProjectEntity objects. Specifically, it is responsible for
 * handling actions related to project analysts.
 * <p>
 * The class EntityAnalystAdapter communicates with Interactors, its main goal
 * is to accept complex String objects as defined in the REST API and pass the
 * appropriate objects to the correct Interactor. Through these interactors the
 * database will be manipulated for instance by storing, editing or removing
 * objects from it. The EntityAnalystAdapter class is responsible for directing these
 * objects to the right Interactor and calling the operation suited for the
 * required database operation. This class mainly handles EntityAnalyst objects.
 *
 * @see AdapterManager
 * @see EntityInteractor
 * @see EntityAnalystEntity
 * @see Result
 */
public class EntityAnalystAdapter implements CommonAdapter {

    private static final String AUTH_ARG = "auth";
    private static final String PROJECT_NAME_ARG = "projectName";
    private static final String NAME_ARG = "name";
    private static final String USER_ID_ARG = "userId";
    private EntityPermission permissionHandler;
    private EntityInteractor interactor;
    private ResultSupplier supplier;
    private ResultMapper resultMapper;

    /**
     * Constructs an EntityAnalystAdapter.
     *
     * @see InteractorFactory
     * @see ResultSupplierFactory
     */
    public EntityAnalystAdapter() {
        interactor = InteractorFactory.getInstance();
        supplier = ResultSupplierFactory.getInstance();
        permissionHandler = PermissionProvider.get("entity");
        resultMapper = ResultMapperFactory.getInstance();
    }

    /**
     * "get" is not supported in the EntityAnalystAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    @Override
    public Result get(Map<String, String> params) {
        return resultMapper.mapException(new UnsupportedOperationException("'get' is not supported by this adapter"));
    }

    /**
     * Retrieves all entity analysts from the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see EntityInteractor
     * @see EntityInteractor#getAnalysts(String, String)
     * @see be.ugent.vopro1.model.EntityProject
     * @see be.ugent.vopro1.model.User
     * @see Result
     */
    @Override
    public Result getAll(Map<String, String> params) {
        Optional<String> projectName = Optional.ofNullable(params.get(PROJECT_NAME_ARG));
        Optional<String> entityName = Optional.ofNullable(params.get(NAME_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));

        try {
            if (!permissionHandler.canGetAnalysts(auth.orElse(""), projectName.orElseThrow(DBInvalidQueryException::new))) {
                return new Result(new NoPermissionResult());
            }

            return supplier.get(interactor::getAnalysts, projectName.get(), entityName.orElseThrow(DBInvalidQueryException::new));
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Adds an analyst to an entity through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see EntityInteractor
     * @see EntityInteractor#addAnalyst(String, String, int)
     * @see be.ugent.vopro1.model.EntityProject
     * @see be.ugent.vopro1.model.User
     * @see Result
     */
    @Override
    public Result add(Map<String, String> params) {
        Optional<String> projectName = Optional.ofNullable(params.get(PROJECT_NAME_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> entityName = Optional.ofNullable(params.get(NAME_ARG));
        Optional<String> userIdString = Optional.ofNullable(params.get(USER_ID_ARG));

        try {
            int userId = Integer.valueOf(userIdString.orElseThrow(DBInvalidQueryException::new));
            String name = entityName.orElseThrow(DBInvalidQueryException::new);

            if (!permissionHandler.canAddAnalyst(auth.orElse(""), projectName.get())) {
                return new Result(new NoPermissionResult());
            }

            return supplier.consume(interactor::addAnalyst, projectName.get(), name, userId);
        } catch (Exception ex){
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Removes an analyst from an entity through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see EntityInteractor
     * @see EntityInteractor#removeAnalyst(String, String, int)
     * @see be.ugent.vopro1.model.EntityProject
     * @see be.ugent.vopro1.model.User
     * @see Result
     */
    @Override
    public Result remove(Map<String, String> params) {
        Optional<String> projectName = Optional.ofNullable(params.get(PROJECT_NAME_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> entityName = Optional.ofNullable(params.get(NAME_ARG));
        Optional<String> userIdString = Optional.ofNullable(params.get(USER_ID_ARG));

        try {
            int userId = Integer.valueOf(userIdString.orElseThrow(DBInvalidQueryException::new));
            String name = entityName.orElseThrow(DBInvalidQueryException::new);

            if (!permissionHandler.canRemoveAnalyst(auth.orElse(""), projectName.get())) {
                return new Result(new NoPermissionResult());
            }

            return supplier.consume(interactor::removeAnalyst, projectName.get(), name, userId);
        } catch (Exception ex){
            return resultMapper.mapException(ex);
        }
    }

    /**
     * "edit" is not supported in the EntityAnalystAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    @Override
    public Result edit(Map<String, String> params) {
        return resultMapper.mapException(new UnsupportedOperationException("'edit' is not supported by this adapter"));
    }

    /**
     * {@inheritDoc}
     *
     * @return Type of object that this adapter can be used for
     */
    @Override
    public Class getType() {
        return EntityAnalystEntity.class;
    }

}
