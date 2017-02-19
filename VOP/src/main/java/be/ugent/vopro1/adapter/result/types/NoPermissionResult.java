package be.ugent.vopro1.adapter.result.types;

/**
 * Result type for a request that requires a greater permission level
 *
 * @see ResultType
 */
public class NoPermissionResult extends ResultType {
    @Override
    public String getMessage() {
        return "Permission error: no rights for this action";
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }
}
