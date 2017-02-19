package be.ugent.vopro1.interactor.task;

import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Holds a single instance of a TaskInteractor.
 *
 * @see TaskInteractor
 * @see TaskInteractorImpl
 */
public class TaskInteractorFactory {

    private static TaskInteractor instance;

    /**
     * Sets a custom TaskInteractor, for dependency injection and tests
     *
     * @param interactor Interactor to set and return in future
     * {@link #getInstance()} calls
     * @see TaskInteractor
     */
    public static void setInstance(TaskInteractor interactor) {
        instance = interactor;
    }

    /**
     * Retrieves the TaskInteractor instance.
     * <p>
     * The default interactor is based on the "taskInteractor" bean defined
     * in <code>application-context.xml</code>.
     *
     * @return TaskInteractor instance
     * @see TaskInteractor
     * @see ApplicationContextProvider
     */
    public static TaskInteractor getInstance() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("taskInteractor", TaskInteractor.class);
        }

        return instance;
    }
}
