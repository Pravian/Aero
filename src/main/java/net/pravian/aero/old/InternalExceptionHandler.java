package net.pravian.aero.old;

import net.pravian.aero.exception.ExceptionHandler;
import net.pravian.aero.util.Loggers;

public final class InternalExceptionHandler {

    private static ExceptionHandler handler;
    private static boolean throwExceptions;

    static {
        handler = null;
        throwExceptions = false;
    }
    private static final ExceptionHandler defaultHandler = new ExceptionHandler() {

        @Override
        public void handleException(String msg) {
            Loggers.severe(msg);
        }

        @Override
        public void handleException(Throwable ex) {
            Loggers.severe(ex);
        }

        @Override
        public void handleException(String msg, Throwable ex) {
            Loggers.severe(msg);
            Loggers.severe(ex);
        }
    };

    static {
        handler = null;
        throwExceptions = false;
    }

    private InternalExceptionHandler() {
    }

    public static void setExceptionHandler(ExceptionHandler handler) {
        InternalExceptionHandler.handler = handler;
    }

    public static void handle(Throwable ex) {
        if (throwExceptions) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            }
        }

        if (handler != null) {
            handler.handleException(ex);
            return;
        }

        defaultHandler.handleException(ex);
    }

    public static void setThrowExceptions(boolean throwExceptions) {
        InternalExceptionHandler.throwExceptions = throwExceptions;
    }
}
