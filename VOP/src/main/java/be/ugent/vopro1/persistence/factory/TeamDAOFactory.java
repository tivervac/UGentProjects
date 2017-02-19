package be.ugent.vopro1.persistence.factory;

import be.ugent.vopro1.persistence.TeamDAO;
import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Holds a single instance of a TeamDAO.
 *
 * @see TeamDAO
 * @see be.ugent.vopro1.persistence.jdbc.postgresql.TeamDAOImpl
 * @see DAOFactory
 */
public class TeamDAOFactory implements DAOFactory<TeamDAO> {

    private TeamDAO instance;

    /**
     * Sets a custom TeamDAO, for dependency injection and tests
     *
     * @param dao TeamDAO to set and return in future {@link #getInstance()}
     * calls
     */
    @Override
    public void setInstance(TeamDAO dao) {
        instance = dao;
    }

    /**
     * Retrieves the TeamDAO instance.
     * <p>
     * The default DAO is based on the "teamDAO" bean defined in
     * <code>application-context.xml</code>.
     *
     * @return TeamDAO instance
     * @see ApplicationContextProvider
     */
    @Override
    public TeamDAO getInstance() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("teamDAO", TeamDAO.class);
        }

        return instance;
    }
}
