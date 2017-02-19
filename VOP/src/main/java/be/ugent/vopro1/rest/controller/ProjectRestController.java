package be.ugent.vopro1.rest.controller;

import be.ugent.vopro1.adapter.AdapterManager;
import be.ugent.vopro1.adapter.AdapterManagerFactory;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.model.EntityProject;
import be.ugent.vopro1.model.stub.ProjectActionEntity;
import be.ugent.vopro1.rest.mapper.WebMapper;
import be.ugent.vopro1.rest.route.ProjectRoute;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * RestController processing the HTTP requests to the /project endpoints.
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
public class ProjectRestController extends RestController {

    private AdapterManager adapter;

    /**
     * Constructs a new ProjectRestController.
     */
    public ProjectRestController() {
        adapter = AdapterManagerFactory.getInstance();
    }

    /**
     * Handles the HTTP 'GET /project' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param name           String to search for in project names
     * @param teamAssignable Should it only return projects that can still have a team assigned to them
     * @param page           The page of results
     * @param auth           HTTP Authorization header for authentication
     * @param request        ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.GET_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getAllProjects(@RequestParam(required = false) String name,
                                        @RequestParam(value = "team_assignable", required = false, defaultValue = "false") boolean teamAssignable,
                                        @RequestParam(required = false, defaultValue = "1") String page,
                                        @RequestHeader(value = "Authorization", required = false) String auth,
                                        HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put(AUTH_ARG, auth);
        if (name != null) {
            params.put("name", name);
        }
        params.put("team_assignable", String.valueOf(teamAssignable));

        return mapper.mapResult(adapter.getAll(EntityProject.class, params),
                RequestMethod.GET,
                request.getServletPath(),
                request.getQueryString(),
                Integer.valueOf(page));
    }

    /**
     * Handles the HTTP 'POST /project' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param blob    JSON object representing the new project
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.POST, method = RequestMethod.POST)
    public HttpEntity<?> createProject(@RequestBody String blob,
                                       @RequestHeader(value = "Authorization", required = false) String auth,
                                       HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put(AUTH_ARG, auth);
        params.put(BLOB, blob);

        return mapper.mapResult(adapter.add(EntityProject.class, params), RequestMethod.POST, request.getServletPath());
    }

    /**
     * Handles the HTTP 'GET /project/{projectName}' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing name of the project to fetch
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.GET_ONE, method = RequestMethod.GET)
    public HttpEntity<?> getProject(@PathVariable Map<String, String> params,
                                    @RequestHeader(value = "Authorization", required = false) String auth,
                                    HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.get(EntityProject.class, params), RequestMethod.GET, trimId(request.getServletPath()));
    }

    /**
     * Handles the HTTP 'DELETE /project/{projectName}' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing name of the project to remove
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.DELETE, method = RequestMethod.DELETE)
    public HttpEntity<?> deleteProject(@PathVariable Map<String, String> params,
                                       @RequestHeader(value = "Authorization", required = false) String auth,
                                       HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        return mapper.mapResult(adapter.remove(EntityProject.class, params), RequestMethod.DELETE, request.getServletPath());
    }

    /**
     * Handles the HTTP 'PATCH /project/{projectName}' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the (old) name of the project to update
     * @param blob    New JSON blob for the project
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.PATCH, method = RequestMethod.PATCH)
    public HttpEntity<?> updateProject(@PathVariable Map<String, String> params,
                                       @RequestBody String blob,
                                       @RequestHeader(value = "Authorization", required = false) String auth,
                                       HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put("blob", blob);

        return mapper.mapResult(adapter.edit(EntityProject.class, params), RequestMethod.PATCH, trimId(request.getServletPath()));
    }

    /**
     * Handles the HTTP 'PUT /project/{projectName}/concept/{conceptName}'
     * request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project containing the
     *                usecase to update and the name of the usecase to act upon
     * @param action  Description of the action
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.ACTION_ENTITY, method = RequestMethod.PUT)
    public HttpEntity<?> actOnProjectEntity(@PathVariable Map<String, String> params,
                                            @RequestBody String action,
                                            @RequestHeader(value = "Authorization", required = false) String auth,
                                            HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(ACTION_ARG, action);

        return mapper.mapResult(adapter.act(ProjectActionEntity.class, params), RequestMethod.PUT, trimId(request.getServletPath()));
    }

}
