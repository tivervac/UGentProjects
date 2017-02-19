package be.ugent.vopro1.interactor.authentication;

/**
 * Provides a way to ask if a user has a high enough permission level to perform
 * an action
 */
public interface AuthenticationHandler {

    /**
     * Checks if the email and password are correct and belong to an actual
     * user.
     *
     * @param email E-mail of the user to check
     * @param password Password of the user to check
     * @return <code>true</code> if this is an actual registered user
     * <code>false</code> otherwise
     */
    boolean hasPermission(String email, String password);

    /**
     * Check if the email and password are correct and belong to an actual user.
     *
     * @param email E-mail of the user
     * @param password Password of the user
     * @param loginRequired Are valid credentials actually required for this
     * action
     * @return <code>true</code> if the permission is granted <code>false</code>
     * otherwise
     */
    boolean hasPermission(String email, String password, boolean loginRequired);

    /**
     * Check if the email and password are correct and belong to an actual user
     * who has the correct authorization level.
     *
     * @param email E-mail of the user
     * @param password Password of the user
     * @param loginRequired Are valid credentials actually required for this
     * action
     * @param adminRequired Is administrator status required for this action
     * @return <code>true</code> if the permission is granted <code>false</code>
     * otherwise
     */
    boolean hasPermission(String email, String password, boolean loginRequired, boolean adminRequired);

}
