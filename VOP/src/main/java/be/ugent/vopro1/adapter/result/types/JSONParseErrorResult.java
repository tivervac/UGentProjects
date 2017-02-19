package be.ugent.vopro1.adapter.result.types;

/**
 * Result type for a JSON parsing error
 *
 * @see ResultType
 */
public class JSONParseErrorResult extends ResultType {
    @Override
    public String getMessage() {
        return "Input error: HTTP request body contains no valid JSON syntax";
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }
}
