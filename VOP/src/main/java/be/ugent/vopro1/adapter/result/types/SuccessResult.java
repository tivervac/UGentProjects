package be.ugent.vopro1.adapter.result.types;

/**
 * Result type for a successful request
 *
 * @see ResultType
 */
public class SuccessResult extends ResultType {
    @Override
    public String getMessage() {
        return "Operation successfully executed";
    }

    @Override
    public boolean isSuccessful() {
        return true;
    }
}
