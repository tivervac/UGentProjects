package be.ugent.vopro1.interactor.authentication;

import be.ugent.vopro1.bean.PersistentUser;
import be.ugent.vopro1.persistence.UserDAO;
import be.ugent.vopro1.persistence.factory.DAOProvider;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Provides a concrete AuthenticationHandler that can also use the HTTP
 * Authorization header to retrieve user information.
 *
 * @see HeaderAuthenticationHandler
 * @see AuthenticationHandler
 */
public class HeaderAuthenticationHandlerImpl implements HeaderAuthenticationHandler {

    public static final String BASIC = "Basic";
    private UserDAO userDAO;
    private PasswordEncoder passwordEncoder;

    /**
     * Constructs a new HeaderAuthenticationHandler with a BCrypt password
     * encoder
     *
     * @see HeaderAuthenticationHandler
     * @see BCryptPasswordEncoder
     */
    public HeaderAuthenticationHandlerImpl() {
        userDAO = DAOProvider.get("user");
        passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * {@inheritDoc}
     *
     * @param email {@inheritDoc}
     * @param password {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasPermission(String email, String password) {
        return hasPermission(email, password, false, false);
    }

    /**
     * {@inheritDoc}
     *
     * @param email {@inheritDoc}
     * @param password {@inheritDoc}
     * @param loginRequired {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasPermission(String email, String password, boolean loginRequired) {
        return hasPermission(email, password, loginRequired, false);
    }

    /**
     * {@inheritDoc}
     *
     * @param email {@inheritDoc}
     * @param password {@inheritDoc}
     * @param loginRequired {@inheritDoc}
     * @param adminRequired {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasPermission(String email, String password, boolean loginRequired, boolean adminRequired) {
        if (!loginRequired) {
            return true; // Login is not required, everyone has permission
        }
        if (email == null || password == null) {
            return false; // No e-mail or password entered
        }

        if (!userDAO.exists(email)) {
            // User with this e-mail does not exists, but login is required
            return false;
        }

        PersistentUser user = userDAO.getByEmail(email);

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return false; // Incorrect password
        }
        if (adminRequired && !user.isAdmin()) {
            return false; // Admin is required, but user is not an administrator
        }

        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @param authHeader {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasPermission(String authHeader) {
        return hasPermission(authHeader, false, false);
    }

    /**
     * {@inheritDoc}
     *
     * @param authHeader {@inheritDoc}
     * @param loginRequired {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasPermission(String authHeader, boolean loginRequired) {
        return hasPermission(authHeader, loginRequired, false);
    }

    /**
     * {@inheritDoc}
     *
     * @param authHeader {@inheritDoc}
     * @param loginRequired {@inheritDoc}
     * @param adminRequired {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public boolean hasPermission(String authHeader, boolean loginRequired, boolean adminRequired) {
        String[] credentials = getCredentials(authHeader);
        if (credentials.length != 2) {
            return false; // Incorrect amount of credentials (username - password)
        }
        return hasPermission(credentials[0], credentials[1], loginRequired, adminRequired);
    }

    /**
     * {@inheritDoc}
     *
     * @param authHeader {@inheritDoc}
     * @return {@inheritDoc}
     */
    @Override
    public String getEmail(String authHeader) {
        return getCredentials(authHeader)[0];
    }

    /**
     * This help method retrieves the email and password from the authHeader and
     * returns them as an array.
     *
     * @param authHeader HTTP Authorization header
     * @return The array containing the email and password
     */
    private String[] getCredentials(String authHeader) {
        Base64.Decoder decoder = Base64.getDecoder();
        String[] values = {null, null};

        if (authHeader != null && authHeader.startsWith(BASIC)) {
            String encodedCredentials = authHeader.substring(BASIC.length()).trim();
            String credentials = new String(decoder.decode(encodedCredentials), Charset.forName("UTF-8"));
            values = credentials.split(":");
        }

        return values;
    }

}
