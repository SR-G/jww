package org.tensin.jww;

/**
 * The Class CoreException.
 */
public class CoreException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Instantiates a new core exception.
     */
    public CoreException() {
        super();
    }

    /**
     * Instantiates a new core exception.
     * 
     * @param message
     *            the message
     */
    public CoreException(final String message) {
        super(message);
    }

    /**
     * Instantiates a new core exception.
     * 
     * @param message
     *            the message
     * @param cause
     *            the cause
     */
    public CoreException(final String message, final Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new core exception.
     * 
     * @param cause
     *            the cause
     */
    public CoreException(final Throwable cause) {
        super(cause);
    }

}
