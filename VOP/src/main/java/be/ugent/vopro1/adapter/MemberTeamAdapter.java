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
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.model.stub.MemberTeamEntity;

import java.util.Map;
import java.util.Optional;

/**
 * Translates calls from REST and passes it to Interactors, thus operating the
 * database for MemberTeamEntity objects.
 * <p>
 * The class MemberTeamAdapter communicates with UserInteractor, its goal is to
 * retrieve all the teams linked to a user.
 *
 * @see AdapterManager
 * @see UserInteractor
 * @see MemberTeamEntity
 * @see Result
 */
public class MemberTeamAdapter implements CommonAdapter {


    private UserInteractor interactor;
    private ResultSupplier supplier;
    private ResultMapper resultMapper;
    private UserPermission permissionHandler;

    /**
     * Constructs a MemberTeamAdapter
     *
     * @see UserInteractorFactory
     * @see ResultSupplierFactory
     */
    public MemberTeamAdapter() {
        interactor = UserInteractorFactory.getInstance();
        supplier = ResultSupplierFactory.getInstance();
        resultMapper = ResultMapperFactory.getInstance();
        permissionHandler = PermissionProvider.get("user");
    }

    /**
     * "get" is not supported in the MemberTeamAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    @Override
    public Result get(Map<String, String> params) {
        throw new UnsupportedOperationException("'get' is not supported by this adapter");
    }

    /**
     * Retrieves all Team objects, to which user with userId 'id' belongs,
     * from the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see UserAdapter
     * @see Result
     * @see Team
     * @see User
     */
    @Override
    public Result getAll(Map<String, String> params) {
        Optional<String> authHeader = Optional.ofNullable(params.get("auth"));
        Optional<String> idString = Optional.ofNullable(params.get("id"));

        try {
            int id = Integer.valueOf(idString.get());
            if (!permissionHandler.canGetTeams(authHeader.orElse(""), id)) {
                return new Result(new NoPermissionResult());
            }

            return supplier.get(interactor::getTeams, id, Boolean.valueOf(params.get("analystOnly")));
        } catch (Exception ex){
            return resultMapper.mapException(ex);
        }
    }

    /**
     * "add" is not supported in the MemberTeamAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    @Override
    public Result add(Map<String, String> params) {
        throw new UnsupportedOperationException("'add' is not supported by this adapter");
    }

    /**
     * "remove" is not supported in the MemberTeamAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    @Override
    public Result remove(Map<String, String> params) {
        throw new UnsupportedOperationException("'remove' is not supported by this adapter");
    }

    /**
     * "edit" is not supported in the MemberTeamAdapter
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
        return MemberTeamEntity.class;
    }
}
