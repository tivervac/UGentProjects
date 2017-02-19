package be.ugent.vopro1.rest.controller;

import be.ugent.vopro1.adapter.AdapterManager;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.model.UsecaseEntity;
import be.ugent.vopro1.model.stub.ProjectActionEntity;
import be.ugent.vopro1.rest.mapper.WebMapper;
import be.ugent.vopro1.rest.route.ProjectRoute;
import be.ugent.vopro1.rest.route.UsecaseRoute;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * RestController processing the HTTP requests to the /usecase endpoints.
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
public class UsecaseRestController extends RestController {

    /**
     * Handles the HTTP 'GET /usecase' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param name    String to search for in usecase names
     * @param page    The page of results
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = UsecaseRoute.GET_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getAllUsecases(@RequestParam(required = false) String name,
                                        @RequestParam(required = false, defaultValue = "1") String page,
                                        @RequestHeader(value = "Authorization", required = false) String auth,
                                        HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("type", "usecase");
        params.put("auth", auth);

        if (name != null) {
            params.put("name", name);
        }

        return mapper.mapResult(adapter.getAll(UsecaseEntity.class, params),
                RequestMethod.GET,
                request.getServletPath(),
                Integer.valueOf(page));
    }


    /**
     * Handles the HTTP 'GET /project/{projectName}/usecase' request.
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
    @RequestMapping(value = ProjectRoute.GET_USECASE_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getAllProjectUsecases(@PathVariable Map<String, String> params,
                                               @RequestHeader(value = "Authorization", required = false) String auth,
                                               HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put("type", USECASE_TYPE);

        return mapper.mapResult(adapter.getAll(UsecaseEntity.class, params), RequestMethod.GET, request.getServletPath());
    }

    /**
     * Handles the HTTP 'GET /project/{projectName}/usecase/{usecaseName}'
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
    @RequestMapping(value = ProjectRoute.GET_USECASE_ONE, method = RequestMethod.GET)
    public HttpEntity<?> getProjectUsecase(@PathVariable Map<String, String> params,
                                           @RequestHeader(value = "Authorization", required = false) String auth,
                                           HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.get(UsecaseEntity.class, params), RequestMethod.GET, trimId(request.getServletPath()));
    }

    /**
     * Handles the HTTP 'POST /project/{projectName}/usecase' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to create the
     *                usecase in
     * @param usecase Usecase to create
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.POST_USECASE, method = RequestMethod.POST)
    public HttpEntity<?> postProjectUsecase(@PathVariable Map<String, String> params,
                                            @RequestHeader(value = "Authorization", required = false) String auth,
                                            @RequestBody String usecase,
                                            HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(BLOB, usecase);

        return mapper.mapResult(adapter.add(UsecaseEntity.class, params), RequestMethod.POST, request.getServletPath());
    }

    /**
     * Handles the HTTP 'DELETE /project/{projectName}/usecase/{usecaseName}'
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
    @RequestMapping(value = ProjectRoute.DELETE_USECASE, method = RequestMethod.DELETE)
    public HttpEntity<?> deleteProjectUsecase(@PathVariable Map<String, String> params,
                                              @RequestHeader(value = "Authorization", required = false) String auth,
                                              HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        return mapper.mapResult(adapter.remove(UsecaseEntity.class, params), RequestMethod.DELETE, request.getServletPath());
    }

    /**
     * Handles the HTTP 'PATCH /project/{projectName}/usecase/{usecaseName}'
     * request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params   Map containing the name of the project containing the
     *                 usecase to update and the name of the usecase to update
     * @param refactor Refactoring requested or not
     * @param usecase  Modified usecase
     * @param auth     HTTP Authorization header for authentication
     * @param request  ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.PATCH_USECASE, method = RequestMethod.PATCH)
    public HttpEntity<?> updateProjectUsecase(@PathVariable Map<String, String> params,
                                              @RequestParam(value = "refactor", required = false) String refactor,
                                              @RequestBody String usecase,
                                              @RequestHeader(value = "Authorization", required = false) String auth,
                                              HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(BLOB, usecase);
        params.put(REFACTOR_ARG, refactor);

        return mapper.mapResult(adapter.edit(UsecaseEntity.class, params), RequestMethod.PATCH, trimId(request.getServletPath()));
    }


    /**
     * Handles the HTTP 'PUT /project/{projectName}/usecase/{usecaseName}'
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
    @RequestMapping(value = ProjectRoute.ACTION_USECASE, method = RequestMethod.PUT)
    public HttpEntity<?> actOnProjectUsecase(@PathVariable Map<String, String> params,
                                             @RequestBody String action,
                                             @RequestHeader(value = "Authorization", required = false) String auth,
                                             HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(ACTION_ARG, action);
        params.put(ACTION_TYPE, USECASE_ACTION_TYPE);

        return mapper.mapResult(adapter.act(ProjectActionEntity.class, params), RequestMethod.PUT, trimId(request.getServletPath()));
    }
}
