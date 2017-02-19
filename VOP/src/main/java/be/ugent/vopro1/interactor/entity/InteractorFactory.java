package be.ugent.vopro1.interactor.entity;

import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Holds a single instance of an EntityInteractor.
 *
 * @see EntityInteractor
 * @see EntityInteractorImpl
 */
public class InteractorFactory {

    private static EntityInteractor instance;

    /**
     * Sets a custom EntityInteractor, for dependency injection and tests
     *
     * @param interactor Interactor to set and return in future
     * {@link #getInstance()} calls
     */
    public static void setInstance(EntityInteractor interactor) {
        instance = interactor;
    }

    /**
     * Retrieves the EntityInteractor instance.
     * <p>
     * The default interactor is based on the "entityInteractor" bean defined in
     * <code>application-context.xml</code>.
     *
     * @return EntityInteractor instance
     * @see EntityInteractor
     * @see ApplicationContextProvider
     */
    public static EntityInteractor getInstance() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("entityInteractor", EntityInteractor.class);
        }

        return instance;
    }

}
