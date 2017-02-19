package be.ugent.vopro1.rest.controller;

import be.ugent.vopro1.adapter.AdapterManager;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.model.stub.ProjectActionEntity;
import be.ugent.vopro1.rest.mapper.WebMapper;
import be.ugent.vopro1.rest.route.ProjectRoute;
import be.ugent.vopro1.scheduling.Schedule;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * RestController processing the HTTP requests to the /project/{projectName}/schedule endpoints.
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

public class ScheduleRestController extends RestController {

    /**
     * Handles the HTTP 'PUT /project/{projectName}/schedule' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the date from which we have to start rescheduling
     * @param action  Description of the action
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.ACTION_SCHEDULE, method = RequestMethod.PUT)
    public HttpEntity<?> actOnProjectScheduling(@PathVariable Map<String, String> params,
                                                @RequestBody String action,
                                                @RequestHeader(value = "Authorization", required = false) String auth,
                                                HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(ACTION_ARG, action);
        params.put(ACTION_TYPE, "schedule");

        return mapper.mapResult(adapter.act(ProjectActionEntity.class, params), RequestMethod.PUT, trimId(request.getServletPath()));
    }

    /**
     * Handles the HTTP 'GET /project/{projectName}/schedule' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project
     * @param page    The page of results
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.GET_SCHEDULE, method = RequestMethod.GET)
    public HttpEntity<?> getProjectScheduling(@PathVariable Map<String, String> params,
                                              @RequestParam(required = false, defaultValue = "1") String page,
                                              @RequestHeader(value = "Authorization", required = false) String auth,
                                              HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.get(Schedule.class, params),
                RequestMethod.GET,
                request.getServletPath(),
                Integer.valueOf(page));
    }

}
