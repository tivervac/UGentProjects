package be.ugent.vopro1.util;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * This is an extension for the BiConsumer that allows three parameters.
 *
 * <p>This is a functional interface
 * whose functional method is {@link #accept(Object, Object, Object)}.
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <R> the type of the third argument to the operation
 *
 * @see Consumer
 * @see BiConsumer
 * @see
 * <a href="http://hg.openjdk.java.net/jdk8/jdk8/jdk/file/687fd7c7986d/src/share/classes/java/util/function/BiConsumer.java">
 * BiConsumer.java Sourcecode in OpenJDK</a>
 */
@FunctionalInterface
public interface TriConsumer<T, U, R> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param r the third input argument
     */
    void accept(T t, U u, R r);

    /**
     * Returns a composed {@code TriConsumer} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code TriConsumer} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default TriConsumer<T, U, R> andThen(TriConsumer<? super T, ? super U, ? super R> after) {
        Objects.requireNonNull(after);

        return (t, u, r) -> {
            accept(t, u, r);
            after.accept(t, u, r);
        };
    }
}
