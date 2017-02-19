package be.ugent.vopro1.rest.controller;

import be.ugent.vopro1.adapter.AdapterManager;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.rest.mapper.WebMapper;
import be.ugent.vopro1.rest.route.ConceptRoute;
import be.ugent.vopro1.rest.route.ProjectRoute;
import org.aikodi.lang.funky.concept.Concept;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * RestController processing the HTTP requests to the /concept endpoints.
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
public class ConceptRestController extends RestController {

    /**
     * Handles the HTTP 'GET /concept' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param name    String to search for in concept names
     * @param page    The page of results
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest to handle
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ConceptRoute.GET_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getAllConcepts(@RequestParam(required = false) String name,
                                        @RequestParam(required = false, defaultValue = "1") String page,
                                        @RequestHeader(value = "Authorization", required = false) String auth,
                                        HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        params.put("auth", auth);
        params.put("type", "concept");

        if (name != null) {
            params.put("name", name);
        }

        return mapper.mapResult(adapter.getAll(Concept.class, params),
                RequestMethod.GET,
                request.getServletPath(),
                Integer.valueOf(page));
    }

    /**
     * Handles the HTTP 'GET /project/{projectName}/concept' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to fetch concepts
     *                from
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.GET_CONCEPT_ALL, method = RequestMethod.GET)
    public HttpEntity<?> getAllProjectConcepts(@PathVariable Map<String, String> params,
                                               @RequestHeader(value = "Authorization", required = false) String auth,
                                               HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put("type", CONCEPT_TYPE);

        return mapper.mapResult(adapter.getAll(Concept.class, params), RequestMethod.GET, request.getServletPath());
    }

    /**
     * Handles the HTTP 'GET /project/{projectName}/concept/{conceptName}'
     * request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project containing the
     *                desired concept and the name of the concept to retrieve
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.GET_CONCEPT_ONE, method = RequestMethod.GET)
    public HttpEntity<?> getProjectConcept(@PathVariable Map<String, String> params,
                                           @RequestHeader(value = "Authorization", required = false) String auth,
                                           HttpServletRequest request) {
        params.put(AUTH_ARG, auth);

        return mapper.mapResult(adapter.get(Concept.class, params), RequestMethod.GET, trimId(request.getServletPath()));
    }

    /**
     * Handles the HTTP 'POST /project/{projectName}/concept' request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project to create the
     *                concept in
     * @param concept The concept to create (only name is required)
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.POST_CONCEPT, method = RequestMethod.POST)
    public HttpEntity<?> postProjectConcept(@PathVariable Map<String, String> params,
                                            @RequestBody String concept,
                                            @RequestHeader(value = "Authorization", required = false) String auth,
                                            HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(BLOB, concept);

        return mapper.mapResult(adapter.add(Concept.class, params), RequestMethod.POST, request.getServletPath());
    }

    /**
     * Handles the HTTP 'DELETE /project/{projectName}/concept/{conceptName}'
     * request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params  Map containing the name of the project that contains the
     *                concept to delete and the name of the concept to delete
     * @param auth    HTTP Authorization header for authentication
     * @param request ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.DELETE_CONCEPT, method = RequestMethod.DELETE)
    public HttpEntity<?> deleteProjectConcept(@PathVariable Map<String, String> params,
                                              @RequestHeader(value = "Authorization", required = false) String auth,
                                              HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        return mapper.mapResult(adapter.remove(Concept.class, params), RequestMethod.DELETE, request.getServletPath());
    }

    /**
     * Handles the HTTP 'PATCH /project/{projectName}/concept/{conceptName}'
     * request.
     * <p>
     * Passes this HTTP request to the adapter layer, where this request will be
     * handled. This adapter layer will return a Result object to this
     * RestController. This Result object will be converted to a valid
     * HttpEntity object by a WebMapper instance. This HttpEntity represents the
     * HTTP response.
     *
     * @param params   Map containing the name of the project that contains the
     *                 concept to update and the (old) name of the concept to update
     * @param refactor Refactoring requested or not
     * @param concept  Modified concept
     * @param auth     HTTP Authorization header for authentication
     * @param request  ServletRequest that should be handled
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = ProjectRoute.PATCH_CONCEPT, method = RequestMethod.PATCH)
    public HttpEntity<?> updateProjectConcept(@PathVariable Map<String, String> params,
                                              @RequestParam(value = "refactor", required = false) String refactor,
                                              @RequestHeader(value = "Authorization", required = false) String auth,
                                              @RequestBody String concept,
                                              HttpServletRequest request) {
        params.put(AUTH_ARG, auth);
        params.put(BLOB, concept);
        params.put(REFACTOR_ARG, refactor);

        return mapper.mapResult(adapter.edit(Concept.class, params), RequestMethod.PATCH, trimId(request.getServletPath()));
    }

}
