package be.ugent.vopro1.interactor.project;

import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Holds a single instance of a ProjectInteractor.
 *
 * @see ProjectInteractor
 * @see ProjectInteractorImpl
 */
public class ProjectInteractorFactory {

    private static ProjectInteractor instance;

    /**
     * Sets a custom ProjectInteractor, for dependency injection and tests
     *
     * @param interactor Interactor to set and return in future
     * {@link #getInstance()} calls
     * @see ProjectInteractor
     */
    public static void setInstance(ProjectInteractor interactor) {
        instance = interactor;
    }

    /**
     * Retrieves the ProjectInteractor instance.
     * <p>
     * The default interactor is based on the "projectInteractor" bean defined
     * in <code>application-context.xml</code>.
     *
     * @return ProjectInteractor instance
     * @see ProjectInteractor
     * @see ApplicationContextProvider
     */
    public static ProjectInteractor getInstance() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("projectInteractor", ProjectInteractor.class);
        }

        return instance;
    }
}
