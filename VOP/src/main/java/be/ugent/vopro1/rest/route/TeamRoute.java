package be.ugent.vopro1.rest.route;

/**
 * Stores routes for the /team endpoint of the API.
 * <p>
 * These routes are used by the
 * {@link be.ugent.vopro1.rest.controller.TeamRestController} to map the
 * incoming requests to the right controller method.
 *
 * @see be.ugent.vopro1.rest.controller.TeamRestController
 */
public class TeamRoute {

    private static final String ENDPOINT = "/team";
    private static final String IDENTIFIER = "/{teamId}";
    public static final String GET_ALL = ENDPOINT;
    public static final String POST = ENDPOINT;
    public static final String SELF = ENDPOINT + IDENTIFIER;

    public static final String GET_ONE = ENDPOINT + IDENTIFIER;
    public static final String DELETE = ENDPOINT + IDENTIFIER;
    public static final String PATCH = ENDPOINT + IDENTIFIER;

    private static final String MEMBER_ENDPOINT = ENDPOINT + IDENTIFIER + "/member";
    public static final String MEMBER_GET_ALL = MEMBER_ENDPOINT;
    private static final String MEMBER_IDENTIFIER = "/{userId}";
    public static final String MEMBER_POST = MEMBER_ENDPOINT + MEMBER_IDENTIFIER;
    public static final String MEMBER_DELETE = MEMBER_ENDPOINT + MEMBER_IDENTIFIER;

    private static final String PROJECT_ENDPOINT = ENDPOINT + IDENTIFIER + "/project";
    public static final String PROJECT_GET_ALL = PROJECT_ENDPOINT;
    private static final String PROJECT_IDENTIFIER = "/{projectName}";
    public static final String PROJECT_POST = PROJECT_ENDPOINT + PROJECT_IDENTIFIER;
    public static final String PROJECT_DELETE = PROJECT_ENDPOINT + PROJECT_IDENTIFIER;

    public static final String ANALYST_GET_ALL = MEMBER_GET_ALL + "?analyst=true";
}
