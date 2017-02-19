package be.ugent.vopro1.interactor.user;

import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Holds a single instance of a UserInteractor.
 *
 * @see UserInteractor
 * @see UserInteractorImpl
 */
public class UserInteractorFactory {

    private static UserInteractor instance;

    /**
     * Sets a custom UserInteractor, for dependency injection and tests
     *
     * @param interactor Interactor to set and return in future
     * {@link #getInstance()} calls
     */
    public static void setInstance(UserInteractor interactor) {
        instance = interactor;
    }

    /**
     * Retrieves the UserInteractor instance.
     * <p>
     * The default interactor is based on the "userInteractor" bean defined in
     * <code>application-context.xml</code>.
     *
     * @return UserInteractor instance
     * @see UserInteractor
     * @see ApplicationContextProvider
     */
    public static UserInteractor getInstance() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("userInteractor", UserInteractor.class);
        }

        return instance;
    }
}
