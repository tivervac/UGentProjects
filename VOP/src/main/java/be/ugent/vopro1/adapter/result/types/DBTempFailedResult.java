package be.ugent.vopro1.adapter.result.types;

/**
 * Result type for a DB temporary failure
 *
 * @see ResultType
 */
public class DBTempFailedResult extends ResultType {
    @Override
    public String getMessage() {
        return "Database error: temporary database failure";
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }
}
