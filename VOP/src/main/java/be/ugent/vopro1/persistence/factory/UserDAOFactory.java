package be.ugent.vopro1.persistence.factory;

import be.ugent.vopro1.persistence.UserDAO;
import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Holds a single instance of a UserDAO.
 *
 * @see be.ugent.vopro1.persistence.UserDAO
 * @see be.ugent.vopro1.persistence.jdbc.postgresql.UserDAOImpl
 * @see DAOFactory
 */
public class UserDAOFactory implements DAOFactory<UserDAO> {

    private UserDAO instance;

    /**
     * Sets a custom UserDAO, for dependency injection and tests
     *
     * @param dao UserDAO to set and return in future {@link #getInstance()}
     * calls
     */
    @Override
    public void setInstance(UserDAO dao) {
        instance = dao;
    }

    /**
     * Retrieves the UserDAO instance.
     * <p>
     * The default DAO is based on the "userDAO" bean defined in
     * <code>application-context.xml</code>.
     *
     * @return UserDAO instance
     */
    @Override
    public UserDAO getInstance() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("userDAO", UserDAO.class);
        }

        return instance;
    }

}
