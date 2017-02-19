package be.ugent.vopro1.interactor.authentication;

/**
 * Extends the {@link AuthenticationHandler} interface to be able to work with
 * an HTTP Authorization header in stead of the 'email' and 'password' string
 * from {@link AuthenticationHandler}.
 * <p>
 * This interface provides, among other things, a
 * {@link #getEmail(java.lang.String)} method to retrieve information from the
 * HTTP Authorization header.
 *
 * @see AuthenticationHandler
 */
public interface HeaderAuthenticationHandler extends AuthenticationHandler {

    /**
     * Checks if the header is correct and belongs to an actual user.
     *
     * @param authHeader HTTP Authorization header
     * @return <code>true</code> if this is an actual registered user
     * <code>false</code> otherwise
     */
    boolean hasPermission(String authHeader);

    /**
     * Checks if the header is correct and belongs to an actual user.
     *
     * @param authHeader HTTP Authorization header
     * @param loginRequired Are valid credentials actually required for this action
     * @return <code>true</code> if this is an actual registered user
     * <code>false</code> otherwise
     */
    boolean hasPermission(String authHeader, boolean loginRequired);

    /**
     * Checks if the header is correct and belongs to an actual user who has the
     * required permission level.
     *
     * @param authHeader HTTP Authorization header
     * @param loginRequired Are valid credentials actually required for this action
     * @param adminRequired Is administrator status required for this action
     * @return <code>true</code> if this is an actual registered user with the
     * required permission level. <code>false</code> otherwise
     */
    boolean hasPermission(String authHeader, boolean loginRequired, boolean adminRequired);

    /**
     * Returns the E-mail address hidden in the encoded HTTP Authorization
     * header.
     *
     * @param authHeader HTTP Authorization header
     * @return Decoded e-mail address
     */
    String getEmail(String authHeader);
}
