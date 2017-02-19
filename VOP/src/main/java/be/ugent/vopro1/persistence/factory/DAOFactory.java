package be.ugent.vopro1.persistence.factory;

/**
 * Interface that all DAOFactories should implement
 *
 * @param <T> Type of the DAO instance that is provided by the implementing
 * factory
 */
public interface DAOFactory<T> {

    /**
     * Sets a custom DAO, for dependency injection and tests
     *
     * @param instance DAO instance to set and return in future
     * {@link #getInstance()} calls
     */
    void setInstance(T instance);

    /**
     * Retrieves the DAO instance.
     * <p>
     * The default DAO should be based on a bean defined in
     * <code>dao.xml</code>.
     *
     * @return DAO instance
     */
    T getInstance();

}
