package be.ugent.vopro1.adapter;

/**
 * Holds a single instance of an AdapterManager. The default is the
 * DefaultAdapterManager.
 *
 * @see DefaultAdapterManager
 * @see AdapterManager
 */
public class AdapterManagerFactory {

    private static AdapterManager instance;

    /**
     * Sets a different AdapterManager instance, for custom dependency injection
     * and tests.
     *
     * @param manager AdapterManager to set and return in future
     * {@link #getInstance()} calls
     * @see AdapterManager
     */
    public static void setInstance(AdapterManager manager) {
        instance = manager;
    }

    /**
     * Retrieves the AdapterManager.
     *
     * @return By default, it will return a new DefaultAdapterManager. However,
     * if a custom AdapterManager instance has been installed through
     * {@link #setInstance} it will return that instance instead.
     * @see AdapterManager
     */
    public static AdapterManager getInstance() {
        if (instance == null) {
            instance = new DefaultAdapterManager();
        }

        return instance;
    }

}
