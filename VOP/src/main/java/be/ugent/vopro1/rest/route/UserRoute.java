package be.ugent.vopro1.rest.route;

/**
 * Stores routes for the /user endpoint of the API.
 * <p>
 * These routes are used by the
 * {@link be.ugent.vopro1.rest.controller.UserRestController} to map the
 * incoming requests to the right controller method.
 *
 * @see be.ugent.vopro1.rest.controller.UserRestController
 */
public class UserRoute {

    private static final String ENDPOINT = "/user";
    private static final String IDENTIFIER = "/{id}";
    public static final String GET_ALL = ENDPOINT;
    public static final String POST = ENDPOINT;
    public static final String GET_ME = ENDPOINT + "/me";
    public static final String SELF = ENDPOINT + IDENTIFIER;

    public static final String GET_ONE = ENDPOINT + IDENTIFIER;
    public static final String DELETE = ENDPOINT + IDENTIFIER;
    public static final String PATCH = ENDPOINT + IDENTIFIER;

    private static final String TEAM_ENDPOINT = "/team";
    public static final String TEAM_GET_ALL = ENDPOINT + IDENTIFIER + TEAM_ENDPOINT;

    private static final String PROJECT_ENDPOINT = "/project";
    public static final String PROJECT_GET_ALL = ENDPOINT + IDENTIFIER + PROJECT_ENDPOINT;

    public static final String ANALYST_GET_ALL = PROJECT_GET_ALL + "?analyst=true";
}
