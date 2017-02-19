package be.ugent.vopro1.adapter.result.mapper;

import be.ugent.vopro1.adapter.result.Result;
import be.ugent.vopro1.adapter.result.types.ResultType;

/**
 * Provides a way to map Exceptions to Results.
 *
 * @see Exception
 * @see Result
 * @see ResultType
 */
public interface ResultMapper {

    /**
     * Registers a new Exception that should be mapped to 'result.
     * @param type Class of the Exception
     * @param result ResultType to be returned
     */
    void registerException(Class type, ResultType result);

    /**
     * Maps an exception
     *
     * @param ex Exception to map
     * @return Mapped exception
     */
    Result mapException(Exception ex);
}
