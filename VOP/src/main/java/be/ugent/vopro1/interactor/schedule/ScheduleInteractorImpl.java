package be.ugent.vopro1.interactor.schedule;

import be.ugent.vopro1.converter.ConverterFacade;
import be.ugent.vopro1.converter.ConverterFactory;
import be.ugent.vopro1.funky.CustomWorkspace;
import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.model.Assignment;
import be.ugent.vopro1.model.Task;
import be.ugent.vopro1.model.UsecaseEntity;
import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.persistence.ScheduleDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import be.ugent.vopro1.scheduling.Schedule;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link ScheduleInteractor}
 */
public class ScheduleInteractorImpl implements ScheduleInteractor {

    private Logger logger = LogManager.getLogger(this.getClass());
    private ConverterFacade converter;
    private ScheduleDAO scheduleDAO;
    private ProjectDAO projectDAO;
    private EntityDAO entityDAO;
    private CustomWorkspace workspace;

    /**
     * Creates a new ScheduleInteractorImpl and sets the correct properties
     */
    public ScheduleInteractorImpl() {
        converter = ConverterFactory.getInstance();
        scheduleDAO = DAOProvider.get("schedule");
        projectDAO = DAOProvider.get("project");
        entityDAO = DAOProvider.get("entity");
        workspace = WorkspaceFactory.getInstance();
    }

    /**
     * {@inheritDoc}
     *
     * @param projectName {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public Schedule getSchedule(String projectName) {
        int projectId = projectDAO.getByName(projectName).getId();
        return new Schedule(converter.convert(scheduleDAO.getAssignmentsForProject(projectId)));
    }

    /**
     * {@inheritDoc}
     *
     * @param projectName {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<Assignment> getScheduleAsAssignments(String projectName) {
        return getSchedule(projectName).getAssignments();
    }

    /**
     * {@inheritDoc}
     *
     * @param projectName {@inheritDoc}
     * @param assignments {@inheritDoc}
     */
    @Override
    public void saveSchedule(String projectName, List<Assignment> assignments) {
        int projectId = projectDAO.getByName(projectName).getId();

        scheduleDAO.removeSchedule(projectId);
        scheduleDAO.saveAssignmentsForProject(projectId, converter.convert(assignments));
    }

    /**
     * {@inheritDoc}
     *
     * @param projectName {@inheritDoc}
     * @param useCases    {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public List<Task> getTasks(String projectName, List<String> useCases) {
        List<Task> tasks = new ArrayList<>();
        for (String useCase : useCases) {
            if (entityDAO.exists(useCase, projectName)) {
                int useCaseId = entityDAO.getByName(useCase, projectName).getId();

                if (scheduleDAO.existsTaskForUseCase(useCaseId)) {
                    Task task = converter.convert(scheduleDAO.getTaskForUseCase(useCaseId));
                    UsecaseEntity useCaseEntity = task.getUsecase();

                    try {
                        UsecaseEntity workspaceUseCase = workspace.getEntityFromProject(useCaseEntity.name(),
                                UsecaseEntity.class,
                                projectName);
                        task.setUsecase(workspaceUseCase);
                    } catch (LookupException e) {
                        logger.debug("Could not find useCase " + useCaseEntity.name() + " in project " + projectName);
                    }

                    tasks.add(task);
                }
            }
        }

        return tasks;
    }

}
