package be.ugent.vopro1.util.error;

/**
 * Exception that can be thrown if certain preconditions are not met for a client request
 */
public class RequirementNotMetException extends RuntimeException {

    /**
     * Creates a new RequirementNotMetException with given reason
     *
     * @param reason Explanation for why the precondition fails
     */
    public RequirementNotMetException(String reason) {
        super(reason);
    }
}
