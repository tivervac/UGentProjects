package be.ugent.vopro1.persistence.factory;

import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Holds a single instance of an EntityDAO.
 *
 * @see EntityDAO
 * @see be.ugent.vopro1.persistence.jdbc.postgresql.EntityDAOImpl
 * @see DAOFactory
 */
public class EntityDAOFactory implements DAOFactory<EntityDAO> {

    private EntityDAO instance;

    /**
     * Sets a custom EntityDAO, for dependency injection and tests
     *
     * @param dao EntityDAO to set and return in future {@link #getInstance()}
     * calls
     */
    @Override
    public void setInstance(EntityDAO dao) {
        instance = dao;
    }

    /**
     * Retrieves the EntityDAO instance.
     * <p>
     * The default DAO is based on the "entityDAO" bean defined in
     * <code>application-context.xml</code>.
     *
     * @return EntityDAO instance
     * @see ApplicationContextProvider
     */
    @Override
    public EntityDAO getInstance() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("entityDAO", EntityDAO.class);
        }

        return instance;
    }

}
