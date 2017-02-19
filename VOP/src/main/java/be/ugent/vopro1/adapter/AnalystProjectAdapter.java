package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.ResultSupplier;
import be.ugent.vopro1.adapter.result.ResultSupplierFactory;
import be.ugent.vopro1.adapter.result.mapper.ResultMapper;
import be.ugent.vopro1.adapter.result.mapper.ResultMapperFactory;
import be.ugent.vopro1.adapter.result.types.NoPermissionResult;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.interactor.user.UserInteractor;
import be.ugent.vopro1.interactor.user.UserInteractorFactory;
import be.ugent.vopro1.interactor.user.UserPermission;
import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.model.stub.AnalystProjectEntity;

import java.util.Map;
import java.util.Optional;

/**
 * Translates calls from REST and passes it to Interactors, thus operating the
 * database for AnalystProjectEntity objects.
 * <p>
 * The class AnalystProjectAdapter communicates with UserInteractor, its goal is to
 * retrieve all the projects linked to an analyst.
 *
 * @see AdapterManager
 * @see UserInteractor
 * @see AnalystProjectEntity
 * @see Result
 */
public class AnalystProjectAdapter implements CommonAdapter {


    private UserInteractor interactor;
    private ResultSupplier resultSupplier;
    private ResultMapper resultMapper;
    private UserPermission permissionHandler;

    /**
     * Constructs a AnalystProjectAdapter
     *
     * @see UserInteractorFactory
     * @see ResultSupplierFactory
     */
    public AnalystProjectAdapter() {
        interactor = UserInteractorFactory.getInstance();
        resultSupplier = ResultSupplierFactory.getInstance();
        resultMapper = ResultMapperFactory.getInstance();
        permissionHandler = PermissionProvider.get("user");
    }

    /**
     * "get" is not supported in the AnalystProjectAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    @Override
    public Result get(Map<String, String> params) {
        throw new UnsupportedOperationException("'get' is not supported by this adapter");
    }

    /**
     * Retrieves all ProjectEntity objects, to which user with userId 'id' belongs,
     * from the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see UserAdapter
     * @see Result
     * @see EntityProject
     * @see User
     */
    @Override
    public Result getAll(Map<String, String> params) {
        Optional<String> idString = Optional.ofNullable(params.get("id"));

        try {
            int id = Integer.valueOf(idString.get());

            if (!permissionHandler.canGetAnalystList(params.get("auth"), id)) {
                return new Result<>(new NoPermissionResult());
            }

            return resultSupplier.get(interactor::getAnalystProjects, id);
        } catch (Exception e) {
            return resultMapper.mapException(e);
        }
    }

    /**
     * "add" is not supported in the AnalystProjectAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    @Override
    public Result add(Map<String, String> params) {
        throw new UnsupportedOperationException("'add' is not supported by this adapter");
    }

    /**
     * "remove" is not supported in the AnalystProjectAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    @Override
    public Result remove(Map<String, String> params) {
        throw new UnsupportedOperationException("'remove' is not supported by this adapter");
    }

    /**
     * "edit" is not supported in the AnalystProjectAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    @Override
    public Result edit(Map<String, String> params) {
        throw new UnsupportedOperationException("'edit' is not supported by this adapter");
    }

    /**
     * {@inheritDoc}
     *
     * @return Type of object that this adapter can be used for
     */
    @Override
    public Class getType() {
        return AnalystProjectEntity.class;
    }
}
