package be.ugent.vopro1.rest.presentationmodel;

import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.User;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Supplier;

/**
 * Class used to present the data of an EntityProject to the user.
 *
 * @see PresentationModel
 * @see EntityProject
 */
public class ProjectPresentationModel extends PresentationModel {

    private EntityProject entity;
    private UserPresentationModel leaderPresentationModel;

    /**
     * Default constructor of ProjectPresentationModel for Jackson.
     */
    public ProjectPresentationModel() {

    }

    /**
     * Sets the reference of this ProjectPresentationModel to an EntityProject
     * object, so that all requested information for this
     * ProjectPresentationModel can be delegated to this EntityProject object.
     *
     * @param entity an EntityProject to be represented by the
     * ProjectPresentationModel.
     * @see EntityProject
     */
    @Override
    public void setEntity(Object entity) {
        this.entity = (EntityProject) entity;
        leaderPresentationModel = new UserPresentationModel();
        leaderPresentationModel.setEntity(this.entity.getLeader());
    }

    @Override
    public String getIdentifier() {
        return getName();
    }

    /**
     * This method calls {@link EntityProject#getName()} of the EntityProject
     * it's representing and returns the name of this EntityProject.
     *
     * @return the name of the EntityProject being represented.
     * @see EntityProject
     * @see EntityProject#getName()
     */
    public String getName() {
        return entity.getName();
    }

    /**
     * This method calls {@link EntityProject#getLeader()} of the EntityProject
     * it's representing and returns the leader of this EntityProject.
     *
     * @return the leader of the EntityProject being represented.
     * @see EntityProject
     * @see EntityProject#getLeader()
     */
    public UserPresentationModel getLeader() {
        return leaderPresentationModel;
    }

    @Override
    public List<Supplier<? extends PresentationModel>> subPresentationModels() {
        return Lists.newArrayList(this::getLeader);
    }
}
