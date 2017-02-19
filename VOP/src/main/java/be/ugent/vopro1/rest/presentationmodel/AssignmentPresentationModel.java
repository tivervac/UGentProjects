package be.ugent.vopro1.rest.presentationmodel;

import be.ugent.vopro1.model.Assignment;
import com.google.common.collect.Lists;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

/**
 * Presentation model for {@link Assignment}
 */
public class AssignmentPresentationModel extends PresentationModel {

    private Assignment assignment;
    private TaskPresentationModel taskPresentationModel;
    private AvailableUserPresentationModel availableUserPresentationModel;

    @Override
    public void setEntity(Object entity) {
        this.assignment = (Assignment) entity;
        this.taskPresentationModel = new TaskPresentationModel();
        taskPresentationModel.setEntity(assignment.getTask());
        this.availableUserPresentationModel = new AvailableUserPresentationModel();
        availableUserPresentationModel.setEntity(assignment.getUser());
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    /**
     * Retrieve a presentation model for {@link Assignment#getTask()}
     *
     * @return Presentation model for the task
     */
    public TaskPresentationModel getTask() {
        return taskPresentationModel;
    }

    /**
     * Retrieve a presentation model for {@link Assignment#getUser()}
     *
     * @return Presentation model for the user
     */
    public AvailableUserPresentationModel getUser() {
        return availableUserPresentationModel;
    }

    /**
     * Retrieves the start date using {@link Assignment#getStartDate()}
     *
     * @return Start date
     */
    public LocalDateTime getStartDate() {
        return assignment.getStartDate();
    }

    /**
     * Retrieves the end date using {@link Assignment#getEnd()}
     *
     * @return End date
     */
    public LocalDateTime getEndDate() {
        return assignment.getEnd();
    }

    @Override
    public List<Supplier<? extends PresentationModel>> subPresentationModels() {
        return Lists.newArrayList(this::getTask, this::getUser);
    }
}
