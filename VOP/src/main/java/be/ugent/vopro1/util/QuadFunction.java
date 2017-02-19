package be.ugent.vopro1.util;

import java.util.Objects;
import java.util.function.Function;

/**
 * This is an extension for the BiFunction that allows four parameters.
 *
 * @see
 * <a href="http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/classes/java/util/function/BiFunction.java">
 * BiFunction.java Sourcecode in OpenJDK</a>
 *
 * @param <T> Type of parameter 1
 * @param <U> Type of parameter 2
 * @param <V> Type of parameter 3
 * @param <W> Type of parameter 4
 * @param <R> Type of the returned result
 */
@FunctionalInterface
public interface QuadFunction<T, U, V, W, R> {

    /**
     * Executes the TriFunction with given parameters
     * @param t Parameter 1
     * @param u Parameter 2
     * @param v Parameter 3
     * @param w Parameter 4
     * @return Result of the execution
     */
    R apply(T t, U u, V v, W w);

    /**
     * Returns a composed function that first applies this function to its
     * input, and then applies the {@code after} function to the result. If
     * evaluation of either function throws an exception, it is relayed to the
     * caller of the composed function.
     * <p>
     * A QuadFunction accepts functions with 4 parameters as opposed to the
     * existing BiFunction that only accepts functions using only 2 parameters.
     *
     * @param <S> the type of output of the {@code after} function, and of the
     * composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     */
    default <S> QuadFunction<T, U, V, W, S> andThen(Function<? super R, ? extends S> after) {
        Objects.requireNonNull(after);
        return (T t, U u, V v, W w) -> after.apply(apply(t, u, v, w));
    }
}
