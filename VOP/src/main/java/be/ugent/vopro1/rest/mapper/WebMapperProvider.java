package be.ugent.vopro1.rest.mapper;

import be.ugent.vopro1.rest.mapper.hateoas.HateoasWebMapperFactory;
import be.ugent.vopro1.rest.mapper.normal.NormalWebMapperFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides {@link WebMapper}s based on the desired mapping system
 */
public class WebMapperProvider {

    /**
     * Mapping mode for HATEOAS results
     */
    public static final String HATEOAS_MAPPING = "hateoas";

    /**
     * Mapping mode with normal results, without any other additions
     */
    public static final String NORMAL_MAPPING = "normal";

    private static Map<String, WebMapperFactory> factories;
    static {
        setDefault();
    }

    /**
     * Sets a map of factories that can be used to create {@link WebMapper}s.
     *
     * @param map New map to use. A deep copy will be created for internal use.
     * @see WebMapper
     * @see WebMapperFactory
     */
    public static void setFactories(Map<String, WebMapperFactory> map) {
        factories = Collections.unmodifiableMap(map);
    }

    /**
     * Retrieves a deep copy of the factories that are available to create {@link WebMapper}s
     *
     * @return Deep copy of the factories that are available
     */
    public static Map<String, WebMapperFactory> getFactories() {
        return Collections.unmodifiableMap(factories);
    }

    /**
     * Returns a WebMapper.
     * <p>
     * By default, the following factories are available:
     * <ul>
     * <li>"normal" -&gt; {@link NormalWebMapperFactory} -&gt; {@link be.ugent.vopro1.rest.mapper.normal.NormalWebMapper}</li>
     * <li>"hateoas" -&gt; {@link HateoasWebMapperFactory} -&gt; {@link be.ugent.vopro1.rest.mapper.hateoas.HateoasWebMapper}</li>
     * </ul>
     *
     * @param name Name of the WebMapper to retrieve.
     * @return Requested WebMapper
     * @throws NullPointerException If the requested factory/mapper is not available
     * @see WebMapperProvider#NORMAL_MAPPING
     * @see WebMapperProvider#HATEOAS_MAPPING
     */
    public static WebMapper get(String name) {
        return factories.get(name).getInstance();
    }

    /**
     * Returns the WebMapper settings to the default
     */
    public static void setDefault() {
        Map<String, WebMapperFactory> temp = new HashMap<>();
        temp.put(NORMAL_MAPPING, new NormalWebMapperFactory());
        temp.put(HATEOAS_MAPPING, new HateoasWebMapperFactory());
        setFactories(temp);
    }

}
