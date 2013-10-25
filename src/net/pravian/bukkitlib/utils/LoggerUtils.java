package net.pravian.bukkitlib.utils;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class LoggerUtils {

    public static void info(String message) {
        Bukkit.getLogger().info(message);
    }

    public static void info(Plugin plugin, String message) {
        plugin.getLogger().info(message);
    }

    public void warning(String message) {
        Bukkit.getLogger().warning(message);
    }

    public void warning(Plugin plugin, String message) {
        plugin.getLogger().warning(message);
    }

    public void severe(Object message) {
        final String line;

        if (message instanceof Throwable) {
            line = ExceptionUtils.getStackTrace((Throwable) message);
        } else {
            line = String.valueOf(message);
        }

        Bukkit.getLogger().info(line);
    }

    public void severe(Plugin plugin, Object message) {
        final String line;

        if (message instanceof Throwable) {
            line = ExceptionUtils.getStackTrace((Throwable) message);
        } else {
            line = String.valueOf(message);
        }

        plugin.getLogger().info(line);
    }
}
