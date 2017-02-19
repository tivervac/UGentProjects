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
import be.ugent.vopro1.interactor.team.TeamInteractor;
import be.ugent.vopro1.interactor.team.TeamInteractorFactory;
import be.ugent.vopro1.interactor.team.TeamPermission;
import be.ugent.vopro1.model.Team;

import java.util.Map;
import java.util.Optional;

/**
 * Translates calls from REST and passes it to Interactors, thus operating the
 * database for Team objects.
 * <p>
 * The class TeamAdapter communicates with Interactors, its main goal is to
 * accept complex String objects as defined in the REST API and pass the
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
public class TeamAdapter implements CommonAdapter {

    private static final String TEAM_ID_ARG = "teamId";
    private static final String AUTH_ARG = "auth";
    private static final String BLOB_ARG = "blob";
    private TeamPermission permissionHandler;
    private TeamInteractor interactor;
    private JsonConverter converter;
    private ResultSupplier supplier;
    private ResultMapper resultMapper;

    /**
     * Constructs a TeamAdapter
     *
     * @see TeamInteractorFactory
     * @see JsonConverterFactory
     * @see ResultSupplierFactory
     */
    public TeamAdapter() {
        interactor = TeamInteractorFactory.getInstance();
        converter = JsonConverterFactory.getInstance();
        supplier = ResultSupplierFactory.getInstance();
        permissionHandler = PermissionProvider.get("team");
        resultMapper = ResultMapperFactory.getInstance();
    }

    /**
     * Retrieves a Team from the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see TeamInteractor
     * @see Team
     * @see Result
     */
    @Override
    public Result get(Map<String, String> params) {
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> teamIdString = Optional.ofNullable(params.get(TEAM_ID_ARG));

        try {
            int teamId = Integer.valueOf(teamIdString.get());

            if (!permissionHandler.canGet(auth.orElse(""), teamId)) {
                return new Result(new NoPermissionResult());
            }

            return supplier.get(interactor::getTeam, teamId);
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Retrieves all Teams from the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see TeamInteractor
     * @see Team
     * @see Result
     */
    @Override
    public Result getAll(Map<String, String> params) {
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));

        if (!permissionHandler.canGetAll(auth.orElse(""))) {
            return new Result(new NoPermissionResult());
        }

        try {
            return supplier.supply(interactor::getAllTeams);
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Adds a Team to the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see TeamInteractor
     * @see Team
     * @see Result
     */
    @Override
    public Result add(Map<String, String> params) {
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> blob = Optional.ofNullable(params.get(BLOB_ARG));

        try {
            if (!permissionHandler.canGetAll(auth.orElse(""))) {
                return new Result(new NoPermissionResult());
            }

            Team entity = converter.convert(Team.class, blob.get());
            return supplier.get(interactor::addTeam, entity);
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Removes a Team from the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see TeamInteractor
     * @see Team
     * @see Result
     */
    @Override
    public Result remove(Map<String, String> params) {
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> teamIdString = Optional.ofNullable(params.get(TEAM_ID_ARG));

        try {
            int teamId = Integer.valueOf(teamIdString.get());
            if (!permissionHandler.canRemove(auth.orElse(""), teamId)) {
                return new Result(new NoPermissionResult());
            }

            return supplier.consume(interactor::removeTeam, teamId);
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Edits a Team from the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see TeamInteractor
     * @see Team
     * @see Result
     */
    @Override
    public Result edit(Map<String, String> params) {
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));
        Optional<String> teamIdString = Optional.ofNullable(params.get(TEAM_ID_ARG));
        Optional<String> blob = Optional.ofNullable(params.get(BLOB_ARG));

        try {
            int teamId = Integer.valueOf(teamIdString.get());
            if (!permissionHandler.canEdit(auth.orElse(""), teamId)) {
                return new Result(new NoPermissionResult());
            }

            Team entity = converter.convert(Team.class, blob.get());
            return supplier.get(interactor::editTeam, teamId, entity);
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * {@inheritDoc}
     * <p>
     * The TeamAdapter overrides this method for retrieval of the identifier based on the
     * unique team name.
     *
     * @param teamName Name of the team to retrieve identifier for
     * @return The unique identifier, if an entity with the unique name is found
     */
    @Override
    public Optional<Integer> getId(String teamName) {
        if (teamName == null) {
            return Optional.empty();
        } else {
            return interactor.getId(teamName);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return Type of object that this adapter can be used for
     */
    @Override
    public Class getType() {
        return Team.class;
    }
}