package be.ugent.vopro1.adapter.result.types;

/**
 * Result type for a DB invalid query
 *
 * @see ResultType
 */
public class DBInvalidQueryResult extends ResultType {
    @Override
    public String getMessage() {
        return "Database error: invalid query";
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }
}
