package net.pravian.bukkitlib;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import net.pravian.bukkitlib.internal.InternalMetrics;
import net.pravian.bukkitlib.metrics.Graph;
import net.pravian.bukkitlib.metrics.FixedDonutPlotter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents BukkitLib; a commons library for Bukkit.
 *
 * @author Prozza
 */
public final class BukkitLib extends JavaPlugin {

    /**
     * The name of this library.
     */
    public static final String NAME = "BukkitLib";
    /**
     * The version of this library.
     */
    public static final String VERSION = "1.2";
    /**
     * The author of this library.
     */
    public static final String AUTHOR = "Prozza";
    /**
     * The features of this library.
     */
    public static final String[] FEATURES = new String[]{
        "Compact serialization for Bukkit objects (Inventory, Block, Location(Block & Entity)",
        "Easy config-file management with YamlConfig",
        "Exporting of net.minecraft.* using ServerUtil",
        "Utils for: Logging, Plugin, Commands, Ips, Players, Worlds, Dates, File management, the Chat, Inventories, Items",
        "Includes Cleanroom world generator",
        "Includes mcstats.org metrics",
        "Various concurrency tools"
    };
    /**
     * The credits to the making of this library.
     */
    public static final String[] CREDITS = new String[]{
        "HeXeRei452/WickedGamingUK (Bukkit Forums) for his interest and support",
        "StevenLawson (Github) for his great helper methods in TotalFreedomMod",
        "Phil2812 (Bukkit Forums) for his inventory serializer",
        "mkyong (mykong.com) for his great examples on how to write objects to files",
        "nvx (Bukkit Forums) for their Cleanroom generator",
        "sk89q (sk89q.com) for his getField() method",
        "bergerkiller for various methods and utilities",
        "The Essentials development team for a larger range of utilities"
    };
    /**
     * The change log to this library.
     */
    public static final String[] CHANGELOG = new String[]{
        "-- 1.2:",
        "  - Fixed errors not showing when metrics failed",
        "  - Fixed glitch with command permissions",
        "  - Added setWeather in WorldUtils",
        "  - Added IncrementalGraph",
        "  - Removed usage in CommandPermissions",
        "  - Refractored .set and .get to .setMap and .getMap in YamlConfig",
        "  - Added DonutPlotter, FixedPlotter and FixedDonutPlotter",
        "  - Cleaned up InternalMetrics",
        "  - Added BukkitLibNotInitializedException",
        "  - Added build number and date tracking through maven",
        "",
        "-- 1.1:",
        "  - Added BukkitPermissionHolder, deprecates BukkitPermissionHandler",
        "  - Added internal metrics through BukkitLib.init()",
        "  - Added BlockUtil",
        "  - Added BukkitSyncTask and BukkitAsyncTask",
        "  - Added ItemUtil",
        "  - Added BukkitSign",
        "  - Renamed PluginLogger to BukkitLogger",
        "  - Added SelectionUtils",
        "  - Added PlayerLobby",
        "  - Added .setMap(Map) and .getMap() in YamlConfig",
        "  - Bugfixes",
        "  - Javadoc fixes",
        "",
        "-- 1.0:",
        "  - Batch format & cleanup",
        "  - Removed generator: SkyGrid",
        "  - Undeprecated LoggerUtil",
        "",
        "-- 1.7-Beta:",
        "  - Updated to CraftBukkit 1.7-R0.1",
        "  - Added PluginLogger instance to BukkitCommand",
        "  - Minor changes",
        "",
        "-- 1.6-Beta:",
        "  - Implemented Bukkit build generator, moved away from plugin-based BukkitLib",
        "  - Fixed all JavaDoc",
        "  - Added debug-style logging in PluginLogger",
        "  - Minor changes",
        "",
        "-- 1.5-Beta:",
        "  - Added PluginLogger, replaces LoggerUtils (now deprecated)",
        "  - Renamed net.pravian.bukkitlib.utils to net.bukkitlib.pravian.util",
        "  - Small changes",
        "",
        "-- 1.4-Beta:",
        "  - Added JavaDoc",
        "  - Minor changes",
        "",
        "-- 1.3-Beta:",
        "  - Added BukkitPermissionHandler",
        "  - Added ability to assign permission handlers with BukkitCommandHandler.setPermissionHandler()",
        "  - Changed all net.pravian.bukkitlib.command classes and methods to instantiated versions",
        "  - Moved net.pravian.util.* to net.pravian.java.*",
        "",
        "-- 1.2-Beta:",
        "  - Added VoidChunkGenerator",
        "  - Added SkyGridGenerator",
        "  - Added BukkitCommand, BukkitCommandHandler, CommandPermissions and SourceType",
        "  - Added Plugin interface",
        "  - Removed BukkitLib.init()",
        "  - Few more Utils methods",
        "",
        "-- 1.1-Beta:",
        "  - Added BukkitLib.init(JavaPlugin)",
        "  - Moved everything which needs plugins over to BukkitLib.getPlugin()",
        "",
        "-- 1.0-Beta: Initial release --",
        "  - Added Cleanroom generator",
        "  - Added YamlConfig",
        "  - Added SerializableBlock",
        "  - Added SerializableBlockLocation",
        "  - Added SerializableEntityLocation",
        "  - Added SerializableInventory",
        "  - Added ChatUtils, CommandUtils, DateUtils, FileUtils, IpUtils, LoggerUtils, PlayerUtils, ServerUtils and WorldUtils",
        "  - Using net.pravian.util.Block, SingletonBlock, ClosedBlock",
        ""
    };
    private static boolean init = false;
    private static String buildNumber;
    private static String buildDate;

    /**
     * Initializes BukkitLib
     *
     * <p><b>All plugins depending on BukkitLib should call this method at plugin load time.</b></p>
     *
     * @param plugin
     */
    public static void init(Plugin plugin) {
        init = true;

        loadBuildInformation();

        try {
            final InternalMetrics metrics = new InternalMetrics(plugin, NAME, VERSION);

            final Graph version = metrics.createGraph("Version");
            version.addPlotter(new FixedDonutPlotter(VERSION, buildNumber));

            final Graph plugins = metrics.createGraph("Plugins");
            plugins.addPlotter(new FixedDonutPlotter(plugin.getDescription().getName(), plugin.getDescription().getVersion()));

            metrics.start();
        } catch (IOException ex) {
            Bukkit.getLogger().warning("[BukkitLib] Failed to submit metrics");
        }
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

        return VERSION + "." + buildNumber;
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

    private static void loadBuildInformation() {
        // Plugin build-number and build-date
        try {
            final InputStream in = BukkitLib.class.getResourceAsStream(
                    "/" + BukkitLib.class.getPackage().getName().replace('.', '/') + "/build.properties");
            final Properties build = new Properties();

            build.load(in);
            in.close();

            buildNumber = build.getProperty("app.buildnumber");
            buildDate = build.getProperty("app.builddate");

        } catch (Exception ex) {
            Bukkit.getLogger().warning("[BukkitLib] Could not load build information!");
        }
    }
}
