package be.ugent.vopro1.adapter.result.mapper;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.types.*;
import be.ugent.vopro1.interactor.permission.NoPermissionException;
import be.ugent.vopro1.persistence.exception.DBInvalidQueryException;
import be.ugent.vopro1.util.error.RequirementNotMetException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.NonTransientDataAccessResourceException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.dao.TransientDataAccessResourceException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Default implementation of the {@link ResultMapper} interface
 *
 * @see ResultMapper
 */
public class DefaultResultMapper implements ResultMapper {

    private Map<Class<? extends Exception>, ResultType> results;
    Logger logger = LogManager.getLogger(this.getClass());

    /**
     * Default implementation of the result mapper
     * Adds key-value pairs for all standard result types
     */
    public DefaultResultMapper(){
        results = new HashMap<>();

        registerException(JsonParseException.class, new JSONParseErrorResult());
        registerException(JsonMappingException.class, new JSONMappingErrorResult());
        registerException(IOException.class, new JSONUnknownErrorResult());
        registerException(NoPermissionException.class, new NoPermissionResult());
        registerException(DBInvalidQueryException.class, new DBInvalidQueryResult());
        registerException(RecoverableDataAccessException.class, new DBAccessFailedResult());
        registerException(NonTransientDataAccessResourceException.class, new DBInvalidQueryResult());
        registerException(TransientDataAccessResourceException.class, new DBTempFailedResult());
        registerException(IllegalArgumentException.class, new UnknownErrorResult());
        registerException(NoSuchElementException.class, new DBInvalidQueryResult());
        registerException(RequirementNotMetException.class, new ConditionFailedResult("A condition wasn't met!"));
    }

    @Override
    public void registerException(Class type, ResultType result) {
        if (type == null || result == null){
            throw new IllegalArgumentException();
        }

        results.put(type, result);
    }

    @Override
    public Result mapException(Exception ex) {
        if (ex == null){
            throw new IllegalArgumentException();
        }

        Class<? extends Exception> exceptionClass = ex.getClass();

        ResultType resultType = results.get(exceptionClass);

        if (resultType == null){
            resultType = new UnknownErrorResult();
        }

        logger.error("Encountered an exception. Responded with: " + resultType.getMessage(), ex);

        return new Result<>(resultType);
    }
}
