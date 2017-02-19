package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.ResultSupplier;
import be.ugent.vopro1.adapter.result.ResultSupplierFactory;
import be.ugent.vopro1.adapter.result.mapper.ResultMapper;
import be.ugent.vopro1.adapter.result.mapper.ResultMapperFactory;
import be.ugent.vopro1.adapter.result.types.NoPermissionResult;
import be.ugent.vopro1.adapter.result.types.ConditionFailedResult;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.interactor.project.ProjectInteractor;
import be.ugent.vopro1.interactor.project.ProjectInteractorFactory;
import be.ugent.vopro1.interactor.team.TeamInteractor;
import be.ugent.vopro1.interactor.team.TeamInteractorFactory;
import be.ugent.vopro1.interactor.team.TeamPermission;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.stub.TeamProjectEntity;
import be.ugent.vopro1.util.LocalConstants;

import java.util.Map;
import java.util.Optional;

/**
 * Translates calls from REST and passes it to Interactors, thus operating the
 * database for Team objects. Specifically, it is responsible for handling
 * actions related to team members.
 * <p>
 * The class TeamProjectAdapter communicates with Interactors, its main goal is
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
public class TeamProjectAdapter implements CommonAdapter {

    private static final String TEAM_ID_ARG = "teamId";
    private static final String PROJECT_NAME_ARG = "projectName";
    private static final String AUTH_ARG = "auth";
    private TeamPermission permissionHandler;
    private TeamInteractor interactor;
    private ProjectInteractor projectInteractor;
    private ResultSupplier supplier;
    private ResultMapper resultMapper;

    /**
     * Constructs a TeamProjectAdapter
     *
     * @see TeamInteractorFactory
     * @see ResultSupplierFactory
     */
    public TeamProjectAdapter() {
        interactor = TeamInteractorFactory.getInstance();
        projectInteractor = ProjectInteractorFactory.getInstance();
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
        return resultMapper.mapException(new UnsupportedOperationException("'get' is not supported by this adapter"));
    }

    /**
     * Retrieves all of this team's projects from the database through an
     * Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see TeamInteractor
     * @see Team
     * @see be.ugent.vopro1.model.EntityProject
     * @see Result
     */
    @Override
    public Result getAll(Map<String, String> params) {
        Optional<String> teamIdString = Optional.ofNullable(params.get(TEAM_ID_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));

        try {
            int teamId = Integer.valueOf(teamIdString.get());

            if (!permissionHandler.canGetProjects(auth.orElse(""), teamId)) {
                return new Result(new NoPermissionResult());
            }

            return supplier.get(interactor::getProjects, teamId);
        } catch (Exception ex){
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Adds a project to a Team through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see TeamInteractor
     * @see Team
     * @see be.ugent.vopro1.model.EntityProject
     * @see Result
     */
    @Override
    public Result add(Map<String, String> params) {
        Optional<String> teamIdString = Optional.ofNullable(params.get(TEAM_ID_ARG));
        Optional<String> projectName = Optional.ofNullable(params.get(PROJECT_NAME_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));

        try {
            int teamId = Integer.valueOf(teamIdString.get());

            if (!permissionHandler.canAddProject(auth.orElse(""), teamId, projectName.get())) {
                return new Result(new NoPermissionResult());
            }

            if (projectInteractor.getTeams(projectName.get()).size() >= LocalConstants.MAX_TEAMS_PER_PROJECT) {
                return new Result(new ConditionFailedResult("Project cannot have more than " +
                        LocalConstants.MAX_TEAMS_PER_PROJECT + " teams assigned to it"));
            }

            return supplier.consume(interactor::addTeamProject, teamId, projectName.get());
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Removes a project from a Team through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see TeamInteractor
     * @see Team
     * @see be.ugent.vopro1.model.EntityProject
     * @see Result
     */
    @Override
    public Result remove(Map<String, String> params) {
        Optional<String> teamIdString = Optional.ofNullable(params.get(TEAM_ID_ARG));
        Optional<String> projectName = Optional.ofNullable(params.get(PROJECT_NAME_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));

        try {
            int teamId = Integer.valueOf(teamIdString.get());

            if (!permissionHandler.canRemoveProject(auth.orElse(""), teamId, projectName.get())) {
                return new Result(new NoPermissionResult());
            }

            return supplier.consume(interactor::removeTeamProject, teamId, projectName.get());
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
        return resultMapper.mapException(new UnsupportedOperationException("'edit' is not supported by this adapter"));
    }

    /**
     * {@inheritDoc}
     *
     * @return Type of object that this adapter can be used for
     */
    @Override
    public Class getType() {
        return TeamProjectEntity.class;
    }

}
