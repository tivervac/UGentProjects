package be.ugent.vopro1.rest.controller;

import be.ugent.vopro1.adapter.AdapterManager;
import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.types.SuccessResult;
import be.ugent.vopro1.rest.mapper.WebMapper;
import be.ugent.vopro1.rest.presentationmodel.ApiPresentationModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * RestController processing the HTTP requests to the root (<code>/</code>) endpoint.
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
public class ApiRestController extends RestController {

    /**
     * Handles the HTTP 'GET /' request.
     * <p>
     * This will show all the possible endpoints in its HTTP response.
     *
     * @return Spring HTTP ResponseEntity representing the HTTP response
     * @see HttpEntity
     * @see ResponseEntity
     * @see AdapterManager
     * @see WebMapper
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public HttpEntity<?> getAllEndpoints() {
        return mapper.mapResult(new Result<>(new SuccessResult(), new ApiPresentationModel()), RequestMethod.GET, "");
    }

}
