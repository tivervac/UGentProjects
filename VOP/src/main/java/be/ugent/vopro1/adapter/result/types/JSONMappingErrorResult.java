package be.ugent.vopro1.adapter.result.types;

/**
 * Result type for a JSON mapping error
 *
 * @see ResultType
 */
public class JSONMappingErrorResult extends ResultType {
    @Override
    public String getMessage() {
        return "Input error: HTTP request body contains invalid field names";
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }
}
