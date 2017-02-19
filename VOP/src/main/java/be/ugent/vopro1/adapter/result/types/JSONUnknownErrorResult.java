package be.ugent.vopro1.adapter.result.types;

/**
 * Result type for a JSON unknown error
 *
 * @see ResultType
 */
public class JSONUnknownErrorResult extends ResultType {
    @Override
    public String getMessage() {
        return "Input error: HTTP request body is invalid";
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }
}
