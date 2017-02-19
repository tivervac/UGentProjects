package be.ugent.vopro1.converter;

import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Factory for the {@link ConverterFacade}
 */
public class ConverterFactory {

    private static ConverterFacade instance;

    /**
     * Sets a custom ConverterFacade, for dependency injection and tests.
     *
     * @param facade ConverterFacade to set and return in future
     * {@link #getInstance()} calls
     */
    public static void setInstance(ConverterFacade facade) {
        instance = facade;
    }

    /**
     * Retrieves the ConverterFacade instance.
     * <p>
     * The default converter is based on the "converterFacade" bean defined in
     * <code>application-context.xml</code>.
     *
     * @return Converter instance
     */
    public static ConverterFacade getInstance() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("converterFacade", ConverterFacade.class);
        }

        return instance;
    }
}
