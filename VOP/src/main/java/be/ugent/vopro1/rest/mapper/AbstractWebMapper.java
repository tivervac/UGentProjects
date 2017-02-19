package be.ugent.vopro1.rest.mapper;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.types.ResultType;
import be.ugent.vopro1.rest.presentationmodel.Error;
import be.ugent.vopro1.rest.presentationmodel.PresentationModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract {@link WebMapper} implementation with some methods that each WebMapper implements
 * the same way
 *
 * @see WebMapper
 */
public abstract class AbstractWebMapper implements WebMapper {

    protected Logger logger = LogManager.getLogger(this.getClass());
    protected Map<Class<?>, Class<?>> presentationModelMap;
    protected Map<RequestMethod, HttpStatus> successStatusMap;
    protected Map<Class<? extends ResultType>, HttpStatus> errorStatusMap;

    @Override
    public void setPresentationModelMap(Map<Class<?>, Class<?>> m){
        presentationModelMap = new HashMap<>(m);
    }

    @Override
    public void setErrorStatusMap(Map<Class<? extends ResultType>, HttpStatus> m){
        errorStatusMap = new HashMap<>(m);
    }

    @Override
    public void setSuccessStatusMap(Map<RequestMethod, HttpStatus> m){
        successStatusMap = new HashMap<>(m);
    }

    @Override
    public ResponseEntity<?> mapResult(Result result, RequestMethod method, String requestPath) {
        return mapResult(result, method, requestPath, null);
    }

    @Override
    public ResponseEntity<?> mapResult(Result result, RequestMethod method, String requestPath, Integer page) {
        if (result.isSuccessful()) {
            return mapSuccess(result, method, requestPath, "", page);
        } else {
            return mapError(result, method, requestPath, "", page);
        }
    }

    @Override
    public ResponseEntity<?> mapResult(Result result, RequestMethod method, String requestPath, String requestQuery, Integer page) {
        if (result.isSuccessful()) {
            return mapSuccess(result, method, requestPath, requestQuery, page);
        } else {
            return mapError(result, method, requestPath, requestQuery, page);
        }
    }

    protected void initialize() {

    }

    /**
     * Converts (part of the) content of a Result to its corresponding
     * PresentationModel. It is assured that the Object to convert is a single
     * Entity object.
     *
     * @param content Content of the result to convert
     * @param requestPath Url request path used for the request that led to the content
     * @param query Query string used in this request, or <code>null</code> if not available
     * @param page Requested page, or <code>null</code> if not available
     * @return PresentationModel of the Result's content
     * @see PresentationModel
     * @see Result
     */
    protected abstract Object convertToPresentationModel(Object content, String requestPath, String query, Integer page);

    /**
     * Maps a successful result to the corresponding response entity
     *
     * @param result Result
     * @param method Request method used for the request that led to the content
     * @param requestPath Url request path used for the request that led to the content
     * @param query Query string used in this request, or <code>null</code> if not available
     * @param page Requested page, or <code>null</code> if not available
     * @return ResponseEntity for this result
     * @see ResponseEntity
     * @see #convertToPresentationModel(Object, String, String, Integer)
     * @see #setSuccessStatusMap(Map)
     */
    protected ResponseEntity<?> mapSuccess(Result result, RequestMethod method, String requestPath, String query, Integer page) {
        Object presentationModel = convertToPresentationModel(result.getContent(), requestPath, query, page);
        HttpStatus httpStatus = successStatusMap.get(method);

        logger.trace("Success returned {} to {} {}", httpStatus.toString(),
                method.toString(), requestPath);

        return new ResponseEntity<>(presentationModel, httpStatus);
    }

    /**
     * Maps a failed result to the corresponding response entity
     *
     * @param result Result
     * @param method Request method used for the request that led to the content
     * @param requestPath Url request path used for the request that led to the content
     * @param query Query string used in this request, or <code>null</code> if not available
     * @param page Requested page, or <code>null</code> if not available
     * @return ResponseEntity for this result
     * @see ResponseEntity
     * @see #setErrorStatusMap(Map)
     */
    protected ResponseEntity<?> mapError(Result result, RequestMethod method, String requestPath, String query, Integer page) {
        HttpStatus httpStatus = errorStatusMap.get(result.getResultType());

        logger.trace("Failure returned {} to {} {}", httpStatus.toString(),
                method.toString(), requestPath);

        Error content = new Error(httpStatus.value(), result.description());
        return new ResponseEntity<>(content, httpStatus);
    }

}
