package be.ugent.vopro1.rest.controller;

import be.ugent.vopro1.adapter.AdapterManager;
import be.ugent.vopro1.adapter.AdapterManagerFactory;
import be.ugent.vopro1.rest.mapper.WebMapper;
import be.ugent.vopro1.rest.mapper.WebMapperProvider;
import be.ugent.vopro1.util.LocalConstants;

/**
 * Provides an abstract Controller with some properties that are shared for all RestControllers
 *
 * @see AdapterManager
 * @see WebMapper
 */
public abstract class RestController {

    protected final AdapterManager adapter;
    protected final WebMapper mapper;
    protected static final String CONCEPT_TYPE = "concept";
    protected static final String ACTOR_TYPE = "actor";
    protected static final String USECASE_TYPE = "usecase";
    protected static final String BLOB = "blob";
    protected static final String ACTION_ARG = "action";
    protected static final String ACTION_TYPE = "type";
    protected static final String AUTH_ARG = "auth";
    protected static final String WORK_ARG = "work";
    protected static final String SPEC = "specification";
    protected static final String REFACTOR_ARG = "refactor";
    protected static final String USECASE_ACTION_TYPE = USECASE_TYPE;

    /**
     * Creates a new RestController and retrieves required properties.
     * Currently uses '{@value LocalConstants#MAPPING_MODE}' as the default
     * way to map the endpoints to JSON.
     *
     * @see LocalConstants#MAPPING_MODE
     */
    public RestController() {
        adapter = AdapterManagerFactory.getInstance();
        mapper = WebMapperProvider.get(LocalConstants.MAPPING_MODE);
    }

    /**
     * Trims an URL so that it doesn't contain the last part
     *
     * @param url URL to trim
     * @return Trimmed URL
     */
    protected String trimId(String url) {
        if (url.length() <= 1) {
            return url;
        }
        String trimmedUrl = url;

        if (trimmedUrl.endsWith("/")) {
            trimmedUrl = trimmedUrl.substring(0, url.length() - 1);
        }

        return trimmedUrl.substring(0, url.lastIndexOf("/"));
    }

}
