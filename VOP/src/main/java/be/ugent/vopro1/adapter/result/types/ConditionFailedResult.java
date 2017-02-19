package be.ugent.vopro1.adapter.result.types;

/**
 * Result type for a failed precondition
 *
 * @see ResultType
 */
public class ConditionFailedResult extends ResultType {

    private String message;

    /**
     * Creates a new ConditionFailedResult with given message
     *
     * @param message Message explaining why the condition failed
     */
    public ConditionFailedResult(String message) {
        super();
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public boolean isSuccessful() {
        return false;
    }
}
