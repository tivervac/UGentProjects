package be.ugent.vopro1.adapter.result;

import be.ugent.vopro1.util.ApplicationContextProvider;

/**
 * Holds a single instance of a ResultSupplier.
 *
 * @see ResultSupplier
 * @see ResultSupplierImpl
 */
public class ResultSupplierFactory {

    private static ResultSupplier instance;

    /**
     * Sets a custom ResultSupplier, for dependency injection and tests
     *
     * @param supplier Supplier to set and return in future
     * {@link #getInstance()} calls
     * @see ResultSupplier
     */
    public static void setInstance(ResultSupplier supplier) {
        instance = supplier;
    }

    /**
     * Retrieves the ResultSupplier instance.
     * <p>
     * The default supplier is based on the "resultSupplier" bean defined in
     * <code>application-context.xml</code>.
     *
     * @return ResultSupplier instance
     * @see ResultSupplier
     */
    public static ResultSupplier getInstance() {
        if (instance == null) {
            instance = ApplicationContextProvider.getApplicationContext().getBean("resultSupplier", ResultSupplier.class);
        }

        return instance;
    }

}
