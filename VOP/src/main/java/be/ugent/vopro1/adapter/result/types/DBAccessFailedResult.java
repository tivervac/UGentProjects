package be.ugent.vopro1.adapter.result.types;

/**
 * Result type for a DB access failure
 *
 * @see ResultType
 */
public class DBAccessFailedResult extends ResultType {
    @Override
    public String getMessage() {
        return "Database error: access to database failed";
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }
}
