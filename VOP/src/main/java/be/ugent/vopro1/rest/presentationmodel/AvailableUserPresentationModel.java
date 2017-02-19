package be.ugent.vopro1.rest.presentationmodel;

import be.ugent.vopro1.adapter.AdapterManager;
import be.ugent.vopro1.adapter.AdapterManagerFactory;
import be.ugent.vopro1.model.AvailableUser;
import be.ugent.vopro1.model.User;
import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Supplier;

/**
 * PresentationModel for {@link AvailableUser}
 */
public class AvailableUserPresentationModel extends PresentationModel {

    private AvailableUser availableUser;
    private UserPresentationModel userPresentationModel;
    private AdapterManager adapter = AdapterManagerFactory.getInstance();

    @Override
    public void setEntity(Object entity) {
        this.availableUser = (AvailableUser) entity;
        this.userPresentationModel = new UserPresentationModel();
        this.userPresentationModel.setEntity(this.availableUser.getUser());
    }

    @Override
    public String getIdentifier() {
        return adapter.getId(User.class, availableUser.getUser().getEmail()).orElse(-1) + "";
    }

    /**
     * Returns the number of seconds that this analyst should work.
     * Calls {@link AvailableUser#getWork()} to retrieve the data.
     *
     * @return Number of working seconds
     */
    public long getWork() {
        return availableUser.getWork();
    }

    /**
     * Returns the user associated with this analyst.
     * Calls {@link AvailableUser#getUser()} to retrieve the data.
     *
     * @return The analyst's user data
     */
    public UserPresentationModel getUser() {
        return userPresentationModel;
    }

    @Override
    public List<Supplier<? extends PresentationModel>> subPresentationModels() {
        return Lists.newArrayList(this::getUser);
    }

}
