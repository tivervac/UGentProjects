package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.ResultSupplier;
import be.ugent.vopro1.adapter.result.ResultSupplierFactory;
import be.ugent.vopro1.adapter.result.mapper.ResultMapper;
import be.ugent.vopro1.adapter.result.mapper.ResultMapperFactory;
import be.ugent.vopro1.adapter.result.types.NoPermissionResult;
import be.ugent.vopro1.converter.json.JsonConverter;
import be.ugent.vopro1.converter.json.JsonConverterFactory;
import be.ugent.vopro1.interactor.entity.EntityInteractor;
import be.ugent.vopro1.interactor.entity.InteractorFactory;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.interactor.project.ProjectInteractor;
import be.ugent.vopro1.interactor.project.ProjectInteractorFactory;
import be.ugent.vopro1.interactor.project.ProjectPermission;
import be.ugent.vopro1.model.EntityProject;
import org.aikodi.lang.funky.executors.Actor;

import java.util.Map;
import java.util.Optional;

/**
 * Translates calls from REST and passes it to Interactors, thus operating the
 * database for EntityProject objects.
 * <p>
 * The class ProjectAdapter communicates with Interactors, its main goal is to
 * accept complex String objects as defined in the REST API and pass the
 * appropriate objects to the correct Interactor. Through these interactors the
 * database will be manipulated for instance by storing, editing or removing
 * objects from it. The ProjectAdapter class is responsible for directing these
 * objects to the right Interactor and calling the operation suited for the
 * required database operation. This class mainly handles Project objects.
 *
 * @see AdapterManager
 * @see ProjectInteractor
 * @see EntityProject
 * @see Result
 */
public class ProjectAdapter implements CommonAdapter {

    private static final String PROJECT_NAME_ARG = "projectName";
    private static final String NAME_ARG = "name";
    private static final String AUTH_ARG = "auth";
    private static final String BLOB_ARG = "blob";
    private ProjectPermission permissionHandler;
    private ProjectInteractor interactor;
    private ResultSupplier resultSupplier;
    private ResultMapper resultMapper;
    private JsonConverter converter;

    /**
     * Constructs a ProjectAdapter.
     *
     * @see ProjectInteractorFactory
     * @see ResultSupplierFactory
     */
    public ProjectAdapter() {
        interactor = ProjectInteractorFactory.getInstance();
        resultSupplier = ResultSupplierFactory.getInstance();
        resultMapper = ResultMapperFactory.getInstance();
        permissionHandler = PermissionProvider.get("project");
        converter = JsonConverterFactory.getInstance();
    }

    /**
     * Gets a Project from the database through an Interactor.
     *
     * @param args map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see ProjectInteractor
     * @see EntityProject
     * @see Result
     */
    @Override
    public Result get(Map<String, String> args) {
        // get project name
        Optional<String> projectName = Optional.ofNullable(args.get(PROJECT_NAME_ARG));
        Optional<String> auth = Optional.ofNullable(args.get(AUTH_ARG));

        try {
            if (!permissionHandler.canGet(auth.orElse(""), projectName.get())) {
                return new Result(new NoPermissionResult());
            }

            EntityProject project = new EntityProject(projectName.get());

            return resultSupplier.get(interactor::getProject, project);
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Gets all Projects from the database through an Interactor.
     *
     * @param args map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see ProjectInteractor
     * @see EntityProject
     * @see Result
     */
    @Override
    public Result getAll(Map<String, String> args) {
        Optional<String> auth = Optional.ofNullable(args.get(AUTH_ARG));
        Optional<String> name = Optional.ofNullable(args.get(NAME_ARG));
        Optional<String> assignTeamFilter = Optional.ofNullable(args.get("team_assignable"));

        try {
            if (!permissionHandler.canSearch(auth.orElse(""))) {
                return new Result(new NoPermissionResult());
            }

            if (name.isPresent()) {
                // Search for projects with a similar name
                return resultSupplier.get(interactor::findProjects, args.get("name"));
            } else {
                // When no name is given, get all projects
                return resultSupplier.get(interactor::getAllProjects, Boolean.valueOf(assignTeamFilter.orElse("false")));
            }
        } catch (Exception ex){
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Adds a Project to the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see ProjectInteractor
     * @see EntityProject
     * @see Result
     */
    @Override
    public Result add(Map<String, String> params) {
        Optional<String> blob = Optional.ofNullable(params.get(BLOB_ARG));
        Optional<String> auth = Optional.ofNullable(params.get(AUTH_ARG));

        try {
            if (!permissionHandler.canAdd(auth.orElse(""))) {
                return new Result(new NoPermissionResult());
            }
            
            EntityProject project = converter.convert(EntityProject.class, blob.get());

            Result result = resultSupplier.get(interactor::addProject, project);

            //TODO Bit of a hack
            if (result.isSuccessful()){
                Actor system = new Actor("System");
                EntityInteractor<Actor> actorInteractor = InteractorFactory.getInstance();
                resultSupplier.get(actorInteractor::add, project.getName(), system);
            }

            return result;
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Removes a Project from the database through an Interactor.
     *
     * @param args map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see ProjectInteractor
     * @see EntityProject
     * @see Result
     */
    @Override
    public Result remove(Map<String, String> args) {
        Optional<String> name = Optional.ofNullable(args.get(PROJECT_NAME_ARG));
        Optional<String> auth = Optional.ofNullable(args.get(AUTH_ARG));

        try {
            if (!permissionHandler.canRemove(args.get(AUTH_ARG), auth.orElse(""))) {
                return new Result(new NoPermissionResult());
            }

            EntityProject project = new EntityProject(name.get());
            return resultSupplier.get(interactor::removeProject, project);
        } catch (Exception ex) {
            return resultMapper.mapException(ex);
        }
    }

    /**
     * Edits a Project from the database through an Interactor.
     *
     * @param args map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see ProjectInteractor
     * @see EntityProject
     * @see Result
     */
    @Override
    public Result edit(Map<String, String> args) {
        Optional<String> old = Optional.ofNullable(args.get(PROJECT_NAME_ARG));
        Optional<String> blob = Optional.ofNullable(args.get(BLOB_ARG));
        Optional<String> auth = Optional.ofNullable(args.get(AUTH_ARG));

        try {
            if (!permissionHandler.canEdit(auth.orElse(""), old.get())) {
                return new Result(new NoPermissionResult());
            }

            EntityProject oldProject = new EntityProject(old.get());
            EntityProject project = converter.convert(EntityProject.class, blob.get());
            
            return resultSupplier.get(interactor::editProject, oldProject, project);
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
        return EntityProject.class;
    }
}
