package be.ugent.vopro1.adapter.result.mapper;

import be.ugent.vopro1.adapter.result.ResultSupplier;
import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Holds a single instance of a ResultMapper.
 *
 * @see ResultMapper
 * @see DefaultResultMapper
 */
public class ResultMapperFactory {

    private static ResultMapper instance;

    /**
     * Sets a custom ResultMapper, for dependency injection and tests
     *
     * @param mapper Supplier to set and return in future
     * {@link #getInstance()} calls
     * @see ResultMapper
     */
    public static void setInstance(ResultMapper mapper) {
        instance = mapper;
    }


    /**
     * Retrieves the ResultMapper instance.
     * <p>
     * The default supplier is based on the "ResultMapper" bean defined in
     * <code>application-context.xml</code>.
     *
     * @return ResultMapper instance
     * @see ResultMapper
     */
    public static ResultMapper getInstance() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("resultMapper", ResultMapper.class);
        }

        return instance;
    }

}
