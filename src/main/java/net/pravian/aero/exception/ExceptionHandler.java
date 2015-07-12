package net.pravian.aero.exception;

/**
 * Represents an exception handler capable of handling thrown exceptions.
 */
public interface ExceptionHandler {

    /**
     * Handles an exception or exception string for a plugin.
     *
     * @param msg The message for the exception.
     */
    public void handleException(String msg);

    /**
     * Handles an exception or exception.
     *
     * @param ex The exception thrown.
     */
    public void handleException(Throwable ex);

    /**
     * Handles an exception or exception.
     *
     * @param msg The message for the exception.
     * @param ex The exception thrown.
     */
    public void handleException(String msg, Throwable ex);
}
