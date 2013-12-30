package net.pravian.bukkitlib;

import java.io.IOException;
import net.pravian.bukkitlib.internal.InternalMetrics;
import net.pravian.bukkitlib.internal.InternalMetrics.Graph;
import net.pravian.bukkitlib.internal.InternalSimplePlotter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Represents BukkitLib; a commons library for Bukkit.
 *
 * @author Prozza
 * @version 1.1
 */
public final class BukkitLib extends JavaPlugin {

    /**
     * The name of this library.
     */
    public static final String NAME = "BukkitLib";
    /**
     * The version of this library.
     */
    public static final String VERSION = "1.1";
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

    /**
     * Initializes BukkitLib
     *
     * <p><b>All plugins depending on BukkitLib should call this method at plugin load time.</b></p>
     *
     * @param plugin
     */
    public static void init(Plugin plugin) {
        try {
            final InternalMetrics metrics = new InternalMetrics(plugin, NAME, VERSION);

            final Graph plugins = metrics.createGraph("Plugins");
            plugins.addPlotter(new InternalSimplePlotter(plugin.getDescription().getName()));

            metrics.start();
        } catch (IOException ex) {
            Bukkit.getLogger().warning("[BukkitLib] Failed to submit metrics");
        }
    }
}
