package be.ugent.vopro1.adapter;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.ResultSupplier;
import be.ugent.vopro1.adapter.result.ResultSupplierFactory;
import be.ugent.vopro1.adapter.result.mapper.ResultMapper;
import be.ugent.vopro1.adapter.result.mapper.ResultMapperFactory;
import be.ugent.vopro1.adapter.result.types.NoPermissionResult;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.interactor.project.ProjectPermission;
import be.ugent.vopro1.interactor.schedule.ScheduleInteractor;
import be.ugent.vopro1.interactor.schedule.ScheduleInteractorFactory;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.scheduling.Schedule;

import java.util.Map;
import java.util.Optional;


/**
 * Translates calls from REST and passes it to Interactors, thus operating the
 * database for Schedule objects.
 * <p>
 * The class ScheduleAdapter communicates with ScheduleInteractor, its goal is to
 * retrieve the Schedule of a project.
 *
 * @see AdapterManager
 * @see ScheduleInteractor
 * @see Schedule
 * @see CommonAdapter
 * @see Result
 */
public class ScheduleAdapter implements CommonAdapter {

    private ScheduleInteractor interactor;
    private ResultSupplier resultSupplier;
    private ResultMapper resultMapper;
    private ProjectPermission permissionHandler;

    /**
     * Constructs a ScheduleAdapter
     *
     * @see ScheduleInteractorFactory
     * @see ResultSupplierFactory
     */
    public ScheduleAdapter() {
        interactor = ScheduleInteractorFactory.getInstance();
        resultSupplier = ResultSupplierFactory.getInstance();
        resultMapper = ResultMapperFactory.getInstance();
        permissionHandler = PermissionProvider.get("project");
    }

    /**
     * Retrieves the Schedule object, for the project 'projectName',
     * from the database through an Interactor.
     *
     * @param params map of data as defined in REST API
     * @return a Result object containing information about the execution
     * @see Result
     * @see Schedule
     * @see User
     */
    @Override
    public Result get(Map<String, String> params) {
        Optional<String> projectNameString = Optional.ofNullable(params.get("projectName"));

        try {
            String projectName = projectNameString.get();

            if (!permissionHandler.canGetSchedule(params.get("auth"), projectName)) {
                return new Result<>(new NoPermissionResult());
            }

            return resultSupplier.get(interactor::getScheduleAsAssignments, projectName);
        } catch (Exception e) {
            return resultMapper.mapException(e);
        }
    }

    /**
     * "getAll" is not supported in the ScheduleAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    @Override
    public Result getAll(Map<String, String> params) {
        throw new UnsupportedOperationException("'getAll' is not supported by this adapter");
    }

    /**
     * "add" is not supported in the ScheduleAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    @Override
    public Result add(Map<String, String> params) {
        throw new UnsupportedOperationException("'add' is not supported by this adapter");
    }

    /**
     * "remove" is not supported in the ScheduleAdapter
     *
     * @param params map of data as defined in REST API
     * @return nothing
     */
    @Override
    public Result remove(Map<String, String> params) {
        throw new UnsupportedOperationException("'remove' is not supported by this adapter");
    }

    /**
     * "edit" is not supported in the ScheduleAdapter
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
        return Schedule.class;
    }
}
