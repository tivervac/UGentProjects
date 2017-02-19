package be.ugent.vopro1.rest.controller;

import be.ugent.vopro1.adapter.AdapterManager;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.model.User;
import be.ugent.vopro1.model.stub.AnalystProjectEntity;
import be.ugent.vopro1.model.stub.MemberTeamEntity;
import be.ugent.vopro1.rest.mapper.WebMapper;
import be.ugent.vopro1.rest.route.UserRoute;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * RestController processing the HTTP requests to the /user endpoints.
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
public class UserRestController extends RestController {

    /**
     * Handles the HTTP 'GET /user' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param page    The page of results
     * @param auth    HTTP Authorization header for authentication of the user
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = UserRoute.GET_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getAllUsers(@RequestParam(required = false, defaultValue = "1") String page,
                                     @RequestHeader(value = "Authorization", required = false) String auth,
                                     HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.getAll(User.class, params),
                RequestMethod.GET,
                request.getServletPath(),
                Integer.valueOf(page));
    }

    /**
     * Handles the HTTP 'POST /user' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param blob    JSON object representing the new user
     * @param auth    HTTP Authorization header for authentication of the user
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = UserRoute.POST, method = RequestMethod.POST)
    public HttpEntity<?> createUser(@RequestBody String blob,
                                    @RequestHeader(value = "Authorization", required = false) String auth,
                                    HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("blob", blob);
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.add(User.class, params), RequestMethod.POST, request.getServletPath());
    }

    /**
     * Handles the HTTP 'GET /user/{userid}' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the identifier of the requested user
     * @param auth    HTTP Authorization header for authentication of the user
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = UserRoute.GET_ONE, method = RequestMethod.GET)
    public HttpEntity<?> getUser(@PathVariable Map<String, String> params,
                                 @RequestHeader(value = "Authorization", required = false) String auth,
                                 HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.get(User.class, params), RequestMethod.GET, trimId(request.getServletPath()));
    }

    /**
     * Handles the HTTP 'GET /user/me' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param auth    HTTP Authorization header for authentication of the user
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = UserRoute.GET_ME, method = RequestMethod.GET)
    public HttpEntity<?> getMe(@RequestHeader(value = "Authorization", required = false) String auth,
                               HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("id", "me");
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.get(User.class, params), RequestMethod.GET, request.getServletPath());
    }

    /**
     * Handles the HTTP 'DELETE /user/{userid}' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the identifier of the user to delete
     * @param auth    HTTP Authorization header for authentication of the user
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = UserRoute.DELETE, method = RequestMethod.DELETE)
    public HttpEntity<?> deleteUser(@PathVariable Map<String, String> params,
                                    @RequestHeader(value = "Authorization", required = false) String auth,
                                    HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.remove(User.class, params), RequestMethod.DELETE, request.getServletPath());
    }

    /**
     * Handles the HTTP 'PATCH /user/{userid}' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param blob    JSON object representing the new user
     * @param params  Map containing the identifier of the user to update
     * @param auth    HTTP Authorization header for authentication of the user
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = UserRoute.PATCH, method = RequestMethod.PATCH)
    public HttpEntity<?> updateUser(@RequestBody String blob,
                                    @PathVariable Map<String, String> params,
                                    @RequestHeader(value = "Authorization", required = false) String auth,
                                    HttpServletRequest request) {
        params.put("blob", blob);
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.edit(User.class, params), RequestMethod.PATCH, trimId(request.getServletPath()));
    }

    /**
     * Handles the HTTP 'GET /user/{userId}/team' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the identifier of the user to update
     * @param analyst Return only analysts if true
     * @param auth    HTTP Authorization header for authentication of the user
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = UserRoute.TEAM_GET_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getUserTeams(@PathVariable Map<String, String> params,
                                      @RequestParam(value = "analyst", required = false, defaultValue = "false") boolean analyst,
                                      @RequestHeader(value = "Authorization", required = false) String auth,
                                      HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put("analystOnly", String.valueOf(analyst));

        return mapper.mapResult(adapter.getAll(MemberTeamEntity.class, params), RequestMethod.GET, request.getServletPath());
    }

    /**
     * Handles the HTTP 'GET /user/{userId}/project' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the identifier of the user to update
     * @param analyst Only retrieve projects that the user is an analyst for.
     *                Currently, this value is ignored and is assumed <code>true</code>
     * @param auth    HTTP Authorization header for authentication of the user
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = UserRoute.PROJECT_GET_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getUserProjects(@PathVariable Map<String, String> params,
                                         @RequestParam(value = "analyst", required = false) String analyst,
                                         @RequestHeader(value = "Authorization", required = false) String auth,
                                         HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.getAll(AnalystProjectEntity.class, params), RequestMethod.GET, request.getServletPath());
    }
}
