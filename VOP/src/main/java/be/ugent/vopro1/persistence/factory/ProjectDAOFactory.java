package be.ugent.vopro1.persistence.factory;

import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Holds a single instance of a ProjectDAO.
 *
 * @see ProjectDAO
 * @see be.ugent.vopro1.persistence.jdbc.postgresql.ProjectDAOImpl
 * @see DAOFactory
 */
public class ProjectDAOFactory implements DAOFactory<ProjectDAO> {

    private ProjectDAO instance;

    /**
     * Sets a custom ProjectDAO, for dependency injection and tests
     *
     * @param dao ProjectDAO to set and return in future {@link #getInstance()}
     * calls
     */
    @Override
    public void setInstance(ProjectDAO dao) {
        instance = dao;
    }

    /**
     * Retrieves the ProjectDAO instance.
     * <p>
     * The default DAO is based on the "projectDAO" bean defined in
     * <code>application-context.xml</code>.
     *
     * @return ProjectDAO instance
     * @see ApplicationContextProvider
     */
    @Override
    public ProjectDAO getInstance() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("projectDAO", ProjectDAO.class);
        }

        return instance;
    }
}
