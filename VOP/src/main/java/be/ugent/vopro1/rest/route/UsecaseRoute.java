package be.ugent.vopro1.rest.route;

import be.ugent.vopro1.util.LocalConstants;

/**
 * Stores routes for the /usecase endpoint of the API.
 * <p>
 * These routes are used by the
 * {@link be.ugent.vopro1.rest.controller.UsecaseRestController} to map the
 * incoming requests to the right controller method.
 *
 * @see be.ugent.vopro1.rest.controller.UsecaseRestController
 */
public class UsecaseRoute {

    private static final String ENDPOINT = "/usecase";
    private static final String IDENTIFIER = "/{usecaseName}";
    public static final String GET_ALL = ENDPOINT;
    public static final String PATCH = LocalConstants.URL + IDENTIFIER;
    public static final String DELETE = LocalConstants.URL + IDENTIFIER;
    public static final String SELF = LocalConstants.URL + IDENTIFIER;
}
