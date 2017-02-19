package be.ugent.vopro1.reference;

import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Provides a LookupStrategy
 *
 * @see LookupStrategy
 */
public class LookupStrategyProvider {

    private static LookupStrategy instance;

    /**
     * Retrieves a LookupStrategy. If it does not yet exist (is null), it will retrieve the default strategy
     * from the <code>application-context.xml</code> file.
     *
     * @return Current LookupStrategy
     * @see DefaultLookupStrategy
     */
    public static LookupStrategy get() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("lookupStrategy", LookupStrategy.class);
        }

        return instance;
    }

    /**
     * Sets a different LookupStrategy for future calls to {@link #get()}
     *
     * @param strategy LookupStrategy to set
     */
    public static void set(LookupStrategy strategy) {
        instance = strategy;
    }

}
