package be.ugent.vopro1.rest.controller;

import be.ugent.vopro1.adapter.AdapterManager;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.model.ProcessEntity;
import be.ugent.vopro1.rest.mapper.WebMapper;
import be.ugent.vopro1.rest.route.ProjectRoute;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * RestController processing the HTTP requests to the /process endpoints.
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
public class ProcessRestController extends RestController {

    /**
     * Handles the HTTP 'GET /project/{projectName}/process' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Mape containing the name of the project to fetch usecases
     *                from
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.GET_PROCESS_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getAllProjectProcesses(@PathVariable Map<String, String> params,
                                                @RequestHeader(value = "Authorization", required = false) String auth,
                                                HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.getAll(ProcessEntity.class, params), RequestMethod.GET, request.getServletPath());
    }

    /**
     * Handles the HTTP 'GET /project/{projectName}/process/{processName}'
     * request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project containing the
     *                desired usecase and the name of the desired usecase
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.GET_PROCESS_ONE, method = RequestMethod.GET)
    public HttpEntity<?> getProjectProcess(@PathVariable Map<String, String> params,
                                           @RequestHeader(value = "Authorization", required = false) String auth,
                                           HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.get(ProcessEntity.class, params), RequestMethod.GET, trimId(request.getServletPath()));
    }

    /**
     * Handles the HTTP 'POST /project/{projectName}/process' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be c.onverted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to create the
     *                usecase in
     * @param process Process to create
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.POST_PROCESS, method = RequestMethod.POST)
    public HttpEntity<?> postProjectProcess(@PathVariable Map<String, String> params,
                                            @RequestHeader(value = "Authorization", required = false) String auth,
                                            @RequestBody String process,
                                            HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(BLOB, process);

        return mapper.mapResult(adapter.add(ProcessEntity.class, params), RequestMethod.POST, request.getServletPath());
    }

    /**
     * Handles the HTTP 'DELETE /project/{projectName}/process/{name}'
     * request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project containing the
     *                usecase to remove and the name of the usecase to remove
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.DELETE_PROCESS, method = RequestMethod.DELETE)
    public HttpEntity<?> deleteProjectProcess(@PathVariable Map<String, String> params,
                                              @RequestHeader(value = "Authorization", required = false) String auth,
                                              HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        return mapper.mapResult(adapter.remove(ProcessEntity.class, params), RequestMethod.DELETE, request.getServletPath());
    }

    /**
     * Handles the HTTP 'PATCH /project/{projectName}/process/{name}'
     * request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project containing the
     *                usecase to update and the name of the usecase to update
     * @param process Modified process
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.PATCH_PROCESS, method = RequestMethod.PATCH)
    public HttpEntity<?> updateProjectProcess(@PathVariable Map<String, String> params,
                                              @RequestBody String process,
                                              @RequestHeader(value = "Authorization", required = false) String auth,
                                              HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(BLOB, process);

        return mapper.mapResult(adapter.edit(ProcessEntity.class, params), RequestMethod.PATCH, trimId(request.getServletPath()));
    }
}
