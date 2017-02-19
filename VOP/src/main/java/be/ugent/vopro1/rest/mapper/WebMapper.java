package be.ugent.vopro1.rest.mapper;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.types.ResultType;
import be.ugent.vopro1.rest.mapper.hateoas.HateoasWebMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;

/**
 * Provides the interface that is used to generate a Spring ResponseEntity based
 * on the the Result created by the
 * {@link be.ugent.vopro1.adapter.AdapterManager}.
 *
 * @see Result
 * @see ResponseEntity
 * @see HateoasWebMapper
 */
public interface WebMapper {

    String NO_LINKS_REQUIRED = "This WebMapper does not require links";

    /**
     * Generates a Spring ResponseEntity based on the Result created by the
     * {@link be.ugent.vopro1.adapter.AdapterManager}.
     * <p>
     * Will determine whether the Result was successful or not and then generate
     * an HTTP ResponseEntity with the appropriate HTTP status code. The body of
     * the HTTP response will contain the requested information or an error
     * message in case of an error.
     *
     * @param result Result object created by the adapter layer
     * @param method HTTP method that started the request and resulted in the
     * creation of the Result object
     * @param requestPath Path used for the request
     * @return the generated Spring ResponseEntity for this request
     * @see Result
     * @see RequestMethod
     */
    ResponseEntity<?> mapResult(Result result, RequestMethod method, String requestPath);

    /**
     * Generates a Spring ResponseEntity based on the Result created by the
     * {@link be.ugent.vopro1.adapter.AdapterManager}.
     * <p>
     * Will determine whether the Result was successful or not and then generate
     * an HTTP ResponseEntity with the appropriate HTTP status code. The body of
     * the HTTP response will contain the requested information or an error
     * message in case of an error.
     *
     * @param result Result object created by the adapter layer
     * @param method HTTP method that started the request and resulted in the
     * creation of the Result object
     * @param requestPath Path used for the request
     * @param page Requested page of a paginated result
     * @return the generated Spring ResponseEntity for this request
     * @see Result
     * @see RequestMethod
     */
    ResponseEntity<?> mapResult(Result result, RequestMethod method, String requestPath, Integer page);

    /**
     * Generates a Spring ResponseEntity based on the Result created by the
     * {@link be.ugent.vopro1.adapter.AdapterManager}.
     * <p>
     * Will determine whether the Result was successful or not and then generate
     * an HTTP ResponseEntity with the appropriate HTTP status code. The body of
     * the HTTP response will contain the requested information or an error
     * message in case of an error.
     *
     * @param result Result object created by the adapter layer
     * @param method HTTP method that started the request and resulted in the
     * creation of the Result object
     * @param requestPath Path used for the request
     * @param requestQuery Query used for the request
     * @param page Requested page of a paginated result
     * @return the generated Spring ResponseEntity for this request
     * @see Result
     * @see RequestMethod
     */
    ResponseEntity<?> mapResult(Result result, RequestMethod method, String requestPath, String requestQuery, Integer page);

    /**
     * Setter method for the map that associates a class
     * with its PresentationModel class
     * @param m New map to use
     */
    void setPresentationModelMap(Map<Class<?>, Class<?>> m);

    /**
     * Setter method for the map that associates a ResultType class
     * with its HttpStatus code
     * @param m New map to use
     */
    void setErrorStatusMap(Map<Class<? extends ResultType>, HttpStatus> m);

    /**
     * Setter method to associate the correct HttpStatus code
     * with a type of request
     * @param m New map to use
     */
    void setSuccessStatusMap(Map<RequestMethod, HttpStatus> m);

    /**
     * Sets specific links for a certain type of class
     * @param m Map which maps a Class to a list of url strings
     */
    default void setSpecificLinksMap(Map<Class<?>, Map<String, String>> m) {
        throw new UnsupportedOperationException(NO_LINKS_REQUIRED);
    }

    /**
     * Sets general links for a certain type of class
     * i.e. POST, GET, ... urls
     * @param m Map which maps a class to a list of url strings
     */
    default void setGeneralLinksMap(Map<Class<?>, Map<String, String>> m) {
        throw new UnsupportedOperationException(NO_LINKS_REQUIRED);
    }

}
