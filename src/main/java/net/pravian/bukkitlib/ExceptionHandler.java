package net.pravian.bukkitlib;

import org.bukkit.plugin.Plugin;

/**
 * Represents an exception handler capable of handling thrown exceptions.
 */
public interface ExceptionHandler {

    /**
     * Handles an exception or exception string.
     *
     * @param ex The exception or string.
     */
    public void handleException(Object ex);

    /**
     * Handles an exception or exception string for a plugin.
     *
     * @param pl The plugin to handle the exception for.
     * @param ex The exception or string.
     */
    public void handleException(Plugin pl, Object ex);
}
