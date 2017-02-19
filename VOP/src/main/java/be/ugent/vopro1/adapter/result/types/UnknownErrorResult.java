package be.ugent.vopro1.adapter.result.types;

/**
 * Result type for an unknown exception
 *
 * @see ResultType
 */
public class UnknownErrorResult extends ResultType {
    @Override
    public String getMessage() {
        return "Unknown error";
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }
}
