package be.ugent.vopro1.util;

import java.io.IOException;

/**
 * A custom Exception because function pointers don't function if they throw a
 * checked exception.
 *
 * @see RuntimeException
 */
public class RuntimeIOException extends RuntimeException {

    private RuntimeIOException() {
        super();
    }

    private RuntimeIOException(String message) {
        super(message);
    }

    private RuntimeIOException(String message, Throwable cause) {
        super(message, cause);
    }

    private RuntimeIOException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new RuntimeIOException with given cause
     *
     * @param ex Exception causing this RuntimeIOException
     */
    public RuntimeIOException(IOException ex) {
        super(ex);
    }

    /**
     * Creates a new RuntimeIOException with given message and cause
     *
     * @param message Message describing the cause of this exception
     * @param ex Exception causing this RuntimeIOException
     */
    public RuntimeIOException(String message, IOException ex) {
        super(message, ex);
    }

}
