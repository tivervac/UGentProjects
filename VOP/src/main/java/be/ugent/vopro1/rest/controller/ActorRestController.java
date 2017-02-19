package be.ugent.vopro1.rest.controller;

import be.ugent.vopro1.adapter.AdapterManager;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.rest.mapper.WebMapper;
import be.ugent.vopro1.rest.route.ActorRoute;
import be.ugent.vopro1.rest.route.ProjectRoute;
import org.aikodi.lang.funky.executors.Actor;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * RestController processing the HTTP requests to the /actor endpoints.
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
public class ActorRestController extends RestController {

    /**
     * Handles the HTTP 'GET /actor' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param name    String to search for in actor names
     * @param page    The page of results
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ActorRoute.GET_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getAllActors(@RequestParam(required = false) String name,
                                      @RequestParam(required = false, defaultValue = "1") String page,
                                      @RequestHeader(value = "Authorization", required = false) String auth,
                                      HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("auth", auth);
        params.put("type", "actor");

        if (name != null) {
            params.put("name", name);
        }

        return mapper.mapResult(adapter.getAll(Actor.class, params),
                RequestMethod.GET,
                request.getServletPath(),
                Integer.valueOf(page));
    }


    /**
     * Handles the HTTP 'GET /project/{projectName}/actor' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to fetch actors from
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.GET_ACTOR_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getAllProjectActors(@PathVariable Map<String, String> params,
                                             @RequestHeader(value = "Authorization", required = false) String auth,
                                             HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put("type", ACTOR_TYPE);

        return mapper.mapResult(adapter.getAll(Actor.class, params), RequestMethod.GET, request.getServletPath());
    }

    /**
     * Handles the HTTP 'GET /project/{projectName}/actor/{actorName}' request.
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
    @RequestMapping(value = ProjectRoute.GET_ACTOR_ONE, method = RequestMethod.GET)
    public HttpEntity<?> getProjectActor(@PathVariable Map<String, String> params,
                                         @RequestHeader(value = "Authorization", required = false) String auth,
                                         HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.get(Actor.class, params), RequestMethod.GET, trimId(request.getServletPath()));
    }

    /**
     * Handles the HTTP 'POST /project/{projectName}/actor' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to create the actor
     *                in
     * @param blob    JSON object representing the new actor
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.POST_ACTOR, method = RequestMethod.POST)
    public HttpEntity<?> postProjectActor(@PathVariable Map<String, String> params,
                                          @RequestHeader(value = "Authorization", required = false) String auth,
                                          @RequestBody String blob,
                                          HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(BLOB, blob);

        return mapper.mapResult(adapter.add(Actor.class, params), RequestMethod.POST, request.getServletPath());
    }

    /**
     * Handles the HTTP 'DELETE /project/{projectName}/actor/{actorName}'
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
    @RequestMapping(value = ProjectRoute.DELETE_ACTOR, method = RequestMethod.DELETE)
    public HttpEntity<?> deleteProjectActor(@PathVariable Map<String, String> params,
                                            @RequestHeader(value = "Authorization", required = false) String auth,
                                            HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        return mapper.mapResult(adapter.remove(Actor.class, params), RequestMethod.DELETE, request.getServletPath());
    }

    /**
     * Handles the HTTP 'PATCH /project/{projectName}/actor/{actorName}'
     * request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params   Map containing the name of the project that contains the
     *                 actor to update and the name of the actor to update
     * @param refactor Refactoring requested or not
     * @param actor    Modified actor
     * @param auth     HTTP Authorization header for authentication
     * @param request  ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.PATCH_ACTOR, method = RequestMethod.PATCH)
    public HttpEntity<?> updateProjectActor(@PathVariable Map<String, String> params,
                                            @RequestParam(value = "refactor", required = false) String refactor,
                                            @RequestHeader(value = "Authorization", required = false) String auth,
                                            @RequestBody String actor,
                                            HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(BLOB, actor);
        params.put(REFACTOR_ARG, refactor);

        return mapper.mapResult(adapter.edit(Actor.class, params), RequestMethod.PATCH, trimId(request.getServletPath()));
    }

}
