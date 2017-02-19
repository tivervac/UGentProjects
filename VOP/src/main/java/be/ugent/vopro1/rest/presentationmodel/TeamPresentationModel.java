package be.ugent.vopro1.rest.presentationmodel;

import be.ugent.vopro1.adapter.AdapterManager;
import be.ugent.vopro1.adapter.AdapterManagerFactory;
import be.ugent.vopro1.adapter.TeamAdapter;
import be.ugent.vopro1.adapter.UserAdapter;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.rest.route.UserRoute;
import be.ugent.vopro1.util.LocalConstants;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Class used to present the data of a Team to the user.
 *
 * @see PresentationModel
 * @see Team
 */
public class TeamPresentationModel extends PresentationModel {

    private Team entity;
    private UserPresentationModel leaderPresentationModel;
    private List<UserPresentationModel> memberPresentationModels;
    private AdapterManager adapter = AdapterManagerFactory.getInstance();

    /**
     * Default constructor of TeamPresentationModel for Jackson.
     */
    public TeamPresentationModel() {
    }

    /**
     * Sets the reference of this TeamPresentationModel to a Team object,
     * so that all requested information for this TeamPresentationModel can be
     * delegated to this Team object.
     *
     * @param entity a Team to be represented by the
     * TeamPresentationModel.
     * @see Team
     */
    @Override
    public void setEntity(Object entity) {
        this.entity = (Team) entity;
        this.leaderPresentationModel = new UserPresentationModel();
        this.leaderPresentationModel.setEntity(this.entity.getLeader());
        this.memberPresentationModels = new ArrayList<>();

        for (User member : this.entity.getMembers()) {
            UserPresentationModel memberPresentationModel = new UserPresentationModel();
            memberPresentationModel.setEntity(member);
            this.memberPresentationModels.add(memberPresentationModel);
        }
    }

    @Override
    public String getIdentifier() {
        return getId() + "";
    }


    /**
     * This method calls {@link TeamAdapter#getId} with the Team's name
     * to return the identifier of the TeamPresentationModel.
     *
     * @return Identifier of the TeamPresentationModel or <code>-1</code> if there is
     * no team with that name.
     * @see Team
     * @see TeamAdapter#getId
     */
    public int getId() {
        return adapter.getId(Team.class, getName()).orElse(-1);
    }

    /**
     * This method calls {@link Team#getName()} of the Team it's
     * representing and returns the name of this Team.
     *
     * @return the name of the Team being represented.
     * @see Team
     * @see Team#getName()
     */
    public String getName() {
        return entity.getName();
    }

    /**
     * This method calls {@link Team#getLeader()} of the Team it's
     * representing and returns the leader of this Team.
     *
     * @return the leader of the Team being represented.
     * @see Team
     * @see Team#getLeader()
     */
    public UserPresentationModel getLeader() {
        return leaderPresentationModel;
    }

    /**
     * Returns a list of team members.
     *
     * @return List of team members
     */
    public List<UserPresentationModel> getMembers() {
        return this.memberPresentationModels;
    }

    @Override
    public List<Supplier<? extends PresentationModel>> subPresentationModels() {
        return Lists.newArrayList(this::getLeader);
    }

    @Override
    public List<Supplier<List<? extends PresentationModel>>> subListPresentationModels() {
        return Lists.newArrayList(this::getMembers);
    }
}
