package be.ugent.vopro1.persistence.factory;

import be.ugent.vopro1.persistence.EntityDAO;
import be.ugent.vopro1.persistence.ProjectDAO;
import be.ugent.vopro1.persistence.UserDAO;

import java.util.HashMap;

/**
 * Provides the correct {@link DAOFactory} based on a String parameter
 *
 * @see DAOFactory
 * @see EntityDAOFactory
 * @see ProjectDAOFactory
 * @see UserDAOFactory
 * @see TeamDAOFactory
 */
public class DAOProvider {

    private static HashMap<String, DAOFactory> factories = new HashMap<>();

    static {
        setDefault();
    }

    /**
     * Sets a custom DAOFactory for a certain name parameter, for dependency
     * injection and tests
     *
     * @param factoryName Name parameter of this factory. For example "entity"
     * or "project".
     * @param factory Factory to set and return in future
     * {@link #getInstance(String name)} calls
     */
    public static void setInstance(String factoryName, DAOFactory factory) {
        factories.put(factoryName, factory);
    }

    /**
     * Retrieves a DAOFactory instance based on the name parameter
     * <p>
     * The default factories are:
     * <ul>
     * <li>"entity" -&gt; {@link EntityDAOFactory}</li>
     * <li>"project" -&gt; {@link ProjectDAOFactory}</li>
     * <li>"user" -&gt; {@link UserDAOFactory}</li>
     * </ul>
     *
     * @param factoryName Name of the factory to retrieve
     * @return the requested DAOFactory instance
     */
    public static DAOFactory getInstance(String factoryName) {
        return factories.get(factoryName);
    }

    /**
     * Convenience method that retrieves the instance provided by the DAOFactory
     * immediately.
     * <p>
     * The default factories are:
     * <ul>
     * <li>"entity" -&gt; {@link EntityDAOFactory} -&gt; {@link EntityDAO}</li>
     * <li>"project" -&gt; {@link ProjectDAOFactory} -&gt;
     * {@link ProjectDAO}</li>
     * <li>"user" -&gt; {@link UserDAOFactory} -&gt; {@link UserDAO}</li>
     * </ul>
     *
     * @param factoryName Name of the factory to retrieve
     * @param <T> Type of the DAO that should be provided
     * @return the requested DAO
     */
    @SuppressWarnings("unchecked")
    public static <T> T get(String factoryName) {
        return (T) factories.get(factoryName).getInstance();
    }

    /**
     * Fills up the factories HashMap with the default factory name for each
     * DAOFactory.
     */
    public static void setDefault() {
        factories = new HashMap<>();
        factories.put("entity", new EntityDAOFactory());
        factories.put("project", new ProjectDAOFactory());
        factories.put("user", new UserDAOFactory());
        factories.put("team", new TeamDAOFactory());
        factories.put("schedule", new ScheduleDAOFactory());
    }
}
