package be.ugent.vopro1.adapter.result;

import be.ugent.vopro1.adapter.result.types.*;
import be.ugent.vopro1.util.QuadFunction;
import be.ugent.vopro1.util.TriConsumer;
import be.ugent.vopro1.util.TriFunction;
import be.ugent.vopro1.util.error.RequirementNotMetException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.dao.TransientDataAccessException;

import java.util.function.*;

/**
 * An implementation of the ResultSupplier.
 * 
 * @see ResultSupplier
 */
public class ResultSupplierImpl implements ResultSupplier {

    Logger logger = LogManager.getLogger(this.getClass());

    /**
     * {@inheritDoc}
     *
     * @param func Function, with a single parameter, to execute
     * @param param1 Parameter of the function to execute
     * @param <T> Type of the function parameter
     * @param <R> Type of the function result
     * @return Execution result after catching possible RuntimeExceptions
     * @see Function
     * @see Result
     */
    @Override
    public <T, R> Result get(Function<T, R> func, T param1) {
        return execute(() -> func.apply(param1));
    }

    /**
     * {@inheritDoc}
     *
     * @param func Function, with two parameters, to execute
     * @param param1 First parameter of the function to execute
     * @param param2 Second parameter of the function to execute
     * @param <T> Type of the first function parameter
     * @param <U> Type of the second function parameter
     * @param <R> Type of the function result
     * @return Execution result after catching possible RuntimeExceptions
     * @see BiFunction
     * @see Result
     */
    @Override
    public <T, U, R> Result get(BiFunction<T, U, R> func, T param1, U param2) {
        return execute(() -> func.apply(param1, param2));
    }

    /**
     * {@inheritDoc}
     *
     * @param func Function, with three parameters, to execute
     * @param param1 First parameter of the function to execute
     * @param param2 Second parameter of the function to execute
     * @param param3 Third parameter of the function to execute
     * @param <T> Type of the first function parameter
     * @param <U> Type of the second function parameter
     * @param <V> Type of the third function parameter
     * @param <R> Type of the function result
     * @return Execution result after catching possible RuntimeExceptions
     * @see TriFunction
     * @see Result
     */
    @Override
    public <T, U, V, R> Result get(TriFunction<T, U, V, R> func, T param1, U param2, V param3) {
        return execute(() -> func.apply(param1, param2, param3));
    }

    @Override
    public <T, U, V, W, R> Result get(QuadFunction<T, U, V, W, R> func, T param1, U param2, V param3, W param4) {
        return execute(() -> func.apply(param1, param2, param3, param4));
    }

    /**
     * {@inheritDoc}
     *
     * @param func Function, with one parameter and no return value, to execute
     * @param param1 First parameter of the function to execute
     * @param <T> Type of the first function parameter
     * @return Execution result after catching possible RuntimeExceptions
     * @see Consumer
     * @see Result
     */
    @Override
    public <T> Result consume(Consumer<T> func, T param1) {
        return execute(() -> {
            func.accept(param1);
            return null; // Incompatible type: void cannot be converted to Void otherwise!
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param func Function, with two parameters and no return value, to execute
     * @param param1 First parameter of the function to execute
     * @param param2 Second parameter of the function to execute
     * @param <T> Type of the first function parameter
     * @param <U> Type of the second function parameter
     * @return Execution result after catching possible RuntimeExceptions
     * @see BiConsumer
     * @see Result
     */
    @Override
    public <T, U> Result consume(BiConsumer<T, U> func, T param1, U param2) {
        return execute(() -> {
            func.accept(param1, param2);
            return null; // Incompatible type: void cannot be converted to Void otherwise!
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param func {@inheritDoc}
     * @param param1 {@inheritDoc}
     * @param param2 {@inheritDoc}
     * @param param3 {@inheritDoc}
     * @param <T> Type of the first function parameter
     * @param <U> Type of the second function parameter
     * @param <R> Type of the third function parameter
     *
     * @return {@inheritDoc}
     * @see TriConsumer
     * @see Result
     */
    @Override
    public <T, U, R> Result consume(TriConsumer<T, U, R> func, T param1, U param2, R param3) {
        return execute(() -> {
            func.accept(param1, param2, param3);
            return null; // Incompatible type: void cannot be converted to Void otherwise!
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param func Function, without parameters, to execute
     * @param <R> Type of the function result
     * @return Execution result after catching possible RuntimeExceptions
     * @see Supplier
     * @see Result
     */
    @Override
    public <R> Result supply(Supplier<R> func) {
        return execute(func);
    }

    /**
     * Executes the method and returns a fitting Result object depending on any
     * RuntimeExceptions encountered during the execution.
     *
     * @param supplier Method to execute
     * @param <R> Type of the method result
     * @return Fitting Result object depending on possible RuntimeExceptions
     * @see Result
     * @see Supplier
     */
    private <R> Result execute(Supplier<R> supplier) {
        try {
            return new Result(new SuccessResult(), supplier.get());
        } catch (RecoverableDataAccessException e) {
            logger.warn("Encountered a recoverable exception", e);
            return new Result(new DBAccessFailedResult());
        } catch (NonTransientDataAccessException e) {
            logger.debug("Encountered a non-transient exception", e);
            return new Result(new DBInvalidQueryResult());
        } catch (TransientDataAccessException e) {
            logger.debug("Encountered a transient data exception", e);
            return new Result(new DBTempFailedResult());
        } catch (DataAccessException e) {
            logger.debug("Encountered a data access exception", e);
            return new Result(new DBUnknownErrorResult());
        } catch (RequirementNotMetException e) {
            logger.trace("Requirement for interaction was not met", e);
            return new Result(new ConditionFailedResult(e.getMessage()));
        } catch (Exception e) {
            logger.error("Encountered an unknown exception", e);
            return new Result(new UnknownErrorResult());
        }
    }

}
