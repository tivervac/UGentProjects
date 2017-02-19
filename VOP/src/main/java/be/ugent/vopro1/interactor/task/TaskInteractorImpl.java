package be.ugent.vopro1.interactor.task;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.bean.PersistentTask;
import be.ugent.vopro1.converter.ConverterFacade;
import be.ugent.vopro1.converter.ConverterFactory;
import be.ugent.vopro1.model.Task;
import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.persistence.ScheduleDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import be.ugent.vopro1.util.error.RequirementNotMetException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the {@link TaskInteractor} interface
 *
 * @see TaskInteractor
 */
public class TaskInteractorImpl implements TaskInteractor {

    public static final String USECASE = "usecase";
    private EntityDAO entityDAO;
    private ScheduleDAO scheduleDAO;
    private ConverterFacade converter;

    /**
     * Creates a new TaskInteractorImpl
     */
    public TaskInteractorImpl() {
        entityDAO = DAOProvider.get("entity");
        scheduleDAO = DAOProvider.get("schedule");
        converter = ConverterFactory.getInstance();
    }

    @Override
    public Task createTask(String projectName, String useCaseName, Task task) {
        PersistentObject object = getUseCase(projectName, useCaseName);

        PersistentTask persistentTask = converter.convert(task);
        persistentTask = persistentTask.useCaseId(object.getId());
        scheduleDAO.saveTaskForUseCase(persistentTask);

        return getTask(projectName, useCaseName);
    }

    @Override
    public Task getTask(String projectName, String useCaseName) {
        PersistentObject object = getUseCase(projectName, useCaseName);
        if (! scheduleDAO.existsTaskForUseCase(object.getId())) {
            throw new RequirementNotMetException("Task does not exist for this use case.");
        }

        return converter.convert(scheduleDAO.getTaskForUseCase(object.getId()));
    }

    @Override
    public List<Task> getTasks(String projectName) {
        List<PersistentObject> objects = entityDAO.getAllForProject(projectName);
        List<PersistentTask> tasks = objects
                .stream()
                .filter(object -> object.getType().equals(USECASE)
                        && scheduleDAO.existsTaskForUseCase(object.getId()))
                .map(object -> scheduleDAO.getTaskForUseCase(object.getId()))
                .collect(Collectors.toList());

        return converter.convert(tasks);
    }

    @Override
    public Task updateTask(String projectName, String useCaseName, Task updatedTask) {
        PersistentObject object = getUseCase(projectName, useCaseName);
        PersistentTask updatedPersistentTask = converter.convert(updatedTask);
        updatedPersistentTask = updatedPersistentTask.useCaseId(object.getId());

        scheduleDAO.updateTaskForUseCase(updatedPersistentTask);

        return getTask(projectName, useCaseName);
    }

    @Override
    public void deleteTask(String projectName, String useCaseName) {
        PersistentObject object = getUseCase(projectName, useCaseName);

        scheduleDAO.deleteTaskForUseCase(object.getId());
    }

    private PersistentObject getUseCase(String projectName, String useCaseName) {
        if (! entityDAO.exists(useCaseName, projectName)) {
            throw new RequirementNotMetException("Entity does not exist");
        }

        PersistentObject object = entityDAO.getByName(useCaseName, projectName);
        if (! object.getType().equals(USECASE)) {
            throw new RequirementNotMetException("Entity needs to be a use case");
        }

        return object;
    }
}
