package be.ugent.vopro1.adapter.result;

import be.ugent.vopro1.adapter.result.types.ResultType;

/**
 * The Result class serves as a result type class and can be identified by its
 * ResultType enumeration. The class also holds a possible content Object which
 * can be a Concept, Exception or a list of Concepts. Result objects are used to
 * return information to a calling class of the REST layer, calling adapter
 * objects.
 *
 * @param <R> Type represented by this result
 * @param <U> Content of the result
 * @see ResultType
 */
public class Result<R extends ResultType, U> {

    private R resultType;
    private U content;

    /**
     * Constructs a Result.
     *
     * @param type result type enumeration value
     * @param content content for this result
     */
    public Result(R type, U content) {
        this.resultType = type;
        this.content = content;
    }

    /**
     * Constructs a Result.
     *
     * @param type result type enumeration value
     */
    public Result(R type) {
        this.resultType = type;
    }

    /**
     * Gets the Result type class.
     *
     * @return class of this result type
     */
    public Class<? extends ResultType> getResultType() {
        return resultType.getClass();
    }

    /**
     * Checks whether operation is successful, in any other case an error has
     * occurred.
     *
     * @return true when operation has been executed successfully, false in any
     * other case
     */
    public boolean isSuccessful() {
        return resultType.isSuccessful();
    }

    /**
     * Gets the textual representation of the Result.
     *
     * @return the actual ResultType String.
     */
    public String description() {
        return resultType.getMessage();
    }

    /**
     * Retrieves content from Result.
     *
     * @return content of the Result
     */
    public U getContent() {
        return content;
    }
}
