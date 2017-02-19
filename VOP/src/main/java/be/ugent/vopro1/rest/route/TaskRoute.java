package be.ugent.vopro1.rest.route;

/**
 * Stores routes for the /project/{projectName}/usecase/{useCaseName}/task endpoint of the API.
 * <p>
 * These routes are used by the
 * {@link be.ugent.vopro1.rest.controller.TaskRestController} to map the
 * incoming requests to the right controller method.
 *
 * @see be.ugent.vopro1.rest.controller.TaskRestController
 */
public class TaskRoute {
    private static final String PROJECT_ENDPOINT = "/project";
    private static final String PROJECT_IDENTIFIER = "/{projectName}";

    private static final String USECASE_ENDPOINT = "/usecase";
    private static final String USECASE_IDENTIFIER = "/{usecaseName}";

    public static final String TASK_ENDPOINT = PROJECT_ENDPOINT + PROJECT_IDENTIFIER +
                                            USECASE_ENDPOINT + USECASE_IDENTIFIER +
                                            "/task";
}
