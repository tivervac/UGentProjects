package be.ugent.vopro1.interactor.schedule;

import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Holds a single instance of a ScheduleInteractor.
 *
 * @see ScheduleInteractor
 * @see ScheduleInteractorImpl
 */
public class ScheduleInteractorFactory {

    private static ScheduleInteractor instance;

    /**
     * Sets a custom ScheduleInteractor, for dependency injection and tests
     *
     * @param interactor Interactor to set and return in future
     * {@link #getInstance()} calls
     * @see ScheduleInteractor
     */
    public static void setInstance(ScheduleInteractor interactor) {
        instance = interactor;
    }

    /**
     * Retrieves the ScheduleInteractor instance.
     * <p>
     * The default interactor is based on the "scheduleInteractor" bean defined
     * in <code>application-context.xml</code>.
     *
     * @return ScheduleInteractor instance
     * @see ScheduleInteractor
     * @see ApplicationContextProvider
     */
    public static ScheduleInteractor getInstance() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("scheduleInteractor", ScheduleInteractor.class);
        }

        return instance;
    }
}
