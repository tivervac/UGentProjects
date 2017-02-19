package be.ugent.vopro1.converter.conversion;

import be.ugent.vopro1.bean.PersistentObject;
import be.ugent.vopro1.bean.PersistentProject;
import be.ugent.vopro1.bean.PersistentTask;
import be.ugent.vopro1.converter.ConverterFacade;
import be.ugent.vopro1.converter.ConverterFactory;
import be.ugent.vopro1.funky.CustomWorkspace;
import be.ugent.vopro1.funky.WorkspaceFactory;
import be.ugent.vopro1.model.Task;
import be.ugent.vopro1.model.UsecaseEntity;
import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.persistence.ScheduleDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Function;

/**
 * Performs a conversion from a {@link PersistentObject#getId()} to a {@link Task}
 */
public class TaskIdentifierConversion implements Function<Integer, Task> {

    private Logger logger = LogManager.getLogger(this.getClass());

    @Override
    public Task apply(Integer integer) {
        ScheduleDAO scheduleDAO = DAOProvider.get("schedule");
        EntityDAO entityDAO = DAOProvider.get("entity");
        ProjectDAO projectDAO = DAOProvider.get("project");
        CustomWorkspace workspace = WorkspaceFactory.getInstance();
        ConverterFacade converter = ConverterFactory.getInstance();
        PersistentObject object = entityDAO.getById(integer);
        PersistentProject project = projectDAO.getById(object.getProjectId());

        Task task = converter.convert(scheduleDAO.getTaskForUseCase(integer));
        try {
            task.setUsecase(workspace.getEntityFromProject(object.getName(), UsecaseEntity.class, project.getName()));
        } catch (LookupException e) {
            logger.debug("Could not retrieve UseCase for identifier " + integer);
        }

        return task;
    }
}
