package net.pravian.bukkitlib;

import net.pravian.bukkitlib.util.LoggerUtils;
import org.bukkit.plugin.Plugin;

public final class InternalExceptionHandler {

    private static ExceptionHandler handler;
    private static boolean throwExceptions;

    static {
        handler = null;
        throwExceptions = false;
    }
    private static final ExceptionHandler defaultHandler = new ExceptionHandler() {
        @Override
        public void handleException(Object ex) {
            LoggerUtils.severe(ex);
        }

        @Override
        public void handleException(Plugin pl, Object ex) {
            LoggerUtils.severe(pl, ex);
        }
    };

    static {
        handler = null;
        throwExceptions = false;
    }

    private InternalExceptionHandler() {
    }

    static void setExceptionHandler(ExceptionHandler handler) {
        InternalExceptionHandler.handler = handler;
    }

    public static void handle(Object ex) {
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

    public static void handle(Plugin plugin, Object ex) {
        if (throwExceptions) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            }
        }

        if (handler != null) {
            handler.handleException(plugin, ex);
            return;
        }

        defaultHandler.handleException(plugin, ex);
    }

    static void setThrowExceptions(boolean throwExceptions) {
        InternalExceptionHandler.throwExceptions = throwExceptions;
    }
}
