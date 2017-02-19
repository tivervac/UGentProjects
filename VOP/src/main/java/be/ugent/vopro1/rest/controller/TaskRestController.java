package be.ugent.vopro1.rest.controller;

import be.ugent.vopro1.adapter.AdapterManager;
import be.ugent.vopro1.adapter.AdapterManagerFactory;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.model.Task;
import be.ugent.vopro1.rest.mapper.WebMapper;
import be.ugent.vopro1.rest.route.TaskRoute;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * RestController processing the HTTP requests to the /project/{projectName}/usecase/{usecaseName}/task endpoints.
 * <p>
 * Passes HTTP requests to the adapter layer, where the requests will be
 * handled. This adapter layer will return a Result object to this
 * RestController. This Result object will be converted to a valid HttpEntity
 * object by a WebMapper instance. This HttpEntity represents the HTTP response.
 *
 * @see org.springframework.web.bind.annotation.RestController
 * @see AdapterManager
 * @see Result
 * @see WebMapper
 * @see HttpEntity
 */
@org.springframework.web.bind.annotation.RestController
public class TaskRestController extends RestController {

    private static final String BLOB = "blob";
    private AdapterManager adapter;

    /**
     * Creates a new TaskRestController
     */
    public TaskRestController() {
        adapter = AdapterManagerFactory.getInstance();
    }

    /**
     * Handles the HTTP 'POST /project/{projectName}/usecase/task' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to create the
     *                usecase in
     * @param task    Task to create
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = TaskRoute.TASK_ENDPOINT, method = RequestMethod.POST)
    public HttpEntity<?> postTask(@PathVariable Map<String, String> params,
                                  @RequestBody String task,
                                  @RequestHeader(value = "Authorization", required = false) String auth,
                                  HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(BLOB, task);

        return mapper.mapResult(adapter.add(Task.class, params), RequestMethod.POST, request.getServletPath());
    }

    /**
     * Handles the HTTP 'GET /project/{projectName}/usecase/task' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to create the
     *                usecase in
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = TaskRoute.TASK_ENDPOINT, method = RequestMethod.GET)
    public HttpEntity<?> getTask(@PathVariable Map<String, String> params,
                                 @RequestHeader(value = "Authorization", required = false) String auth,
                                 HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.get(Task.class, params), RequestMethod.GET, request.getServletPath());
    }

    /**
     * Handles the HTTP 'DELETE /project/{projectName}/usecase/task' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to create the
     *                usecase in
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = TaskRoute.TASK_ENDPOINT, method = RequestMethod.DELETE)
    public HttpEntity<?> deleteTask(@PathVariable Map<String, String> params,
                                    @RequestHeader(value = "Authorization", required = false) String auth,
                                    HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.remove(Task.class, params), RequestMethod.DELETE, request.getServletPath());
    }

    /**
     * Handles the HTTP 'GET /project/{projectName}/usecase/task' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to create the
     *                usecase in
     * @param task    Task to update with
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = TaskRoute.TASK_ENDPOINT, method = RequestMethod.PATCH)
    public HttpEntity<?> patchTask(@PathVariable Map<String, String> params,
                                   @RequestBody String task,
                                   @RequestHeader(value = "Authorization", required = false) String auth,
                                   HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(BLOB, task);

        return mapper.mapResult(adapter.edit(Task.class, params), RequestMethod.PATCH, request.getServletPath());
    }

}
