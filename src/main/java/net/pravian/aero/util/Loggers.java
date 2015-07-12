package net.pravian.aero.util;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Represents all Logger-related utilities.
 *
 */
public class Loggers {

    private Loggers() {
    }

    /**
     * Prints an info-message to the raw Bukkit logger.
     *
     * @param message The message to print.
     */
    public static void info(String message) {
        Bukkit.getLogger().info(message);
    }

    /**
     * Prints an info-message to the plugin logger.
     *
     * @param plugin The Plugin to use.
     * @param message The message to print.
     */
    public static void info(Plugin plugin, String message) {
        plugin.getLogger().info(message);
    }

    /**
     * Prints a warning-message to the raw Bukkit logger.
     *
     * @param message The message to print.
     */
    public static void warning(String message) {
        Bukkit.getLogger().warning(message);
    }

    /**
     * Prints a warning-message to the plugin logger.
     *
     * @param plugin The Plugin to use.
     * @param message The message to print.
     */
    public static void warning(Plugin plugin, String message) {
        plugin.getLogger().warning(message);
    }

    /**
     * Prints a severe-message to the raw Bukkit logger.
     *
     * <p>
     * <b>Note</b>: This methods also accepts all instances of {@link java.lang.Throwable} and will print the attached stacktrace.
     *
     * @param message The message to print.
     */
    public static void severe(Object message) {
        final String line;

        if (message instanceof Throwable) {
            line = ExceptionUtils.getStackTrace((Throwable) message);
        } else {
            line = String.valueOf(message);
        }

        Bukkit.getLogger().info(line);
    }

    /**
     * Prints a severe-message to the plugin logger.
     *
     * <p>
     * <b>Note</b>: This methods also accepts all instances of {@link java.lang.Throwable} and will print the attached stacktrace.
     *
     * @param plugin The Plugin to use.
     * @param message The message to print.
     */
    public static void severe(Plugin plugin, Object message) {
        final String line;

        if (message instanceof Throwable) {
            line = ExceptionUtils.getStackTrace((Throwable) message);
        } else {
            line = String.valueOf(message);
        }

        plugin.getLogger().info(line);
    }
}
