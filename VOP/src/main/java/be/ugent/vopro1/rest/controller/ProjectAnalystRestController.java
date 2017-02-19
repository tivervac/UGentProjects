package be.ugent.vopro1.rest.controller;

import be.ugent.vopro1.adapter.AdapterManager;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.model.stub.ProjectAnalystEntity;
import be.ugent.vopro1.rest.mapper.WebMapper;
import be.ugent.vopro1.rest.route.ProjectRoute;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * RestController processing the HTTP requests to the /project/{projectName}/analyst endpoints.
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
public class ProjectAnalystRestController extends RestController {

    /**
     * Handles the HTTP 'GET /project/{projectName}/analyst' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to remove the actor
     *                from and the name of the actor to remove
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.GET_ANALYST_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getAllAnalysts(@PathVariable Map<String, String> params,
                                        @RequestHeader(value = "Authorization", required = false) String auth,
                                        HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.getAll(ProjectAnalystEntity.class, params), RequestMethod.GET, request.getServletPath());
    }

    /**
     * Handles the HTTP 'GET /project/{projectName}/eligible_analyst' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to remove the actor
     *                from and the name of the actor to remove
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.GET_ELIGIBLE_ANALYST_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getAllEligibleAnalysts(@PathVariable Map<String, String> params,
                                                @RequestHeader(value = "Authorization", required = false) String auth,
                                                HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(SPEC, "eligible");

        return mapper.mapResult(adapter.getAll(ProjectAnalystEntity.class, params), RequestMethod.GET, request.getServletPath());
    }

    /**
     * Handles the HTTP 'POST /project/{projectName}/analyst/{userId}' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to remove the actor
     *                from and the name of the actor to remove
     * @param blob    JSON blob containing the amount of time the analyst can work on the project, in seconds
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.POST_ANALYST, method = RequestMethod.POST)
    public HttpEntity<?> addAnalyst(@PathVariable Map<String, String> params,
                                    @RequestBody String blob,
                                    @RequestHeader(value = "Authorization", required = false) String auth,
                                    HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(WORK_ARG, blob);

        return mapper.mapResult(adapter.add(ProjectAnalystEntity.class, params), RequestMethod.POST, request.getServletPath());
    }

    /**
     * Handles the PATCH 'PATCH /project/{projectName}/analyst/{userId}' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to remove the actor
     *                from and the name of the actor to remove
     * @param blob    JSON blob containing the analyst and his/her workload
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.PATCH_ANALYST, method = RequestMethod.PATCH)
    public HttpEntity<?> patchAnalyst(@PathVariable Map<String, String> params,
                                      @RequestBody String blob,
                                      @RequestHeader(value = "Authorization", required = false) String auth,
                                      HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(WORK_ARG, blob);

        return mapper.mapResult(adapter.edit(ProjectAnalystEntity.class, params), RequestMethod.PATCH, request.getServletPath());
    }

    /**
     * Handles the HTTP 'DELETE /project/{projectName}/analyst/{userId}'
     * request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to remove the actor
     *                from and the name of the actor to remove
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.DELETE_ANALYST, method = RequestMethod.DELETE)
    public HttpEntity<?> deleteAnalyst(@PathVariable Map<String, String> params,
                                       @RequestHeader(value = "Authorization", required = false) String auth,
                                       HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.remove(ProjectAnalystEntity.class, params), RequestMethod.DELETE, request.getServletPath());
    }
}
