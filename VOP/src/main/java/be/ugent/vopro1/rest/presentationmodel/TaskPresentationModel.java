package be.ugent.vopro1.rest.presentationmodel;

import be.ugent.vopro1.model.Task;
import org.aikodi.chameleon.core.declaration.Declaration;

/**
 * PresentationModel for {@link Task}
 */
public class TaskPresentationModel extends PresentationModel {

    private Task task;

    @Override
    public void setEntity(Object entity) {
        this.task = (Task) entity;
    }

    @Override
    public String getIdentifier() {
        return task.getUsecase().name();
    }

    /**
     * Returns the use case name associated with the task by executing the task's {@link Declaration#name()}
     *
     * @return Use case associated with this task
     */
    public String getUseCase() {
        return task.getUsecase().name();
    }

    /**
     * Retrieves the priority associated with the task by executing {@link Task#getPriority()}
     *
     * @return Priority associated with this task
     */
    public int getPriority() {
        return task.getPriority();
    }

    /**
     * Retrieves the work load associated with the task by executing {@link Task#getWorkload()}
     *
     * @return Workload associated with this task
     */
    public long getWorkload() {
        return task.getWorkload();
    }

}
