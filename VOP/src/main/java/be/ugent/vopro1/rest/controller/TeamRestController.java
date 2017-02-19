package be.ugent.vopro1.rest.controller;

import be.ugent.vopro1.adapter.AdapterManager;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.model.Team;
import be.ugent.vopro1.model.stub.TeamMemberEntity;
import be.ugent.vopro1.model.stub.TeamProjectEntity;
import be.ugent.vopro1.rest.mapper.WebMapper;
import be.ugent.vopro1.rest.route.TeamRoute;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * RestController processing the HTTP requests to the /team endpoints.
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
public class TeamRestController extends RestController {

    /**
     * Handles the HTTP 'GET /team' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param page    The page of results
     * @param auth    HTTP Authorization header for authentication of the team
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = TeamRoute.GET_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getAllTeams(@RequestParam(required = false, defaultValue = "1") String page,
                                     @RequestHeader(value = "Authorization", required = false) String auth,
                                     HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.getAll(Team.class, params),
                RequestMethod.GET,
                request.getServletPath(),
                Integer.valueOf(page));
    }

    /**
     * Handles the HTTP 'POST /team' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param blob    JSON object representing the new team
     * @param auth    HTTP Authorization header for authentication of the team
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = TeamRoute.POST, method = RequestMethod.POST)
    public HttpEntity<?> createTeam(@RequestBody String blob,
                                    @RequestHeader(value = "Authorization", required = false) String auth,
                                    HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("blob", blob);
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.add(Team.class, params), RequestMethod.POST, request.getServletPath());
    }

    /**
     * Handles the HTTP 'GET /team/{teamid}' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the identifier of the requested team
     * @param auth    HTTP Authorization header for authentication of the team
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = TeamRoute.GET_ONE, method = RequestMethod.GET)
    public HttpEntity<?> getTeam(@PathVariable Map<String, String> params,
                                 @RequestHeader(value = "Authorization", required = false) String auth,
                                 HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.get(Team.class, params), RequestMethod.GET, trimId(request.getServletPath()));
    }

    /**
     * Handles the HTTP 'DELETE /team/{teamid}' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the identifier of the team to delete
     * @param auth    HTTP Authorization header for authentication of the team
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = TeamRoute.DELETE, method = RequestMethod.DELETE)
    public HttpEntity<?> deleteTeam(@PathVariable Map<String, String> params,
                                    @RequestHeader(value = "Authorization", required = false) String auth,
                                    HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.remove(Team.class, params), RequestMethod.DELETE, request.getServletPath());
    }

    /**
     * Handles the HTTP 'PATCH /team/{teamid}' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the identifier of the team to update
     * @param auth    HTTP Authorization header for authentication of the team
     * @param request ServletRequest to handle
     * @param blob    JSON object representing the new team
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = TeamRoute.PATCH, method = RequestMethod.PATCH)
    public HttpEntity<?> updateTeam(@RequestBody String blob,
                                    @PathVariable Map<String, String> params,
                                    @RequestHeader(value = "Authorization", required = false) String auth,
                                    HttpServletRequest request) {
        params.put("blob", blob);
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.edit(Team.class, params), RequestMethod.PATCH, trimId(request.getServletPath()));
    }

    /**
     * Handles the HTTP 'GET /team/{teamId}/member' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to remove the actor
     *                from and the name of the actor to remove
     * @param analyst Only return team members who are analysts
     * @param auth    HTTP Authorization header for authentication of the team
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = TeamRoute.MEMBER_GET_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getAllTeamMembers(@PathVariable Map<String, String> params,
                                           @RequestParam(value = "analyst", required = false, defaultValue = "false") boolean analyst,
                                           @RequestHeader(value = "Authorization", required = false) String auth,
                                           HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put("analystOnly", String.valueOf(analyst));

        return mapper.mapResult(adapter.getAll(TeamMemberEntity.class, params), RequestMethod.GET, request.getServletPath());
    }

    /**
     * Handles the HTTP 'POST /team/{teamId}/member/{userId}' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to remove the actor
     *                from and the name of the actor to remove
     * @param auth    HTTP Authorization header for authentication of the team
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = TeamRoute.MEMBER_POST, method = RequestMethod.POST)
    public HttpEntity<?> addTeamMember(@PathVariable Map<String, String> params,
                                       @RequestHeader(value = "Authorization", required = false) String auth,
                                       HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.add(TeamMemberEntity.class, params), RequestMethod.POST, request.getServletPath());
    }

    /**
     * Handles the HTTP 'DELETE /team/{teamId}/member/{userId}' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to remove the actor
     *                from and the name of the actor to remove
     * @param auth    HTTP Authorization header for authentication of the team
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = TeamRoute.MEMBER_DELETE, method = RequestMethod.DELETE)
    public HttpEntity<?> removeTeamMember(@PathVariable Map<String, String> params,
                                          @RequestHeader(value = "Authorization", required = false) String auth,
                                          HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.remove(TeamMemberEntity.class, params), RequestMethod.DELETE, request.getServletPath());
    }

    /**
     * Handles the HTTP 'GET /team/{teamId}/project' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to remove the actor
     *                from and the name of the actor to remove
     * @param auth    HTTP Authorization header for authentication of the team
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = TeamRoute.PROJECT_GET_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getAllProjectsResponsible(@PathVariable Map<String, String> params,
                                                   @RequestHeader(value = "Authorization", required = false) String auth,
                                                   HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.getAll(TeamProjectEntity.class, params), RequestMethod.GET, request.getServletPath());
    }

    /**
     * Handles the HTTP 'POST /team/{teamId}/project/{projectName}' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to remove the actor
     *                from and the name of the actor to remove
     * @param auth    HTTP Authorization header for authentication of the team
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = TeamRoute.PROJECT_POST, method = RequestMethod.POST)
    public HttpEntity<?> addProjectResponsible(@PathVariable Map<String, String> params,
                                               @RequestHeader(value = "Authorization", required = false) String auth,
                                               HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.add(TeamProjectEntity.class, params), RequestMethod.POST, request.getServletPath());
    }

    /**
     * Handles the HTTP 'DELETE /team/{teamId}/project/{projectName}' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to remove the actor
     *                from and the name of the actor to remove
     * @param auth    HTTP Authorization header for authentication of the team
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = TeamRoute.PROJECT_DELETE, method = RequestMethod.DELETE)
    public HttpEntity<?> deleteProjectResponsible(@PathVariable Map<String, String> params,
                                                  @RequestHeader(value = "Authorization", required = false) String auth,
                                                  HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.remove(TeamProjectEntity.class, params), RequestMethod.DELETE, request.getServletPath());
    }
}
