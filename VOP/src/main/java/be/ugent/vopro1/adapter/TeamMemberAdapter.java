package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.ResultSupplier;
import be.ugent.vopro1.adapter.result.ResultSupplierFactory;
import be.ugent.vopro1.adapter.result.mapper.ResultMapper;
import be.ugent.vopro1.adapter.result.mapper.ResultMapperFactory;
import be.ugent.vopro1.adapter.result.types.NoPermissionResult;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.interactor.team.TeamInteractor;
import be.ugent.vopro1.interactor.team.TeamInteractorFactory;
import be.ugent.vopro1.interactor.team.TeamPermission;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.model.stub.TeamMemberEntity;

import java.util.Map;
import java.util.Optional;

/**
 * Translates calls from REST and passes it to Interactors, thus operating the
 * database for Team objects. Specifically, it is responsible for handling
 * actions related to team members.
 * <p>
 * The class TeamMemberAdapter communicates with Interactors, its main goal is
 * to accept complex String objects as defined in the REST API and pass the
 * appropriate objects to the correct Interactor. Through these interactors the
 * database will be manipulated for instance by storing, editing or removing
 * objects from it. The TeamAdapter class is responsible for directing these
 * objects to the right Interactor and calling the operation suited for the
 * required database operation. This class mainly handles Team objects.
 *
 * @see AdapterManager
 * @see TeamInteractor
 * @see Team
 * @see Result
 */
public class TeamMemberAdapter implements CommonAdapter {

    private static final String TEAM_ID_ARG = "teamId";
    private static final String USER_ID_ARG = "userId";
    private static final String AUTH_ARG = "auth";
    private static final String ANALYST_ONLY_ARG = "analystOnly";
    private TeamPermission permissionHandler;
    private TeamInteractor interactor;
    private ResultSupplier supplier;
    private ResultMapper resultMapper;

    /**
     * Constructs a TeamMemberAdapter
     *
     * @see TeamInteractorFactory
     * @see ResultSupplierFactory
     */
    public TeamMemberAdapter() {
        interactor = TeamInteractorFactory.getInstance();
        supplier = ResultSupplierFactory.getInstance();
        permissionHandler = PermissionProvider.get("team");
        resultMapper = ResultMapperFactory.getInstance();
    }

    /**
     * "get" is not supported in the TeamMemberAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    @Override
    public Result get(Map<String, String> params) {
        throw new UnsupportedOperationException("'get' is not supported by this adapter");
    }

    /**
     * Retrieves all team members from the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see TeamInteractor
     * @see Team
     * @see User
     * @see Result
     */
    @Override
    public Result getAll(Map<String, String> params) {
        Optional<String> idString = Optional.ofNullable(params.get(TEAM_ID_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));

        try {
            int id = Integer.valueOf(idString.get());

            if (!permissionHandler.canGetMembers(auth.orElse(""), id)) {
                return new Result(new NoPermissionResult());
            }

            return supplier.get(interactor::getTeamMembers, id, Boolean.valueOf(params.get(ANALYST_ONLY_ARG)));
        } catch (Exception ex){
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Adds a member to a Team through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see TeamInteractor
     * @see Team
     * @see User
     * @see Result
     */
    @Override
    public Result add(Map<String, String> params) {
        Optional<String> teamIdString = Optional.ofNullable(params.get(TEAM_ID_ARG));
        Optional<String> userIdString = Optional.ofNullable(params.get(USER_ID_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));

        try {
            int teamId = Integer.valueOf(teamIdString.get());
            int userId = Integer.valueOf(userIdString.get());

            if (!permissionHandler.canAddMember(auth.orElse(""), teamId)) {
                return new Result(new NoPermissionResult());
            }

            return supplier.consume(interactor::addTeamMember, teamId, userId);
        } catch (Exception ex){
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Removes a member from a Team through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see TeamInteractor
     * @see Team
     * @see User
     * @see Result
     */
    @Override
    public Result remove(Map<String, String> params) {
        Optional<String> teamIdString = Optional.ofNullable(params.get(TEAM_ID_ARG));
        Optional<String> userIdString = Optional.ofNullable(params.get(USER_ID_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));

        try {
            int teamId = Integer.valueOf(teamIdString.get());
            int userId = Integer.valueOf(userIdString.get());

            if (!permissionHandler.canRemoveMember(auth.orElse(""), teamId)) {
                return new Result(new NoPermissionResult());
            }

            return supplier.consume(interactor::removeTeamMember, teamId, userId);
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * "edit" is not supported in the TeamMemberAdapter
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
        return TeamMemberEntity.class;
    }
}
