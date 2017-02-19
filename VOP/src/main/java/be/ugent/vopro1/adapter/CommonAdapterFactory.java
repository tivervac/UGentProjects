package be.ugent.vopro1.adapter;

import be.ugent.vopro1.util.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds a single instance of of each CommonAdapter associated with an Entity
 * class in a Map.
 *
 * @see CommonAdapter
 * @see DefaultAdapterManager
 */
public class CommonAdapterFactory {

    private static ApplicationContext CONTEXT = ApplicationContextProvider.getApplicationContext();
    private static Map<Class, CommonAdapter> instance;

    /**
     * Sets a custom map, for dependency injection and tests
     *
     * @param adapters Map of the adapters to use in future
     * {@link #getInstance(Class type)} calls
     * @see CommonAdapter
     */
    public static void setInstance(Map<Class, CommonAdapter> adapters) {
        instance = adapters;
    }

    /**
     * Retrieves the CommonAdapter that is responsible for the given class.
     * <p>
     * The default mapping is based on the beans defined in
     * <code>application-context.xml</code>.
     *
     * @param type Class that should be adapted
     * @return CommonAdapter that can adapt the given class
     * @see CommonAdapter
     */
    public static CommonAdapter getInstance(Class type) {
        if (instance == null) {
            instance = new HashMap<>();

            for (String adapterName : CONTEXT.getBeansOfType(CommonAdapter.class).keySet()) {
                CommonAdapter adapter = CONTEXT.getBean(adapterName, CommonAdapter.class);
                Class adapterType = adapter.getType();

                instance.put(adapterType, adapter);
            }
        }

        return instance.get(type);
    }
}
