package be.ugent.vopro1.adapter.result.types;

/**
 * Result type for a DB unknown error
 *
 * @see ResultType
 */
public class DBUnknownErrorResult extends ResultType {
    @Override
    public String getMessage() {
        return "Database error: unknown failure";
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }
}
