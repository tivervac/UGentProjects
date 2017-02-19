package be.ugent.vopro1.interactor.permission;

/**
 * Interface that all PermissionFactories should implement
 *
 * @param <T> Type of the PermissionInteractor instance that is provided by the implementing
 * factory
 */
public interface PermissionFactory<T> {

    /**
     * Sets a custom PermissionInteractor, for dependency injection and tests
     *
     * @param instance PermissionInteractor instance to set and return in future
     * {@link #getInstance()} calls
     */
    void setInstance(T instance);

    /**
     * Retrieves the PermissionInteractor instance.
     * <p>
     * The default PermissionInteractor should be based on a bean defined in <code>application-context.xml</code>.
     *
     * @return PermissionInteractor instance
     */
    T getInstance();

}