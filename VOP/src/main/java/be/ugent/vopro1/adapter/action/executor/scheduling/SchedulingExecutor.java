package be.ugent.vopro1.adapter.action.executor.scheduling;

import be.ugent.vopro1.adapter.action.ProjectActionAdapter;
import be.ugent.vopro1.adapter.action.executor.AbstractExecutor;
import be.ugent.vopro1.adapter.action.executor.Executor;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.types.ConditionFailedResult;
import be.ugent.vopro1.adapter.result.types.SuccessResult;
import be.ugent.vopro1.interactor.entity.EntityInteractor;
import be.ugent.vopro1.interactor.entity.InteractorFactory;
import be.ugent.vopro1.interactor.permission.PermissionProvider;
import be.ugent.vopro1.interactor.project.ProjectInteractor;
import be.ugent.vopro1.interactor.project.ProjectInteractorFactory;
import be.ugent.vopro1.interactor.project.ProjectPermission;
import be.ugent.vopro1.interactor.schedule.ScheduleInteractor;
import be.ugent.vopro1.interactor.schedule.ScheduleInteractorFactory;
import be.ugent.vopro1.interactor.user.UserInteractor;
import be.ugent.vopro1.interactor.user.UserInteractorFactory;
import be.ugent.vopro1.model.AvailableUser;
import be.ugent.vopro1.model.ProcessEntity;
import be.ugent.vopro1.model.Task;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.scheduling.FirstFitScheduler;
import be.ugent.vopro1.scheduling.Schedule;
import be.ugent.vopro1.scheduling.comparators.ProcessComparator;
import be.ugent.vopro1.scheduling.comparators.TaskComparator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Uses the executor interface to generate a schedule.
 *
 * @see Executor
 * @see ProjectActionAdapter
 */
public class SchedulingExecutor extends AbstractExecutor {

    private static final String ACTION_TYPE = "schedule";
    private static final String AUTH_ARG = "auth";
    private static final String ACTION_ARG = "action";
    private static final String PROJECT_NAME_ARG = "projectName";
    private final TaskComparator taskComparator;
    private final ProcessComparator processComparator;
    private ProjectInteractor projectInteractor;
    private EntityInteractor<ProcessEntity> entityInteractor;
    private ScheduleInteractor scheduleInteractor;
    private UserInteractor userInteractor;
    private ProjectPermission projectPermission;

    /**
     * Creates a new SchedulingExecutor
     *
     * @param adapter {@inheritDoc}
     */
    public SchedulingExecutor(ProjectActionAdapter adapter) {
        super(adapter, ACTION_TYPE);
        this.projectPermission = PermissionProvider.get("project");
        this.projectInteractor = ProjectInteractorFactory.getInstance();
        this.entityInteractor = InteractorFactory.getInstance();
        this.userInteractor = UserInteractorFactory.getInstance();
        this.scheduleInteractor = ScheduleInteractorFactory.getInstance();
        taskComparator = new TaskComparator();
        processComparator = new ProcessComparator();
    }

    /**
     * Constructs the parameters for the given date and project to create a schedule.
     *
     * @param params {@inheritDoc}
     * @return {@inheritDoc}
     * @throws Exception thrown in {@link ProjectActionAdapter}
     */
    @Override
    public Result execute(Map<String, String> params) throws Exception {
        String projectName = params.get(PROJECT_NAME_ARG);
        SchedulingAction action = converter.convert(SchedulingAction.class, params.get(ACTION_ARG));
        LocalDateTime start = action.getStart();
        if (start == null) {
            return new Result<>(new ConditionFailedResult("A date has to be given."));
        }

        FirstFitScheduler scheduler = new FirstFitScheduler();
        List<AvailableUser> analysts = projectInteractor.getAnalysts(projectName);
        List<ProcessEntity> processes = entityInteractor.getAll(projectName, "process");
        Set<Task> schedulableTasks = new HashSet<>();
        Schedule prevSchedule = scheduleInteractor.getSchedule(projectName);

        processes.sort(processComparator);
        for (ProcessEntity process : processes) {
            List<Task> tasks = scheduleInteractor.getTasks(projectName, process.getUseCases());
            tasks = tasks.stream()
                            .filter(task -> prevSchedule.isRescheduleNeeded(task, start))
                            .collect(Collectors.toList()
            );
            tasks.sort(taskComparator);
            schedulableTasks.addAll(tasks);
        }

        Schedule schedule = scheduler.schedule(analysts, new ArrayList<>(schedulableTasks), start);
        schedule.mergeSchedule(prevSchedule, start);

        scheduleInteractor.saveSchedule(projectName, schedule.getAssignments());

        return new Result<>(new SuccessResult(), schedule);
    }

    /**
     * {@inheritDoc}
     * <p>
     * If the credentials allow the user to get the analysts from the
     * <code>origin</code> project, this method will return <code>true</code>.
     *
     * @param params {@inheritDoc}
     * @return {@inheritDoc}
     * @throws Exception thrown in {@link ProjectActionAdapter}
     * @see ProjectPermission
     */
    @Override
    public boolean canExecute(Map<String, String> params) throws Exception {
        String origin = params.get(PROJECT_NAME_ARG);
        String auth = params.get(AUTH_ARG);

        return projectPermission.canGetAnalysts(auth, origin);
    }
}
