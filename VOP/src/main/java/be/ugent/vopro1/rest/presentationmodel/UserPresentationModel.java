package be.ugent.vopro1.rest.presentationmodel;

import be.ugent.vopro1.adapter.AdapterManager;
import be.ugent.vopro1.adapter.AdapterManagerFactory;
import be.ugent.vopro1.adapter.UserAdapter;
import be.ugent.vopro1.model.User;

/**
 * Class used to present the data of a User to the user.
 *
 * @see PresentationModel
 * @see User
 */
public class UserPresentationModel extends PresentationModel {

    private User entity;
    private AdapterManager adapter = AdapterManagerFactory.getInstance();

    /**
     * Default constructor of UserPresentationModel for Jackson.
     */
    public UserPresentationModel() {

    }

    /**
     * Sets the reference of this PresentationModel to an Entity object, so that
     * all requested information for this PresentationModel can be delegated to
     * this Entity object.
     *
     * @param entity Entity object that must be represented by the
     * PresentationModel
     * @see User
     */
    @Override
    public void setEntity(Object entity) {
        this.entity = (User) entity;
    }

    @Override
    public String getIdentifier() {
        return getId() + "";
    }

    /**
     * This method calls {@link User#getFirstName()} of the User
     * it's representing and returns the first name of the
     * UserPresentationModel.
     *
     * @return First name of the UserPresentationModel
     * @see User
     * @see User#getFirstName()
     */
    public String getFirstName() {
        return entity.getFirstName();
    }

    /**
     * This method calls {@link User#getLastName()} of the User it's
     * representing and returns the last name of the UserPresentationModel.
     *
     * @return Last name of the UserPresentationModel
     * @see User
     * @see User#getLastName()
     */
    public String getLastName() {
        return entity.getLastName();
    }

    /**
     * This method calls {@link User#getEmail()} of the User it's
     * representing and returns the e-mail of the UserPresentationModel.
     *
     * @return E-mail of the UserPresentationModel
     * @see User
     * @see User#getEmail()
     */
    public String getEmail() {
        return entity.getEmail();
    }

    /**
     * This method calls {@link UserAdapter#getId} with the User's e-mail address
     * to return the identifier of the UserPresentationModel.
     *
     * @return Identifier of the UserPresentationModel or <code>-1</code> if there is
     * no user with that e-mail address.
     * @see User
     * @see UserAdapter#getId
     */
    public int getId() {
        return adapter.getId(User.class, getEmail()).orElse(-1);
    }

    /**
     * This method calls {@link User#isAdmin()} of the User it's
     * representing and returns the admin status of the UserPresentationModel.
     *
     * @return Admin status of the UserPresentationModel
     * @see User
     * @see User#isAdmin()
     */
    public boolean isAdmin() {
        return entity.isAdmin();
    }

}
