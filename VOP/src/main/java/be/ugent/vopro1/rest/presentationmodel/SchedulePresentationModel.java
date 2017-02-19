package be.ugent.vopro1.rest.presentationmodel;

import be.ugent.vopro1.model.Assignment;
import be.ugent.vopro1.scheduling.Schedule;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * PresentationModel for {@link Schedule}
 */
public class SchedulePresentationModel extends PresentationModel {

    private Schedule schedule;
    private List<AssignmentPresentationModel> assignmentPresentationModels;

    @Override
    public void setEntity(Object entity) {
        this.schedule = (Schedule) entity;
        this.assignmentPresentationModels = new ArrayList<>();

        for (Assignment ass : this.schedule.getAssignments()) {
            AssignmentPresentationModel assignmentPresentationModel = new AssignmentPresentationModel();
            assignmentPresentationModel.setEntity(ass);
            assignmentPresentationModels.add(assignmentPresentationModel);
        }
    }

    @Override
    public String getIdentifier() {
        return null;
    }

    /**
     * Retrieves the presentation models of all assignments in this schedule using {@link Schedule#getAssignments()}
     *
     * @return Presentation models of all assignments in this schedule
     */
    public List<AssignmentPresentationModel> getAssignments() {
        return assignmentPresentationModels;
    }

    @Override
    public List<Supplier<List<? extends PresentationModel>>> subListPresentationModels() {
        return Lists.newArrayList(this::getAssignments);
    }
}
