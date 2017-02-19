package be.ugent.vopro1.funky;

import org.aikodi.chameleon.core.lookup.LookupException;

/**
 * Our own RuntimeLookupException
 *
 * @see RuntimeException
 * @see LookupException
 */
public class RuntimeLookupException extends RuntimeException {

    private RuntimeLookupException() {
        super();
    }

    private RuntimeLookupException(String message) {
        super(message);
    }

    private RuntimeLookupException(String message, Throwable cause) {
        super(message, cause);
    }

    private RuntimeLookupException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new RuntimeLookupException with a {@link LookupException} as cause
     *
     * @param ex Cause of the exception
     */
    public RuntimeLookupException(LookupException ex) {
        super(ex);
    }

    /**
     * Creates a new RuntimeLookupException with a {@link LookupException} as cause and a message
     * explaining why it occurred.
     *
     * @param message Explanatory message for the exception
     * @param ex Cause of the exception
     */
    public RuntimeLookupException(String message, LookupException ex) {
        super(message, ex);
    }
}
