package net.pravian.aero.plugin;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.bukkit.plugin.Plugin;

/*
 * Represents a Plugin-specific Logger . <p><b>Note</b>: The methods in this class also accept all instances of {@link java.lang.Throwable} and will print the attached StackTrace.
 */
public class AeroLogger extends Logger {

  /**
   * The "raw" logger used to display messages without a plugin-tag.
   */
  public final Logger BUKKIT_LOGGER;
  private String pluginName;
  private boolean debugMode;

  /**
   * Creates a new BukkitLogger instance.
   * <p>
   * <p>
   * <b>Note</b>: This method also accepts all instances of {@link java.lang.Throwable} and will
   * print the attached StackTrace.
   *
   * @param plugin The plugin for which the logger will be used.
   */
  public AeroLogger(Plugin plugin) {
    this(plugin, plugin.getServer().getLogger());
  }

  public AeroLogger(Plugin plugin, Logger raw) {
    super(plugin.getClass().getCanonicalName(), null);
    String prefix = plugin.getDescription().getPrefix();
    pluginName =
        prefix != null ? new StringBuilder().append("[").append(prefix).append("] ").toString()
            : "[" + plugin.getDescription().getName() + "] ";
    setParent(raw);
    setLevel(Level.ALL);
    BUKKIT_LOGGER = raw;
  }

  /**
   * Prints an info-message to the plugin logger.
   * <p>
   * <p>
   * <b>Note</b>: This method also accepts all instances of {@link java.lang.Throwable} and will
   * print the attached StackTrace.
   *
   * @param message The message to print.
   */
  public void info(Object message) {
    super.info(getLogMessage(message));
  }

  /**
   * Prints an info-message to the raw Bukkit logger.
   * <p>
   * <p>
   * <b>Note</b>: This method also accepts all instances of {@link java.lang.Throwable} and will
   * print the attached StackTrace.
   *
   * @param message The message to print.
   */
  public void rawInfo(Object message) {
    BUKKIT_LOGGER.info(getLogMessage(message));
  }

  /**
   * Prints a warning-message to the plugin logger.
   * <p>
   * <p>
   * <b>Note</b>: This method also accepts all instances of {@link java.lang.Throwable} and will
   * print the attached StackTrace.
   *
   * @param message The message to print.
   */
  public void warning(Object message) {
    super.warning(getLogMessage(message));
  }

  /**
   * Prints a warning-message to the raw Bukkit logger.
   * <p>
   * <p>
   * <b>Note</b>: This method also accepts all instances of {@link java.lang.Throwable} and will
   * print the attached StackTrace.
   *
   * @param message The message to print.
   */
  public void rawWarning(Object message) {
    BUKKIT_LOGGER.warning(getLogMessage(message));
  }

  /**
   * Prints a severe-message to the plugin logger.
   * <p>
   * <p>
   * <b>Note</b>: This method also accepts all instances of {@link java.lang.Throwable} and will
   * print the attached StackTrace.
   *
   * @param message The message to print.
   */
  public void severe(Object message) {
    super.severe(getLogMessage(message));
  }

  /**
   * Prints a severe-message to the plugin logger.
   *
   * @param message The message to print.
   * @param exception The exception causing this message.
   */
  public void severe(String message, Throwable exception) {
    super.log(Level.SEVERE, message, exception);
  }

  /**
   * Prints a severe-message to the raw Bukkit logger.
   * <p>
   * <p>
   * <b>Note</b>: This method also accepts all instances of {@link java.lang.Throwable} and will
   * print the attached StackTrace.
   *
   * @param message The message to print.
   */
  public void rawSevere(Object message) {
    BUKKIT_LOGGER.severe(getLogMessage(message));
  }

  /**
   * Prints a debug-message to the plugin logger if debug-mode is enabled.
   * <p>
   * <p>
   * <b>Note</b>: This method also accepts all instances of {@link java.lang.Throwable} and will
   * print the attached StackTrace.
   */
  public void debug(Object message) {
    if (!debugMode) {
      return;
    }

    super.info("[DEBUG] " + getLogMessage(message));
  }

  /**
   * Prints a debug-message to the raw Bukkit logger if debug-mode is enabled.
   * <p>
   * <p>
   * <b>Note</b>: This method also accepts all instances of {@link java.lang.Throwable} and will
   * print the attached StackTrace.
   */
  public void rawDebug(Object message) {
    if (!debugMode) {
      return;
    }

    BUKKIT_LOGGER.info("[DEBUG] " + getLogMessage(message));
  }

  /**
   * Validates if debug-mode is enabled.
   *
   * @return true if debug-mode is enabled.
   */
  public boolean getDebugMode() {
    return debugMode;
  }

  /**
   * Enables/disables debug-mode.
   *
   * @param enabled If debug-mode should be enabled.
   * @see #debug(Object)
   */
  public void setDebugMode(boolean enabled) {
    this.debugMode = enabled;
  }

  /**
   * Logs a LogRecord to the plugin logger.
   *
   * @param logRecord The record to log.
   */
  @Override
  public void log(LogRecord logRecord) {
    logRecord.setMessage(pluginName + logRecord.getMessage());
    super.log(logRecord);
  }

  /**
   * Logs a LogRecord to the raw Bukkit logger.
   *
   * @param logRecord The record to log.
   */
  public void rawLog(LogRecord logRecord) {
    logRecord.setMessage(pluginName + logRecord.getMessage());
    BUKKIT_LOGGER.log(logRecord);
  }

  @Deprecated
  public void handleException(String msg) {
    handleException(msg, null);
  }

  @Deprecated
  public void handleException(Throwable ex) {
    handleException(null, ex);
  }

  @Deprecated
  public void handleException(String msg, Throwable ex) {
    if (msg != null) {
      severe(msg);
    }

    if (ex != null) {
      severe(ex);
    }
  }

  private String getLogMessage(Object message) {
    if (message instanceof Throwable) {
      return ExceptionUtils.getStackTrace((Throwable) message);
    } else {
      return String.valueOf(message);
    }
  }
}
