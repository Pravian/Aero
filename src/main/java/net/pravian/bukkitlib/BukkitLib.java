package net.pravian.bukkitlib;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import net.pravian.bukkitlib.metrics.Graph;
import net.pravian.bukkitlib.metrics.FixedDonutPlotter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginBase;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * Represents BukkitLib; a commons library for Bukkit.
 */
public final class BukkitLib {

    private BukkitLib() {
    }
    /**
     * The name of this library.
     */
    public static final String NAME = "BukkitLib";
    /**
     * The author of this library.
     */
    public static final String AUTHOR = "Prozza";
    //
    private static String buildVersion;
    private static String buildNumber;
    private static String buildDate;
    //
    private static final Set<String> noMetrics;
    private static boolean init;
    private static boolean debug;

    static {
        noMetrics = new HashSet<String>();
        init = false;
        debug = false;
    }

    /**
     * Initializes BukkitLib
     *
     * <p><b>All plugins depending on BukkitLib should call this method at plugin load time.</b></p>
     *
     * @param plugin
     */
    public static void init(PluginBase plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException();
        }

        final PluginDescriptionFile pdf = plugin.getDescription();
        if (pdf.getVersion() == null
                || pdf.getVersion().isEmpty()) {
            throw new BukkitLibIncompletePluginException(plugin);
        }

        init = true;
        loadBuildInformation();

        if (noMetrics.contains(plugin.getName())) {
            return;
        }

        try {
            final InternalMetrics metrics = new InternalMetrics(plugin, NAME, buildVersion);

            final Graph version = metrics.createGraph("Version");
            version.addPlotter(new FixedDonutPlotter(buildVersion, buildNumber));

            final Graph plugins = metrics.createGraph("Plugins");
            plugins.addPlotter(new FixedDonutPlotter(plugin.getDescription().getName(), plugin.getDescription().getVersion()));

            metrics.start();
        } catch (IOException ex) {
            if (BukkitLib.debug) {
                Bukkit.getLogger().warning("[BukkitLib] Failed to submit metrics for plugin: " + plugin.getName());
            }
        }
    }

    /**
     * Validates if BukkitLib has been initialized.
     *
     * @return True if BukkitLib has been initialized.
     */
    public static boolean isInitialized() {
        return init;
    }

    /**
     * Returns the build version of this BukkitLib build.
     *
     * @return The BukkitLib version.
     */
    public static String getVersion() {
        if (!init) {
            throw new BukkitLibNotInitializedException();
        }
        return buildVersion;
    }

    /**
     * Returns the build number of this BukkitLib build.
     *
     * @return The build number.
     */
    public static String getBuildNumber() {
        if (!init) {
            throw new BukkitLibNotInitializedException();
        }
        return buildNumber;
    }

    /**
     * Returns the full BukkitLib version in the format <b>major.minor.build</b>.
     *
     * @return The version.
     */
    public static String getFullVersion() {
        if (!init) {
            throw new BukkitLibNotInitializedException();
        }

        return buildVersion + "." + buildNumber;
    }

    /**
     * Returns the date this BukkitLib build was compiled.
     *
     * @return The date
     */
    public static String getBuildDate() {
        if (!init) {
            throw new BukkitLibNotInitializedException();
        }

        return buildDate;
    }

    /**
     * Explicitly disables the metrics for this plugin.
     *
     * <p><b>Note</b>: Using this will make the author sad... ;(
     *
     * @param plugin The plugin to disable metrics for.
     */
    public static void explicitDisableMetrics(PluginBase plugin) {
        noMetrics.add(plugin.getName());
    }

    /**
     * Sets the exception handler.
     *
     * <p>All runtime-exceptions will be handled by the exception handler.</p>
     *
     * <p>Set to null to reset to the default handler. The default handler prints the output on the screen.</p>
     *
     * @param handler The new handler.
     */
    public static void setExceptionHandler(ExceptionHandler handler) {
        InternalExceptionHandler.setExceptionHandler(handler);
    }

    /**
     * Explicitly enables throwing exceptions.
     */
    public static void explicitEnableExceptionThrowing() {
        InternalExceptionHandler.setThrowExceptions(true);
    }

    /**
     * Sets if BukkitLib-related error messages should be printed.
     *
     * @param debug The debugging state.
     */
    public static void setDebugMode(boolean debug) {
        BukkitLib.debug = debug;
    }

    /**
     * Validates if debug-mode is enabled.
     *
     * @return True if debug-mode is enabled.
     */
    public static boolean getDebugMode() {
        return BukkitLib.debug;
    }

    /**
     * Validates if metrics are disabled for a plugin.
     *
     * @param plugin The plugin.
     * @return True if metrics are disabled.
     */
    public static boolean isExplicitMetricsDisabled(Plugin plugin) {
        return noMetrics.contains(plugin.getName());
    }

    private static void loadBuildInformation() {
        // Plugin build-number and build-date
        try {
            final InputStream in = BukkitLib.class
                    .getResourceAsStream(
                    "/" + BukkitLib.class
                    .getPackage().getName().replace('.', '/') + "/build.properties");
            final Properties build = new Properties();

            build.load(in);

            in.close();
            buildVersion = build.getProperty("app.buildversion");
            buildNumber = build.getProperty("app.buildnumber");
            buildDate = build.getProperty("app.builddate");
        } catch (Exception ex) {
            if (BukkitLib.debug) {
                Bukkit.getLogger().warning("[BukkitLib] Could not load build information!");
            }
        }
    }
}
