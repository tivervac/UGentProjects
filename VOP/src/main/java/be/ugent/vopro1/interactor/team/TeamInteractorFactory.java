package be.ugent.vopro1.interactor.team;

import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Holds a single instance of a TeamInteractor.
 *
 * @see TeamInteractor
 * @see TeamInteractorImpl
 */
public class TeamInteractorFactory {

    private static TeamInteractor instance;

    /**
     * Sets a custom TeamInteractor, for dependency injection and tests
     *
     * @param interactor Interactor to set and return in future
     * {@link #getInstance()} calls
     */
    public static void setInstance(TeamInteractor interactor) {
        instance = interactor;
    }

    /**
     * Retrieves the TeamInteractor instance.
     * <p>
     * The default interactor is based on the "teamInteractor" bean defined in
     * <code>application-context.xml</code>.
     *
     * @return TeamInteractor instance
     * @see TeamInteractor
     * @see ApplicationContextProvider
     */
    public static TeamInteractor getInstance() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("teamInteractor", TeamInteractor.class);
        }

        return instance;
    }

}
