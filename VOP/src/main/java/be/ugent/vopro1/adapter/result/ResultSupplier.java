package be.ugent.vopro1.adapter.result;

import be.ugent.vopro1.util.QuadFunction;
import be.ugent.vopro1.util.TriConsumer;
import be.ugent.vopro1.util.TriFunction;

import java.util.function.*;

/**
 * Provides various ways to abstract away the exception catching in adapters
 *
 * @see be.ugent.vopro1.adapter.CommonAdapter
 */
public interface ResultSupplier {

    /**
     * Executes a function with one parameter
     *
     * @param func Function, with a single parameter, to execute
     * @param param1 Parameter of the function to execute
     * @param <T> Type of the function parameter
     * @param <R> Type of the function result
     * @return Execution result after catching possible RuntimeExceptions
     * @see Function
     * @see Result
     */
    <T, R> Result get(Function<T, R> func, T param1);

    /**
     * Executes a function with two parameters
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
    <T, U, R> Result get(BiFunction<T, U, R> func, T param1, U param2);

    /**
     * Executes a function with three parameters
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
    <T, U, V, R> Result get(TriFunction<T, U, V, R> func, T param1, U param2, V param3);

    /**
     * Executes a function with four parameters
     *
     * @param func Function, with three parameters, to execute
     * @param param1 First parameter of the function to execute
     * @param param2 Second parameter of the function to execute
     * @param param3 Third parameter of the function to execute
     * @param param4 Fourth parameter of the function to execute
     * @param <T> Type of the first function parameter
     * @param <U> Type of the second function parameter
     * @param <V> Type of the third function parameter
     * @param <W> Type of the fourth function parameter
     * @param <R> Type of the function result
     * @return Execution result after catching possible RuntimeExceptions
     * @see TriFunction
     * @see Result
     */
    <T, U, V, W, R> Result get(QuadFunction<T, U, V, W, R> func, T param1, U param2, V param3, W param4);

    /**
     * Executes a function with one parameter without a result (void)
     *
     * @param func Function, with one parameter and no return value, to execute
     * @param param1 First parameter of the function to execute
     * @param <T> Type of the first function parameter
     * @return Execution result after catching possible RuntimeExceptions
     * @see Consumer
     * @see Result
     */
    <T> Result consume(Consumer<T> func, T param1);

    /**
     * Executes a function with two parameters without a result (void)
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
    <T, U> Result consume(BiConsumer<T, U> func, T param1, U param2);

    /**
     * Executes a function with three parameters without a result (void)
     *
     * @param func Function, with three parameters and no return value, to execute
     * @param param1 First parameter of the function to execute
     * @param param2 Second parameter of the function to execute
     * @param param3 Third parameter of the function to execute
     * @param <T> Type of the first function parameter
     * @param <U> Type of the second function parameter
     * @param <R> Type of the third function parameter
     *
     * @return Execution result after catching possible RuntimeExceptions
     * @see TriConsumer
     * @see Result
     */
    <T, U, R> Result consume(TriConsumer<T, U, R> func, T param1, U param2, R param3);

    /**
     * Executes a function without parameters
     *
     * @param func Function, without parameters, to execute
     * @param <R> Type of the function result
     * @return Execution result after catching possible RuntimeExceptions
     * @see Supplier
     * @see Result
     */
    <R> Result supply(Supplier<R> func);

}
