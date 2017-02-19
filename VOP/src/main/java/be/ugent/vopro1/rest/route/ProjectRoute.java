package be.ugent.vopro1.rest.route;

/**
 * Stores routes for the /project endpoint of the API.
 * <p>
 * These routes are used by the
 * {@link be.ugent.vopro1.rest.controller.ProjectRestController} to map the
 * incoming requests to the right controller method.
 *
 * @see be.ugent.vopro1.rest.controller.ProjectRestController
 */
public class ProjectRoute {

    private static final String ENDPOINT = "/project";
    private static final String IDENTIFIER = "/{projectName}";
    public static final String GET_ALL = ENDPOINT;
    public static final String GET_ALL_TEAM_ASSIGNABLE = GET_ALL + "?team_assignable=true";
    public static final String POST = ENDPOINT;
    public static final String GET_ONE = ENDPOINT + IDENTIFIER;
    public static final String DELETE = ENDPOINT + IDENTIFIER;
    public static final String PATCH = ENDPOINT + IDENTIFIER;
    public static final String SELF = ENDPOINT + IDENTIFIER;

    // General
    private static final String ENTITY_IDENTIFIER = "/{name}";
    private static final String ENTITY_TYPE = "/{type}";
    public static final String ACTION_ENTITY = ENDPOINT + IDENTIFIER + ENTITY_TYPE + ENTITY_IDENTIFIER;

    // Actor subsection
    private static final String ACTOR_ENDPOINT = ENDPOINT + IDENTIFIER + "/actor";
    public static final String GET_ACTOR_ALL = ACTOR_ENDPOINT;
    public static final String POST_ACTOR = ACTOR_ENDPOINT;
    public static final String GET_ACTOR_ONE = ACTOR_ENDPOINT + ENTITY_IDENTIFIER;
    public static final String DELETE_ACTOR = ACTOR_ENDPOINT + ENTITY_IDENTIFIER;
    public static final String PATCH_ACTOR = ACTOR_ENDPOINT + ENTITY_IDENTIFIER;

    // Concept subsection
    private static final String CONCEPT_ENDPOINT = ENDPOINT + IDENTIFIER + "/concept";
    public static final String GET_CONCEPT_ALL = CONCEPT_ENDPOINT;
    public static final String POST_CONCEPT = CONCEPT_ENDPOINT;
    public static final String GET_CONCEPT_ONE = CONCEPT_ENDPOINT + ENTITY_IDENTIFIER;
    public static final String DELETE_CONCEPT = CONCEPT_ENDPOINT + ENTITY_IDENTIFIER;
    public static final String PATCH_CONCEPT = CONCEPT_ENDPOINT + ENTITY_IDENTIFIER;
    public static final String ACTION_CONCEPT = CONCEPT_ENDPOINT + ENTITY_IDENTIFIER;

    // Usecase subsection
    private static final String USECASE_ENDPOINT = ENDPOINT + IDENTIFIER + "/usecase";
    public static final String GET_USECASE_ALL = USECASE_ENDPOINT;
    public static final String POST_USECASE = USECASE_ENDPOINT;
    public static final String GET_USECASE_ONE = USECASE_ENDPOINT + ENTITY_IDENTIFIER;
    public static final String DELETE_USECASE = USECASE_ENDPOINT + ENTITY_IDENTIFIER;
    public static final String PATCH_USECASE = USECASE_ENDPOINT + ENTITY_IDENTIFIER;
    public static final String ACTION_USECASE = USECASE_ENDPOINT + ENTITY_IDENTIFIER;

    // Analyst subsection
    private static final String ANALYST_ENDPOINT_NAME = "/analyst";
    private static final String ANALYST_ENDPOINT = ENDPOINT + IDENTIFIER + ANALYST_ENDPOINT_NAME;
    public static final String GET_ANALYST_ALL = ANALYST_ENDPOINT;
    public static final String GET_ELIGIBLE_ANALYST_ALL = ENDPOINT + IDENTIFIER + "/eligible_analyst";
    private static final String ANALYST_IDENTIFIER = "/{userId}";
    public static final String POST_ANALYST = ANALYST_ENDPOINT + ANALYST_IDENTIFIER;
    public static final String PATCH_ANALYST = ANALYST_ENDPOINT + ANALYST_IDENTIFIER;
    public static final String DELETE_ANALYST = ANALYST_ENDPOINT + ANALYST_IDENTIFIER;
    
    // Usecase analyst subsection
    private static final String USECASE_ANALYST_ENDPOINT = USECASE_ENDPOINT + ENTITY_IDENTIFIER + ANALYST_ENDPOINT_NAME;
    public static final String GET_USECASE_ANALYST_ALL = USECASE_ANALYST_ENDPOINT;
    private static final String USECASE_ANALYST_IDENTIFIER = ANALYST_IDENTIFIER;
    public static final String POST_USECASE_ANALYST = USECASE_ANALYST_ENDPOINT + USECASE_ANALYST_IDENTIFIER;
    public static final String DELETE_USECASE_ANALYST = USECASE_ANALYST_ENDPOINT + USECASE_ANALYST_IDENTIFIER;

    // Scheduler
    public static final String ACTION_SCHEDULE = ENDPOINT + IDENTIFIER + "/schedule";
    public static final String GET_SCHEDULE = ACTION_SCHEDULE;

    // Process
    private static final String PROCESS_ENDPOINT = ENDPOINT + IDENTIFIER + "/process";
    public static final String GET_PROCESS_ALL = PROCESS_ENDPOINT;
    public static final String POST_PROCESS = PROCESS_ENDPOINT;
    public static final String GET_PROCESS_ONE = PROCESS_ENDPOINT + ENTITY_IDENTIFIER;
    public static final String PATCH_PROCESS = PROCESS_ENDPOINT + ENTITY_IDENTIFIER;
    public static final String DELETE_PROCESS = PROCESS_ENDPOINT + ENTITY_IDENTIFIER;
}
